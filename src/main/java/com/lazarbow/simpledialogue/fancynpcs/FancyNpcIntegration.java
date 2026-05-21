package com.lazarbow.simpledialogue.fancynpcs;

import com.lazarbow.simpledialogue.SimpleDialoguePlugin;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;

public final class FancyNpcIntegration {
    private final SimpleDialoguePlugin plugin;
    private FancyNpcSimpleDialogueAction action;

    public FancyNpcIntegration(SimpleDialoguePlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        action = new FancyNpcSimpleDialogueAction(plugin);
        FancyNpcsPlugin.get().getActionManager().registerAction(action);
        plugin.getLogger().info("Registered FancyNPC action: simple_dialogue");
    }

    public void unregister() {
        if (action != null) {
            FancyNpcsPlugin.get().getActionManager().unregisterAction(action);
        }
    }
}
