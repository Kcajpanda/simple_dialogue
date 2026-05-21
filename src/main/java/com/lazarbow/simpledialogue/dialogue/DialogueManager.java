package com.lazarbow.simpledialogue.dialogue;

import com.lazarbow.simpledialogue.ClickSide;
import com.lazarbow.simpledialogue.PlayerDialogueSessions.Session;
import com.lazarbow.simpledialogue.SimpleDialoguePlugin;
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
        Session session = plugin.sessions().getOrStart(player.getUniqueId(), dialogue.id(), dialogue.start());
        Dialogue.DialogueNode current = dialogue.nodes().getOrDefault(session.node(), dialogue.startNode());
        if (current == null) {
            player.sendMessage(Component.text("Dialogue " + dialogue.id() + " has no start node."));
            plugin.sessions().clear(player.getUniqueId());
            return false;
        }

        sendNode(player, dialogue, current);

        if (current.end()) {
            plugin.sessions().clear(player.getUniqueId());
            return true;
        }

        String next = side == ClickSide.LEFT ? current.left() : current.right();
        if (next == null || next.isBlank()) {
            plugin.sessions().clear(player.getUniqueId());
            return true;
        }

        plugin.sessions().setNode(player.getUniqueId(), dialogue.id(), next);
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
        String path = "nodes." + nodeId + ".lines";
        List<String> lines = yaml.getStringList(path);
        lines.add(line);
        yaml.set(path, lines);
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
    }

    private Component prefix(Dialogue dialogue, String speaker, Player player) {
        if ("player".equalsIgnoreCase(speaker)) {
            return miniMessage.deserialize(
                "<gray><</gray><aqua>" + miniMessage.escapeTags(player.getName()) + "</aqua><gray>></gray> "
            );
        }

        Dialogue.NpcProfile npc = dialogue.npc();
        return miniMessage.deserialize(
            "<" + npc.bracketColor() + "><</" + npc.bracketColor() + ">"
                + "<" + npc.nameColor() + ">" + miniMessage.escapeTags(npc.name()) + "</" + npc.nameColor() + ">"
                + "<" + npc.bracketColor() + ">></" + npc.bracketColor() + "> "
        );
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
        for (String nodeId : nodesSection.getKeys(false)) {
            String path = "nodes." + nodeId + ".";
            nodes.put(nodeId, new Dialogue.DialogueNode(
                nodeId,
                yaml.getString(path + "speaker", "npc"),
                yaml.getStringList(path + "lines"),
                yaml.getString(path + "left"),
                yaml.getString(path + "right"),
                yaml.getBoolean(path + "end", false)
            ));
        }

        return new Dialogue(id, npc, start, nodes);
    }

    private File dialogueFile(String dialogueId) {
        return new File(plugin.getDataFolder(), "dialogues/" + dialogueId + ".yml");
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
