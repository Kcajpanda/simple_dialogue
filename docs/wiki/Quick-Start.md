# Quick Start

Create or pick a FancyNpcs NPC named `guide`, then add the Simple Dialogue action to both click triggers:

```text
/npc action guide LEFT_CLICK add simple_dialogue guide
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

The final `guide` is the dialogue id from `plugins/SimpleDialogue/dialogues/guide.yml`.

If the custom action is not available, use console-command actions:

```text
/npc action guide LEFT_CLICK add console_command sd click guide left {player}
/npc action guide RIGHT_CLICK add console_command sd click guide right {player}
```

Run:

```text
/sd validate guide
```

Then click the NPC in-game. Right-click and left-click follow different branches when the current node has those branch targets.
