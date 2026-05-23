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
      - "<gray>Right-click for work. Left-click to leave.</gray>"
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

## Node Fields

- `speaker`: `npc` or `player`
- `lines`: messages sent when the node is reached
- `left`: node id reached by a left-click
- `right`: node id reached by a right-click
- `end`: clears the player's active dialogue session after the node is sent

If a clicked branch is missing or blank, the conversation ends silently.

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
