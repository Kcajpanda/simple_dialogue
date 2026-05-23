package io.github.kcajpanda.simpledialogue.dialogue;

import io.github.kcajpanda.simpledialogue.ClickSide;
import io.github.kcajpanda.simpledialogue.PlayerDialogueSessions.Session;
import io.github.kcajpanda.simpledialogue.SimpleDialoguePlugin;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Loads dialogue YAML files and advances player conversations.
 *
 * <p>This class also centralizes NPC/player text formatting so every NPC line receives
 * the configured display-name prefix automatically.</p>
 */
public final class DialogueManager {
    private final SimpleDialoguePlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, Dialogue> dialogues = new HashMap<>();

    public DialogueManager(SimpleDialoguePlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        dialogues.clear();
        File directory = new File(plugin.getDataFolder(), "dialogues");
        if (!directory.exists() && !directory.mkdirs()) {
            plugin.getLogger().warning("Could not create dialogues directory.");
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (files == null) {
            return;
        }

        for (File file : files) {
            try {
                Dialogue dialogue = loadDialogue(file);
                dialogues.put(dialogue.id().toLowerCase(Locale.ROOT), dialogue);
            } catch (RuntimeException exception) {
                plugin.getLogger().warning("Could not load dialogue " + file.getName() + ": " + exception.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + dialogues.size() + " dialogue file(s).");
    }

    public Collection<String> dialogueIds() {
        return Collections.unmodifiableSet(dialogues.keySet());
    }

    /**
     * Finds a loaded dialogue by id, ignoring case.
     *
     * @param id dialogue id from a YAML file
     * @return the loaded dialogue when present
     */
    public Optional<Dialogue> find(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(dialogues.get(id.toLowerCase(Locale.ROOT)));
    }

    public boolean runClick(Player player, String dialogueId, ClickSide side) {
        Optional<Dialogue> optionalDialogue = find(dialogueId);
        if (optionalDialogue.isEmpty()) {
            player.sendMessage(Component.text("Unknown dialogue: " + dialogueId));
            return false;
        }

        Dialogue dialogue = optionalDialogue.get();
        Optional<Session> optionalSession = plugin.sessions().get(player.getUniqueId())
            .filter(session -> session.dialogueId().equals(dialogue.id()));
        if (optionalSession.isEmpty()) {
            return startDialogue(player, dialogue);
        }

        Dialogue.DialogueNode current = dialogue.nodes().getOrDefault(optionalSession.get().node(), dialogue.startNode());
        if (current == null) {
            player.sendMessage(Component.text("Dialogue " + dialogue.id() + " has no start node."));
            plugin.sessions().clear(player.getUniqueId());
            return false;
        }

        String next = side == ClickSide.LEFT ? current.left() : current.right();
        if (next == null || next.isBlank()) {
            plugin.sessions().clear(player.getUniqueId());
            return true;
        }

        Dialogue.DialogueNode nextNode = dialogue.nodes().get(next);
        if (nextNode == null) {
            player.sendMessage(Component.text("Dialogue " + dialogue.id() + " is missing node " + next + "."));
            plugin.sessions().clear(player.getUniqueId());
            return false;
        }

        sendNode(player, dialogue, nextNode);
        if (nextNode.end()) {
            plugin.sessions().clear(player.getUniqueId());
        } else {
            plugin.sessions().setNode(player.getUniqueId(), dialogue.id(), nextNode.id());
        }
        return true;
    }

    public boolean setNpcProfile(String dialogueId, String npcName, String nameColor, String bracketColor) {
        Optional<Dialogue> optionalDialogue = find(dialogueId);
        if (optionalDialogue.isEmpty()) {
            return false;
        }

        File file = dialogueFile(dialogueId);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("npc.name", npcName);
        yaml.set("npc.name-color", nameColor);
        yaml.set("npc.bracket-color", bracketColor);
        save(yaml, file);
        reload();
        return true;
    }

    public boolean setFancyNpc(String dialogueId, String fancyNpc) {
        Optional<Dialogue> optionalDialogue = find(dialogueId);
        if (optionalDialogue.isEmpty()) {
            return false;
        }

        File file = dialogueFile(dialogueId);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("npc.fancy-npc", fancyNpc);
        save(yaml, file);
        reload();
        return true;
    }

    public boolean appendLine(String dialogueId, String nodeId, String line) {
        File file = dialogueFile(dialogueId);
        if (!file.exists()) {
            return false;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection nodeSection = nodeSection(yaml, nodeId);
        if (nodeSection == null) {
            nodeSection = yaml.createSection("nodes." + nodeId);
            nodeSection.set("speaker", "npc");
        }

        List<String> lines = nodeSection.getStringList("lines");
        lines.add(line);
        nodeSection.set("lines", lines);
        save(yaml, file);
        reload();
        return true;
    }

    public void createDialogue(String dialogueId, String npcName, String nameColor) {
        File file = dialogueFile(dialogueId);
        if (file.exists()) {
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("id", dialogueId);
        yaml.set("npc.fancy-npc", dialogueId);
        yaml.set("npc.name", npcName);
        yaml.set("npc.name-color", nameColor);
        yaml.set("npc.bracket-color", "gray");
        yaml.set("start", "1");
        yaml.set("nodes.1.speaker", "npc");
        yaml.set("nodes.1.lines", List.of("New dialogue line."));
        yaml.set("nodes.1.end", true);
        save(yaml, file);
        reload();
    }

    private void sendNode(Player player, Dialogue dialogue, Dialogue.DialogueNode node) {
        for (String line : node.lines()) {
            player.sendMessage(prefix(dialogue, node.speaker(), player).append(miniMessage.deserialize(line)));
        }

        choicePrompt(node).ifPresent(prompt -> player.sendMessage(miniMessage.deserialize(prompt)));
    }

    private boolean startDialogue(Player player, Dialogue dialogue) {
        Dialogue.DialogueNode startNode = dialogue.startNode();
        if (startNode == null) {
            player.sendMessage(Component.text("Dialogue " + dialogue.id() + " has no start node."));
            return false;
        }

        sendNode(player, dialogue, startNode);
        if (startNode.end()) {
            plugin.sessions().clear(player.getUniqueId());
        } else {
            plugin.sessions().setNode(player.getUniqueId(), dialogue.id(), startNode.id());
        }
        return true;
    }

    private Component prefix(Dialogue dialogue, String speaker, Player player) {
        if ("player".equalsIgnoreCase(speaker)) {
            return miniMessage.deserialize(formatPlayerPrefix(player));
        }

        Dialogue.NpcProfile npc = dialogue.npc();
        return miniMessage.deserialize(formatNpcPrefix(npc));
    }

    private String formatNpcPrefix(Dialogue.NpcProfile npc) {
        String format = plugin.getConfig().getString(
            "messages.npc-format",
            "<gray><</gray><name_color><npc_name></name_color><gray>></gray> "
        );

        return format
            .replace("<name_color>", "<" + safeColor(npc.nameColor(), "green") + ">")
            .replace("</name_color>", "</" + safeColor(npc.nameColor(), "green") + ">")
            .replace("<bracket_color>", "<" + safeColor(npc.bracketColor(), "gray") + ">")
            .replace("</bracket_color>", "</" + safeColor(npc.bracketColor(), "gray") + ">")
            .replace("<npc_name>", miniMessage.escapeTags(npc.name()));
    }

    private String formatPlayerPrefix(Player player) {
        String format = plugin.getConfig().getString(
            "messages.player-format",
            "<gray><</gray><aqua><player_name></aqua><gray>></gray> "
        );

        return format.replace("<player_name>", miniMessage.escapeTags(player.getName()));
    }

    private Optional<String> choicePrompt(Dialogue.DialogueNode node) {
        boolean hasLeft = hasBranch(node.left()) && hasChoiceText(node.leftText());
        boolean hasRight = hasBranch(node.right()) && hasChoiceText(node.rightText());
        if (!hasLeft && !hasRight) {
            return Optional.empty();
        }

        if (hasLeft && hasRight) {
            String format = plugin.getConfig().getString(
                "messages.choice-format",
                "<gray><</gray><red>Left</red><gray>></gray> <gray><left_choice></gray> <white>|</white> "
                    + "<gray><</gray><red>Right</red><gray>></gray> <gray><right_choice></gray>"
            );

            return Optional.of(format
                .replace("<left_choice>", miniMessage.escapeTags(choiceText(node.leftText())))
                .replace("<right_choice>", miniMessage.escapeTags(choiceText(node.rightText()))));
        }

        String format = plugin.getConfig().getString(
            hasLeft ? "messages.left-choice-format" : "messages.right-choice-format",
            hasLeft
                ? "<gray><</gray><red>Left</red><gray>></gray> <gray><left_choice></gray>"
                : "<gray><</gray><red>Right</red><gray>></gray> <gray><right_choice></gray>"
        );

        return Optional.of(format
            .replace("<left_choice>", miniMessage.escapeTags(choiceText(node.leftText())))
            .replace("<right_choice>", miniMessage.escapeTags(choiceText(node.rightText()))));
    }

    private boolean hasBranch(String nodeId) {
        return nodeId != null && !nodeId.isBlank();
    }

    private String choiceText(String text) {
        return text == null ? "" : text;
    }

    private boolean hasChoiceText(String text) {
        return text != null && !text.isBlank();
    }

    private String safeColor(String color, String fallback) {
        if (color == null || !color.matches("[a-zA-Z_]+")) {
            return fallback;
        }

        return color.toLowerCase(Locale.ROOT);
    }

    private Dialogue loadDialogue(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        String id = yaml.getString("id", stripExtension(file.getName()));
        Dialogue.NpcProfile npc = new Dialogue.NpcProfile(
            yaml.getString("npc.fancy-npc", id),
            yaml.getString("npc.name", id),
            yaml.getString("npc.name-color", "green"),
            yaml.getString("npc.bracket-color", "gray")
        );
        String start = yaml.getString("start", "1");

        ConfigurationSection nodesSection = yaml.getConfigurationSection("nodes");
        if (nodesSection == null) {
            throw new IllegalArgumentException("missing nodes section");
        }

        Map<String, Dialogue.DialogueNode> nodes = new LinkedHashMap<>();
        collectNodes(file, nodes, "", nodesSection);

        return new Dialogue(id, npc, start, nodes);
    }

    private File dialogueFile(String dialogueId) {
        return new File(plugin.getDataFolder(), "dialogues/" + dialogueId + ".yml");
    }

    private ConfigurationSection nodeSection(YamlConfiguration yaml, String nodeId) {
        ConfigurationSection nodesSection = yaml.getConfigurationSection("nodes");
        if (nodesSection == null) {
            nodesSection = yaml.createSection("nodes");
        }

        Object rawNode = nodesSection.getValues(false).get(nodeId);
        if (rawNode instanceof ConfigurationSection section) {
            return section;
        }

        return null;
    }

    private void collectNodes(File file, Map<String, Dialogue.DialogueNode> nodes, String prefix, ConfigurationSection parent) {
        for (Map.Entry<String, Object> entry : parent.getValues(false).entrySet()) {
            String key = entry.getKey();
            String nodeId = prefix.isBlank() ? key : prefix + "." + key;
            if (!(entry.getValue() instanceof ConfigurationSection nodeSection)) {
                if (isNodeField(key)) {
                    continue;
                }
                plugin.getLogger().warning("Skipping node " + nodeId + " in " + file.getName() + " because it is not a section.");
                continue;
            }

            if (isDialogueNode(nodeSection)) {
                nodes.put(nodeId, new Dialogue.DialogueNode(
                    nodeId,
                    nodeSection.getString("speaker", "npc"),
                    nodeSection.getStringList("lines"),
                    nodeSection.getString("left"),
                    nodeSection.getString("right"),
                    nodeSection.getString("left-text", ""),
                    nodeSection.getString("right-text", ""),
                    nodeSection.getBoolean("end", false)
                ));
            }

            for (Map.Entry<String, Object> child : nodeSection.getValues(false).entrySet()) {
                if (child.getValue() instanceof ConfigurationSection childSection && !isNodeField(child.getKey())) {
                    collectNodes(file, nodes, nodeId, nodeSection);
                    break;
                }
            }
        }
    }

    private boolean isDialogueNode(ConfigurationSection section) {
        return section.isList("lines")
            || section.isString("speaker")
            || section.isString("left")
            || section.isString("right")
            || section.isString("left-text")
            || section.isString("right-text")
            || section.isBoolean("end");
    }

    private boolean isNodeField(String key) {
        return switch (key) {
            case "speaker", "lines", "left", "right", "left-text", "right-text", "end" -> true;
            default -> false;
        };
    }

    private void save(YamlConfiguration yaml, File file) {
        try {
            File parent = file.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IOException("Could not create " + parent);
            }
            yaml.save(file);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save " + file.getName(), exception);
        }
    }

    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot == -1 ? name : name.substring(0, dot);
    }
}
