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
start: "1"
nodes:
  "1":
    speaker: npc
    lines:
      - "Road's closed until morning."
    left-text: "Say goodbye"
    right-text: "Ask why"
    left: "1.2"
    right: "1.1"
  "1.1":
    speaker: npc
    lines:
      - "Bandits near the bridge."
    end: true
  "1.2":
    speaker: npc
    lines:
      - "Safe travels."
    end: true
```

## Node Fields

- `speaker`: `npc` or `player`
- `lines`: messages sent when the node is reached
- `left`: node id reached by a left-click
- `right`: node id reached by a right-click
- `left-text`: choice text shown for the left-click option
- `right-text`: choice text shown for the right-click option
- `end`: clears the player's active dialogue session after the node is sent

Run `/sd validate` after editing YAML.
