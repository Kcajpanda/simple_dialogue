package com.lazarbow.simpledialogue.command;

import com.lazarbow.simpledialogue.ClickSide;
import com.lazarbow.simpledialogue.SimpleDialoguePlugin;
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
            case "new" -> create(sender, args);
            default -> help(sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "click", "npcname", "link", "line", "new");
        }
        if (args.length == 2 && List.of("click", "npcname", "link", "line").contains(args[0].toLowerCase())) {
            return new ArrayList<>(plugin.dialogueManager().dialogueIds());
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("click")) {
            return List.of("left", "right");
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

    private void help(CommandSender sender) {
        sender.sendMessage("/sd click <dialogue> <left|right> [player]");
        sender.sendMessage("/sd new <dialogue> <npc-name> [name-color]");
        sender.sendMessage("/sd npcname <dialogue> <name> <name-color> [bracket-color]");
        sender.sendMessage("/sd link <dialogue> <fancy-npc>");
        sender.sendMessage("/sd line add <dialogue> <node> <text...>");
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
