# Simple Dialogue

Simple Dialogue is a small Paper plugin for left/right NPC dialogue trees. It was built to pair with FancyNpcs: add one custom FancyNpcs action to an NPC, then players can left-click or right-click through a YAML-backed conversation.

Simple Dialogue `1.0.0` is the first stable release. It has been server-tested with Paper `26.1.2`, Java `25`, and FancyNpcs `2.10.0`.

## Features

- YAML dialogue files in `plugins/SimpleDialogue/dialogues/`
- Left-click and right-click branching
- Per-player in-memory conversation sessions
- MiniMessage support in dialogue lines
- RPG-style choice prompts with `left-text` and `right-text`
- Configurable NPC/player chat prefixes
- Custom FancyNpcs action: `simple_dialogue`
- Command fallback for servers that prefer console-command NPC actions
- In-game commands for creating dialogues, adding nodes, wiring branches, node commands, and endings
- `/sd validate` for checking dialogue tree mistakes

## Requirements

- Paper `26.1.2`, tested against API `26.1.2.build.63-stable`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

Spigot is not supported.

## Download

For testing, download the jar from the latest GitHub Release and place it in your server's `plugins/` directory.

You can also build from source:

```text
./gradlew clean build
```

On Windows:

```text
.\gradlew.bat clean build
```

The compiled plugin jar is written to:

```text
build/libs/simple-dialogue-1.0.0.jar
```

## Installation

1. Stop the server.
2. Install FancyNpcs `2.10.0` or newer.
3. Upload `simple-dialogue-1.0.0.jar` to `plugins/`.
4. Start the server.
5. Confirm `SimpleDialogue` appears in `/plugins`.
6. Confirm the sample file was created:

```text
plugins/SimpleDialogue/dialogues/guide.yml
```

On startup, Simple Dialogue logs `Registered FancyNPC action: simple_dialogue` when FancyNpcs is present and enabled.

## Quick Start

Create or pick a FancyNpcs NPC named `guide`, then add the Simple Dialogue action to both click triggers:

```text
/npc action guide LEFT_CLICK add simple_dialogue guide
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

The final `guide` is the dialogue id from `plugins/SimpleDialogue/dialogues/guide.yml`.

Join the server and right-click the NPC once. You should see the sample dialogue:

```text
<Guide> Hello, I'm the guide.
<Guide> How can I help?
<Left> Ask what this place is | <Right> Ask what you can do here
```

Right-click again to follow the right branch, or left-click to follow the left branch.

## Command Fallback

If the custom FancyNpcs action is not available on your setup, use console-command actions instead:

```text
/npc action guide LEFT_CLICK add console_command sd click guide left {player}
/npc action guide RIGHT_CLICK add console_command sd click guide right {player}
```

## Dialogue Files

Dialogue files live in:

```text
plugins/SimpleDialogue/dialogues/
```

Each file is a YAML document:

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
    left-text: "Ask what this place is"
    right-text: "Ask what you can do here"
    left: "What"
    right: "Do"
  "What":
    speaker: npc
    lines:
      - "This is an outside place, a dimension beyond space and time."
    left-text: "Leave"
    right-text: "Ask more questions"
    left: "Leave"
    right: "Help"
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

See [docs/dialogues.md](docs/dialogues.md) for authoring tips and file-format details.

## Commands

```text
/sd new <dialogue> <npc-name> [name-color]
/sd delete <dialogue> confirm
/sd start <dialogue> <node>
/sd npcname <dialogue> <name> <name-color> [bracket-color]
/sd link <dialogue> <fancy-npc>
/sd line add <dialogue> <node> <text...>
/sd line remove <dialogue> <node> <line-number|all>
/sd node add <dialogue> <node> [npc|player] [text...]
/sd node remove <dialogue> <node>
/sd node info <dialogue> <node>
/sd node end <dialogue> <node> <true|false>
/sd node next <dialogue> <node> <target|clear>
/sd command add <dialogue> <node> <console|player> <command...>
/sd command remove <dialogue> <node> <console|player> <command-number|all>
/sd command clear <dialogue> <node> <console|player>
/sd branch <dialogue> <node> <left|right> <target|clear> [choice text...]
/sd click <dialogue> <left|right> [player]
/sd reset [player]
/sd info <dialogue>
/sd validate [dialogue]
/sd reload
```

Admin/editing commands require `simpledialogue.admin`, which defaults to server operators.

## Configuration

`plugins/SimpleDialogue/config.yml` controls the chat prefixes prepended to dialogue lines:

```yaml
messages:
  npc-format: "<bracket_color><</bracket_color><name_color><npc_name></name_color><bracket_color>></bracket_color> "
  player-format: "<gray><</gray><aqua><player_name></aqua><gray>></gray> "
  choice-format: "<gray><</gray><red>Left</red><gray>></gray> <gray><left_choice></gray> <white>|</white> <gray><</gray><red>Right</red><gray>></gray> <gray><right_choice></gray>"
  left-choice-format: "<gray><</gray><red>Left</red><gray>></gray> <gray><left_choice></gray>"
  right-choice-format: "<gray><</gray><red>Right</red><gray>></gray> <gray><right_choice></gray>"
```

Available placeholders:

- `<npc_name>`
- `<player_name>`
- `<name_color>` and `</name_color>`
- `<bracket_color>` and `</bracket_color>`
- `<left_choice>`
- `<right_choice>`

Dialogue lines themselves also support MiniMessage formatting.

## Building Dialogue Trees

For real dialogue trees, YAML is still the easiest source of truth because you can see the whole tree at once. The server commands can now create and wire a full tree when you want to draft in-game or avoid file access.

Recommended workflow:

1. Use `/sd new <id> <npc-name>` to create the starter file.
2. Use `/sd link <id> <fancy-npc>` to print the FancyNpcs action commands.
3. Set the start node with `/sd start <id> <node>`.
4. Add nodes with `/sd node add <id> <node> [npc|player] [text...]`.
5. Wire choices with `/sd branch <id> <node> <left|right> <target> [choice text...]`.
6. Mark endings with `/sd node end <id> <node> true`.
7. Inspect mistakes with `/sd node info <id> <node>`.
8. Remove bad lines, nodes, commands, or branches with the remove/clear commands.
9. Run `/sd validate`.
10. Test the NPC in-game.

Example command-built tree:

```text
/sd new blacksmith Blacksmith gold
/sd node add blacksmith 1 npc Need something forged?
/sd branch blacksmith 1 left 1.1 Leave
/sd branch blacksmith 1 right 1.2 Ask for work
/sd node add blacksmith 1.1 npc Then keep your blade sharp.
/sd node end blacksmith 1.1 true
/sd node add blacksmith 1.2 npc Bring me iron and coal.
/sd node end blacksmith 1.2 true
/sd validate blacksmith
```

For a one-time intro before the choice tree, make the intro the start node and auto-advance it with `next`:

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

`next` sends the target node immediately after the current node. It is best for intros, short cutscenes, and player/NPC line exchanges that should not require another click.

## Node Commands

Nodes can run commands when they are reached. Use `commands` for console commands and `player-commands` for commands run as the clicking player.

```yaml
"Do":
  speaker: npc
  lines:
    - "Take this map before you go."
  commands:
    - "give <player> minecraft:filled_map 1"
    - "playsound minecraft:entity.experience_orb.pickup player <player>"
  player-commands:
    - "me thanks the guide"
  end: true
```

Available command placeholders:

- `<player>`
- `<uuid>`
- `<dialogue>`
- `<node>`

The same command actions can be edited in-game:

```text
/sd command add guide Do console give <player> minecraft:filled_map 1
/sd command add guide Do console playsound minecraft:entity.experience_orb.pickup player <player>
/sd command remove guide Do console 1
/sd command clear guide Do console
```

## Editing Mistakes

Server-command editing now has cleanup commands, so you do not need to open YAML just to recover from a bad draft.

```text
/sd node info blacksmith 1.1
/sd line remove blacksmith 1 all
/sd branch blacksmith 1 right clear
/sd node next blacksmith Intro clear
/sd command remove blacksmith 1.1 console 1
/sd command clear blacksmith 1.1 console
/sd node remove blacksmith 1.1
/sd delete blacksmith confirm
```

Adding a branch or `next` target automatically sets that node to `end: false`, which avoids the common mistake from command-built drafts where the starter node still ends the conversation.

## Release And Publishing Notes

- Hangar page copy: [docs/hangar.md](docs/hangar.md)
- Modrinth page copy: [docs/modrinth.md](docs/modrinth.md)
- Server test checklist: [docs/testing-checklist.md](docs/testing-checklist.md)
- GitHub Wiki draft pages: [docs/wiki/Home.md](docs/wiki/Home.md)
- Updated sample guide dialogue: [docs/examples/guide.yml](docs/examples/guide.yml)
- Example merchant dialogue: [docs/examples/merchant.yml](docs/examples/merchant.yml)
- `v1.0.0` release notes: [docs/release-v1.0.0.md](docs/release-v1.0.0.md)

## Javadocs

Generate Javadocs locally:

```text
./gradlew javadoc
```

Generated docs are written to:

```text
build/docs/javadoc
```

The included GitHub Actions workflow can publish Javadocs to GitHub Pages. In the GitHub repository, open `Settings` -> `Pages`, set the source to `GitHub Actions`, then run or push the `Javadocs` workflow.

## License

Simple Dialogue is licensed under the MIT License. See [LICENSE](LICENSE).
