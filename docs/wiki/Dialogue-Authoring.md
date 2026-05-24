# Dialogue Authoring

Dialogue files live in:

```text
plugins/SimpleDialogue/dialogues/
```

Each file is one YAML dialogue:

```yaml
id: guide
npc:
  fancy-npc: guide
  name: Guide
  name-color: green
  bracket-color: gray
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
  "Do":
    speaker: npc
    lines:
      - "You can explore survival, minigames, or creative worlds from here."
    left-text: "Leave"
    right-text: "Ask more questions"
    left: "Leave"
    right: "Help"
  "Leave":
    speaker: npc
    lines:
      - "I'll be right here if you have any more questions."
    end: true
```

## Intro Nodes

Use `next` when a node should auto-advance into another node without waiting for another click.

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
    left: "Leave"
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

## Node Commands

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

Available placeholders are `<player>`, `<uuid>`, `<dialogue>`, and `<node>`.

Run `/sd validate` after editing YAML.

## Server Command Cleanup

```text
/sd start guide Intro
/sd node info guide Help
/sd line remove guide Help 1
/sd branch guide Help left clear
/sd node next guide Intro clear
/sd command clear guide Do console
/sd node remove guide Do
```

Run `/sd validate <dialogue>` after cleanup so missing references are caught before players test the NPC.
