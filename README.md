# Simple Dialogue

Simple Dialogue is now shaped as a Paper plugin instead of only a datapack.

The datapack prototype still exists under `data/`, but the plugin is the path forward because it can store dialogue trees in YAML, expose server commands, remember per-player dialogue sessions, and register a native FancyNPC action.

## Why Plugin Instead Of Datapack?

Datapack value:

- No Java build or plugin install.
- Good for tiny, fixed dialogue trees.
- Easy to call from FancyNPC console/player commands.

Datapack cost:

- Arbitrary text input is painful.
- Runtime editing through chat is not natural.
- Data structures become scoreboards, tags, and storage paths.
- Styling repeated NPC names before every line is possible, but clunky.

Plugin value:

- Dialogue files live as readable `.yml`.
- Commands can create and edit dialogues while the server runs.
- Chat capture can be added later for a comfortable authoring mode.
- FancyNPC can call a custom action directly, similar to how FancyDialogs registers `open_dialog`.
- NPC name and prefix styling can be stored once and reused automatically.

Plugin cost:

- Requires compiling and installing a `.jar`.
- Must track Paper and FancyNPC API versions.

## FancyNPC Setup

When FancyNPC is installed, Simple Dialogue registers this custom action:

```text
simple_dialogue
```

Add it to both click types so the plugin can tell left from right:

```mcfunction
/npc action guide LEFT_CLICK add simple_dialogue guide
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

`guide` at the end is the dialogue id in `plugins/SimpleDialogue/dialogues/guide.yml`.

Command fallback, if you do not want to use the custom action:

```mcfunction
/npc action guide LEFT_CLICK add console_command sd click guide left {player}
/npc action guide RIGHT_CLICK add console_command sd click guide right {player}
```

## Dialogue YAML

Example:

```yaml
id: guide
npc:
  fancy-npc: guide
  name: Guide
  name-color: green
  bracket-color: gray
start: "1"
nodes:
  "1":
    speaker: npc
    lines:
      - "Road's closed until morning."
      - "<gray>Right-click to ask why. Left-click to say goodbye.</gray>"
    right: "1.1"
    left: "1.2"
```

NPC lines are automatically prefixed as:

```text
<Guide> Road's closed until morning.
```

with gray brackets and the configured NPC name color.

## Runtime Commands

```mcfunction
/sd new <dialogue> <npc-name> [name-color]
/sd npcname <dialogue> <name> <name-color> [bracket-color]
/sd link <dialogue> <fancy-npc>
/sd line add <dialogue> <node> <text...>
/sd click <dialogue> <left|right> [player]
/sd reload
```

The current command editor is intentionally small. The next useful layer would be an authoring session:

```text
/sd edit guide
/sd node 1
/sd say Road's closed until morning.
/sd choice right 1.1
/sd choice left 1.2
```

After that, a chat-capture mode could let you type long dialogue lines normally in chat and save them to the selected node.
