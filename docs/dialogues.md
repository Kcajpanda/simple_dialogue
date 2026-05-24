# Dialogue Authoring

Simple Dialogue reads one YAML file per dialogue from:

```text
plugins/SimpleDialogue/dialogues/
```

The file name usually matches the dialogue id, such as `guide.yml`.

## Recommended Workflow

Use commands for setup and YAML for the actual tree.

Commands are good for:

- Creating a starter file
- Linking a dialogue id to a FancyNpcs NPC name
- Adding quick test lines while standing in-game
- Creating nodes
- Wiring left/right branch targets
- Marking ending nodes
- Setting one-way `next` transitions for intros or automatic line exchanges
- Adding console/player commands that run when a node is reached
- Reloading after edits

YAML is better for:

- Branching
- Renaming nodes
- Ending conversations
- Seeing the whole tree at once
- Reviewing changes in Git

## Minimal Dialogue

```yaml
id: blacksmith
npc:
  fancy-npc: blacksmith
  name: Blacksmith
  name-color: gold
  bracket-color: gray
start: "1"
nodes:
  "1":
    speaker: npc
    lines:
      - "Need something forged?"
    end: true
```

## Branching Dialogue

Each node can point left and right clicks to another node id.

```yaml
nodes:
  "1":
    speaker: npc
    lines:
      - "Need something forged?"
    left-text: "Leave"
    right-text: "Ask for work"
    right: "1.1"
    left: "1.2"
  "1.1":
    speaker: npc
    lines:
      - "Bring me iron and coal."
    end: true
  "1.2":
    speaker: npc
    lines:
      - "Then keep your blade sharp."
    end: true
```

The same tree can be drafted with commands:

```text
/sd node add blacksmith 1 npc Need something forged?
/sd branch blacksmith 1 left 1.2 Leave
/sd branch blacksmith 1 right 1.1 Ask for work
/sd node add blacksmith 1.1 npc Bring me iron and coal.
/sd node end blacksmith 1.1 true
/sd node add blacksmith 1.2 npc Then keep your blade sharp.
/sd node end blacksmith 1.2 true
```

## Node Fields

- `speaker`: `npc` or `player`
- `lines`: messages sent when the node is reached
- `left`: node id reached by a left-click
- `right`: node id reached by a right-click
- `left-text`: choice text shown for the left-click option
- `right-text`: choice text shown for the right-click option
- `next`: node id reached automatically after this node is sent
- `commands`: console commands run when the node is reached
- `player-commands`: commands run as the clicking player when the node is reached
- `end`: clears the player's active dialogue session after the node is sent

If a clicked branch is missing or blank, the conversation ends silently.

## Intro Nodes

Use `next` when a line should play once at the start, then immediately enter a normal choice node.

```yaml
start: "Intro"
nodes:
  "Intro":
    speaker: npc
    lines:
      - "Hello, I'm the guide."
    next: "Help"
  "Help":
    speaker: npc
    lines:
      - "How can I help?"
    left-text: "Leave"
    right-text: "Ask what you can do here"
    left: "Leave"
    right: "Do"
```

`next` does not wait for another click. Avoid putting `left` or `right` branches on the same node; validation warns because those branches are skipped by the automatic transition.

## Node Commands

Use `commands` for console actions such as rewards, teleports, titles, sounds, scoreboard tags, and integration commands from other plugins.

```yaml
"Reward":
  speaker: npc
  lines:
    - "Take this before you go."
  commands:
    - "give <player> minecraft:bread 4"
    - "playsound minecraft:entity.experience_orb.pickup player <player>"
  end: true
```

Use `player-commands` only when the command should run with the player's permissions and context.

```yaml
"Thanks":
  speaker: player
  lines:
    - "Thanks!"
  player-commands:
    - "me thanks the guide"
  next: "Help"
```

Command placeholders:

- `<player>`: the player's current name
- `<uuid>`: the player's UUID
- `<dialogue>`: the dialogue id
- `<node>`: the node id

## Choice Prompts

When a node has both a branch and matching choice text, Simple Dialogue sends a separate prompt line after the dialogue text.

```yaml
"1":
  speaker: npc
  lines:
    - "Need something forged?"
  left: "1.1"
  left-text: "Leave"
  right: "1.2"
  right-text: "Ask for work"
```

With the default config, that appears like:

```text
<Left> Leave | <Right> Ask for work
```

Choice prompt colors and separators can be changed in `config.yml`.

Choice text is treated as plain text. Put colors, brackets, and separators in the config formats instead of inside `left-text` or `right-text`.

## Nested Node Style

Simple Dialogue supports flat node ids like `"1.1"` and nested YAML sections. This means these shapes are both valid:

```yaml
nodes:
  "1.1":
    lines:
      - "Flat node id."
```

```yaml
nodes:
  "1":
    "1":
      lines:
        - "Nested node id."
```

For hand editing, flat quoted ids are usually easier to read.

## MiniMessage

Dialogue lines support MiniMessage:

```yaml
lines:
  - "<yellow>Careful.</yellow> <gray>The road is watched.</gray>"
```

NPC and player name prefixes are controlled in `config.yml`.

## FancyNpcs Wiring

For a dialogue id named `blacksmith` and a FancyNpcs NPC named `blacksmith`, add:

```text
/npc action blacksmith LEFT_CLICK add simple_dialogue blacksmith
/npc action blacksmith RIGHT_CLICK add simple_dialogue blacksmith
```

The plugin can tell left from right only when both trigger types are wired.
