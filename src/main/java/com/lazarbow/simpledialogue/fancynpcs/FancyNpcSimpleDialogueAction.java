package com.lazarbow.simpledialogue.fancynpcs;

import com.lazarbow.simpledialogue.ClickSide;
import com.lazarbow.simpledialogue.SimpleDialoguePlugin;
import de.oliver.fancynpcs.api.actions.ActionTrigger;
import de.oliver.fancynpcs.api.actions.NpcAction;
import de.oliver.fancynpcs.api.actions.executor.ActionExecutionContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FancyNpcSimpleDialogueAction extends NpcAction {
    private final SimpleDialoguePlugin plugin;

    public FancyNpcSimpleDialogueAction(SimpleDialoguePlugin plugin) {
        super("simple_dialogue", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull ActionExecutionContext context, @Nullable String value) {
        Player player = context.getPlayer();
        if (player == null || value == null || value.isBlank()) {
            return;
        }

        ClickSide side = switch (context.getTrigger()) {
            case LEFT_CLICK -> ClickSide.LEFT;
            case RIGHT_CLICK -> ClickSide.RIGHT;
            case ANY_CLICK, CUSTOM -> ClickSide.RIGHT;
        };

        plugin.dialogueManager().runClick(player, value, side);
    }
}
