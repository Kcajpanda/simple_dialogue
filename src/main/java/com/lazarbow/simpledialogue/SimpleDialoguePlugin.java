package com.lazarbow.simpledialogue;

import com.lazarbow.simpledialogue.command.SimpleDialogueCommand;
import com.lazarbow.simpledialogue.dialogue.DialogueManager;
import com.lazarbow.simpledialogue.fancynpcs.FancyNpcIntegration;
import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Paper entrypoint for Simple Dialogue.
 *
 * <p>The plugin owns dialogue loading, player conversation sessions, command registration,
 * and the optional FancyNPC action bridge.</p>
 */
public final class SimpleDialoguePlugin extends JavaPlugin {
    private DialogueManager dialogueManager;
    private PlayerDialogueSessions sessions;
    private FancyNpcIntegration fancyNpcIntegration;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveBundledDialogue("guide.yml");

        sessions = new PlayerDialogueSessions();
        dialogueManager = new DialogueManager(this);
        dialogueManager.reload();

        SimpleDialogueCommand command = new SimpleDialogueCommand(this);
        getCommand("simpledialogue").setExecutor(command);
        getCommand("simpledialogue").setTabCompleter(command);

        Plugin fancyNpcs = getServer().getPluginManager().getPlugin("FancyNpcs");
        if (fancyNpcs != null && fancyNpcs.isEnabled()) {
            fancyNpcIntegration = new FancyNpcIntegration(this);
            fancyNpcIntegration.register();
        } else {
            getLogger().info("FancyNpcs was not found. Use /sd click <dialogue> <left|right> <player> as a command fallback.");
        }
    }

    @Override
    public void onDisable() {
        if (fancyNpcIntegration != null) {
            fancyNpcIntegration.unregister();
        }
    }

    public DialogueManager dialogueManager() {
        return dialogueManager;
    }

    public PlayerDialogueSessions sessions() {
        return sessions;
    }

    public void reloadPlugin() {
        reloadConfig();
        dialogueManager.reload();
        sessions.clear();
    }

    private void saveBundledDialogue(String fileName) {
        File target = new File(getDataFolder(), "dialogues/" + fileName);
        if (!target.exists()) {
            saveResource("dialogues/" + fileName, false);
        }
    }
}
