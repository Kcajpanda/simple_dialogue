package io.github.kcajpanda.simpledialogue.command;

import io.github.kcajpanda.simpledialogue.ClickSide;
import io.github.kcajpanda.simpledialogue.SimpleDialoguePlugin;
import io.github.kcajpanda.simpledialogue.dialogue.Dialogue;
import io.github.kcajpanda.simpledialogue.dialogue.DialogueManager.CommandMode;
import io.github.kcajpanda.simpledialogue.dialogue.DialogueManager.ValidationIssue;
import io.github.kcajpanda.simpledialogue.dialogue.DialogueManager.Severity;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * Handles the /simpledialogue and /sd command surface.
 *
 * <p>The command set is deliberately small for now: enough to create a dialogue,
 * edit display names, add lines, reload YAML, and support command-based NPC actions.</p>
 */
public final class SimpleDialogueCommand implements CommandExecutor, TabCompleter {
    private final SimpleDialoguePlugin plugin;

    public SimpleDialogueCommand(SimpleDialoguePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            help(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                if (!requireAdmin(sender)) {
                    return true;
                }
                plugin.reloadPlugin();
                sender.sendMessage("SimpleDialogue reloaded.");
            }
            case "click" -> click(sender, args);
            case "npcname" -> npcName(sender, args);
            case "link" -> link(sender, args);
            case "line" -> line(sender, args);
            case "node" -> node(sender, args);
            case "command" -> nodeCommand(sender, args);
            case "branch" -> branch(sender, args);
            case "new" -> create(sender, args);
            case "reset" -> reset(sender, args);
            case "info" -> info(sender, args);
            case "validate" -> validate(sender, args);
            default -> help(sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "click", "npcname", "link", "line", "node", "command", "branch", "new", "reset", "info", "validate");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("line")) {
            return List.of("add");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("node")) {
            return List.of("add", "end", "next");
        }
        if (
            (args.length == 2 && List.of("click", "npcname", "link", "branch", "info", "validate").contains(args[0].toLowerCase()))
                || (args.length == 3 && List.of("line", "node").contains(args[0].toLowerCase()))
        ) {
            return new ArrayList<>(plugin.dialogueManager().dialogueIds());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("command")) {
            return List.of("add", "clear");
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("command")) {
            return new ArrayList<>(plugin.dialogueManager().dialogueIds());
        }
        if (args.length == 5 && args[0].equalsIgnoreCase("command")) {
            return List.of("console", "player");
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("click")) {
            return List.of("left", "right");
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("branch")) {
            return List.of("left", "right");
        }
        if (args.length == 5 && args[0].equalsIgnoreCase("node") && args[1].equalsIgnoreCase("add")) {
            return List.of("npc", "player");
        }
        return List.of();
    }

    private void click(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /sd click <dialogue> <left|right> [player]");
            return;
        }

        ClickSide side = ClickSide.parse(args[2]);
        if (side == null) {
            sender.sendMessage("Click side must be left or right.");
            return;
        }

        Player player;
        if (args.length >= 4) {
            player = Bukkit.getPlayerExact(args[3]);
        } else if (sender instanceof Player senderPlayer) {
            player = senderPlayer;
        } else {
            sender.sendMessage("Console must provide a player.");
            return;
        }

        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        plugin.dialogueManager().runClick(player, args[1], side);
    }

    private void npcName(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 4) {
            sender.sendMessage("Usage: /sd npcname <dialogue> <name> <name-color> [bracket-color]");
            return;
        }

        String bracketColor = args.length >= 5 ? args[4] : "gray";
        if (plugin.dialogueManager().setNpcProfile(args[1], args[2], args[3], bracketColor)) {
            sender.sendMessage("Updated NPC name styling for " + args[1] + ".");
        } else {
            sender.sendMessage("Unknown dialogue: " + args[1]);
        }
    }

    private void link(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 3) {
            sender.sendMessage("Usage: /sd link <dialogue> <fancy-npc>");
            return;
        }

        if (!plugin.dialogueManager().setFancyNpc(args[1], args[2])) {
            sender.sendMessage("Unknown dialogue: " + args[1]);
            return;
        }

        sender.sendMessage("Linked " + args[1] + " to FancyNPC " + args[2] + ".");
        sender.sendMessage("Add these FancyNPC actions:");
        sender.sendMessage("/npc action " + args[2] + " LEFT_CLICK add simple_dialogue " + args[1]);
        sender.sendMessage("/npc action " + args[2] + " RIGHT_CLICK add simple_dialogue " + args[1]);
    }

    private void line(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 5 || !args[1].equalsIgnoreCase("add")) {
            sender.sendMessage("Usage: /sd line add <dialogue> <node> <text...>");
            return;
        }

        String line = String.join(" ", List.of(args).subList(4, args.length));
        if (plugin.dialogueManager().appendLine(args[2], args[3], line)) {
            sender.sendMessage("Added line to " + args[2] + " node " + args[3] + ".");
        } else {
            sender.sendMessage("Unknown dialogue: " + args[2]);
        }
    }

    private void node(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 4) {
            sender.sendMessage("Usage: /sd node add <dialogue> <node> [npc|player] [text...]");
            sender.sendMessage("Usage: /sd node end <dialogue> <node> <true|false>");
            sender.sendMessage("Usage: /sd node next <dialogue> <node> <target|clear>");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "add" -> nodeAdd(sender, args);
            case "end" -> nodeEnd(sender, args);
            case "next" -> nodeNext(sender, args);
            default -> {
                sender.sendMessage("Usage: /sd node add <dialogue> <node> [npc|player] [text...]");
                sender.sendMessage("Usage: /sd node end <dialogue> <node> <true|false>");
                sender.sendMessage("Usage: /sd node next <dialogue> <node> <target|clear>");
            }
        }
    }

    private void nodeAdd(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("Usage: /sd node add <dialogue> <node> [npc|player] [text...]");
            return;
        }

        String speaker = "npc";
        int textStart = 4;
        if (args.length >= 5 && List.of("npc", "player").contains(args[4].toLowerCase())) {
            speaker = args[4].toLowerCase();
            textStart = 5;
        }

        List<String> lines = args.length > textStart
            ? List.of(String.join(" ", List.of(args).subList(textStart, args.length)))
            : List.of();

        if (plugin.dialogueManager().upsertNode(args[2], args[3], speaker, lines)) {
            sender.sendMessage("Saved node " + args[3] + " in " + args[2] + ".");
        } else {
            sender.sendMessage("Unknown dialogue: " + args[2]);
        }
    }

    private void nodeEnd(CommandSender sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage("Usage: /sd node end <dialogue> <node> <true|false>");
            return;
        }

        if (!List.of("true", "false").contains(args[4].toLowerCase())) {
            sender.sendMessage("End value must be true or false.");
            return;
        }

        if (plugin.dialogueManager().setNodeEnd(args[2], args[3], Boolean.parseBoolean(args[4]))) {
            sender.sendMessage("Set end=" + args[4].toLowerCase() + " for " + args[2] + " node " + args[3] + ".");
        } else {
            sender.sendMessage("Unknown dialogue: " + args[2]);
        }
    }

    private void nodeNext(CommandSender sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage("Usage: /sd node next <dialogue> <node> <target|clear>");
            return;
        }

        if (plugin.dialogueManager().setNodeNext(args[2], args[3], args[4])) {
            sender.sendMessage("Set next target for " + args[2] + " node " + args[3] + ".");
        } else {
            sender.sendMessage("Unknown dialogue: " + args[2]);
        }
    }

    private void branch(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 5) {
            sender.sendMessage("Usage: /sd branch <dialogue> <node> <left|right> <target|clear> [choice text...]");
            return;
        }

        ClickSide side = ClickSide.parse(args[3]);
        if (side == null) {
            sender.sendMessage("Branch side must be left or right.");
            return;
        }

        String choiceText = args.length >= 6 ? String.join(" ", List.of(args).subList(5, args.length)) : "";
        if (plugin.dialogueManager().setBranch(args[1], args[2], side, args[4], choiceText)) {
            sender.sendMessage("Set " + args[3].toLowerCase() + " branch on " + args[1] + " node " + args[2] + ".");
        } else {
            sender.sendMessage("Unknown dialogue: " + args[1]);
        }
    }

    private void nodeCommand(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 5) {
            sender.sendMessage("Usage: /sd command add <dialogue> <node> <console|player> <command...>");
            sender.sendMessage("Usage: /sd command clear <dialogue> <node> <console|player>");
            return;
        }

        CommandMode mode = commandMode(args[4]);
        if (mode == null) {
            sender.sendMessage("Command mode must be console or player.");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "add" -> {
                if (args.length < 6) {
                    sender.sendMessage("Usage: /sd command add <dialogue> <node> <console|player> <command...>");
                    return;
                }
                String commandText = String.join(" ", List.of(args).subList(5, args.length));
                if (plugin.dialogueManager().addNodeCommand(args[2], args[3], mode, commandText)) {
                    sender.sendMessage("Added " + args[4].toLowerCase() + " command to " + args[2] + " node " + args[3] + ".");
                } else {
                    sender.sendMessage("Unknown dialogue or node: " + args[2] + " " + args[3]);
                }
            }
            case "clear" -> {
                if (plugin.dialogueManager().clearNodeCommands(args[2], args[3], mode)) {
                    sender.sendMessage("Cleared " + args[4].toLowerCase() + " commands from " + args[2] + " node " + args[3] + ".");
                } else {
                    sender.sendMessage("Unknown dialogue or node: " + args[2] + " " + args[3]);
                }
            }
            default -> {
                sender.sendMessage("Usage: /sd command add <dialogue> <node> <console|player> <command...>");
                sender.sendMessage("Usage: /sd command clear <dialogue> <node> <console|player>");
            }
        }
    }

    private CommandMode commandMode(String mode) {
        return switch (mode.toLowerCase()) {
            case "console" -> CommandMode.CONSOLE;
            case "player" -> CommandMode.PLAYER;
            default -> null;
        };
    }

    private void create(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }
        if (args.length < 3) {
            sender.sendMessage("Usage: /sd new <dialogue> <npc-name> [name-color]");
            return;
        }

        String color = args.length >= 4 ? args[3] : "green";
        plugin.dialogueManager().createDialogue(args[1], args[2], color);
        sender.sendMessage("Created dialogue " + args[1] + ".");
    }

    private void reset(CommandSender sender, String[] args) {
        Player player;
        if (args.length >= 2) {
            if (!requireAdmin(sender)) {
                return;
            }
            player = Bukkit.getPlayerExact(args[1]);
        } else if (sender instanceof Player senderPlayer) {
            player = senderPlayer;
        } else {
            sender.sendMessage("Usage: /sd reset [player]");
            return;
        }

        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        plugin.sessions().clear(player.getUniqueId());
        sender.sendMessage("Reset dialogue session for " + player.getName() + ".");
    }

    private void info(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /sd info <dialogue>");
            return;
        }

        Dialogue dialogue = plugin.dialogueManager().find(args[1]).orElse(null);
        if (dialogue == null) {
            sender.sendMessage("Unknown dialogue: " + args[1]);
            return;
        }

        sender.sendMessage("Dialogue: " + dialogue.id());
        sender.sendMessage("Start: " + dialogue.start());
        sender.sendMessage("NPC: " + dialogue.npc().name() + " (" + dialogue.npc().fancyNpc() + ")");
        sender.sendMessage("Nodes: " + String.join(", ", dialogue.nodes().keySet()));
    }

    private void validate(CommandSender sender, String[] args) {
        if (!requireAdmin(sender)) {
            return;
        }

        List<ValidationIssue> issues = args.length >= 2
            ? plugin.dialogueManager().validate(args[1])
            : plugin.dialogueManager().validateAll();

        if (issues.isEmpty()) {
            sender.sendMessage("SimpleDialogue validation passed.");
            return;
        }

        long errors = issues.stream().filter(issue -> issue.severity() == Severity.ERROR).count();
        long warnings = issues.size() - errors;
        sender.sendMessage("SimpleDialogue validation found " + errors + " error(s) and " + warnings + " warning(s):");
        for (ValidationIssue issue : issues) {
            sender.sendMessage("[" + issue.severity() + "] " + issue.message());
        }
    }

    private void help(CommandSender sender) {
        sender.sendMessage("/sd click <dialogue> <left|right> [player]");
        sender.sendMessage("/sd new <dialogue> <npc-name> [name-color]");
        sender.sendMessage("/sd npcname <dialogue> <name> <name-color> [bracket-color]");
        sender.sendMessage("/sd link <dialogue> <fancy-npc>");
        sender.sendMessage("/sd line add <dialogue> <node> <text...>");
        sender.sendMessage("/sd node add <dialogue> <node> [npc|player] [text...]");
        sender.sendMessage("/sd node end <dialogue> <node> <true|false>");
        sender.sendMessage("/sd node next <dialogue> <node> <target|clear>");
        sender.sendMessage("/sd command add <dialogue> <node> <console|player> <command...>");
        sender.sendMessage("/sd command clear <dialogue> <node> <console|player>");
        sender.sendMessage("/sd branch <dialogue> <node> <left|right> <target|clear> [choice text...]");
        sender.sendMessage("/sd reset [player]");
        sender.sendMessage("/sd info <dialogue>");
        sender.sendMessage("/sd validate [dialogue]");
        sender.sendMessage("/sd reload");
    }

    private boolean requireAdmin(CommandSender sender) {
        if (!sender.hasPermission("simpledialogue.admin")) {
            sender.sendMessage("You do not have permission.");
            return false;
        }
        return true;
    }
}
