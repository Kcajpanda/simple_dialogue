# Simple Dialogue

Simple Dialogue is a small Paper plugin for left/right NPC dialogue trees. It was built to pair with FancyNpcs: add one custom FancyNpcs action to an NPC, then players can left-click or right-click through a YAML-backed conversation.

This project is moving toward a stable release. Server owners should still run the release checklist on their exact Paper/FancyNpcs versions before upgrading a live server.

## Features

- YAML dialogue files in `plugins/SimpleDialogue/dialogues/`
- Left-click and right-click branching
- Per-player in-memory conversation sessions
- MiniMessage support in dialogue lines
- RPG-style choice prompts with `left-text` and `right-text`
- Configurable NPC/player chat prefixes
- Custom FancyNpcs action: `simple_dialogue`
- Command fallback for servers that prefer console-command NPC actions
- In-game commands for creating dialogues, adding nodes, wiring branches, and marking endings
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
build/libs/simple-dialogue-0.1.3.jar
```

## Installation

1. Stop the server.
2. Install FancyNpcs `2.10.0` or newer.
3. Upload `simple-dialogue-0.1.3.jar` to `plugins/`.
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
<Guide> Road's closed until morning.
<Left> Say goodbye | <Right> Ask why
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
start: "1"
nodes:
  "1":
    speaker: npc
    lines:
      - "Road's closed until morning."
    left-text: "Say goodbye"
    right-text: "Ask why"
    right: "1.1"
    left: "1.2"
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

See [docs/dialogues.md](docs/dialogues.md) for authoring tips and file-format details.

## Commands

```text
/sd new <dialogue> <npc-name> [name-color]
/sd npcname <dialogue> <name> <name-color> [bracket-color]
/sd link <dialogue> <fancy-npc>
/sd line add <dialogue> <node> <text...>
/sd node add <dialogue> <node> [npc|player] [text...]
/sd node end <dialogue> <node> <true|false>
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
3. Add nodes with `/sd node add <id> <node> [npc|player] [text...]`.
4. Wire choices with `/sd branch <id> <node> <left|right> <target> [choice text...]`.
5. Mark endings with `/sd node end <id> <node> true`.
6. Run `/sd validate`.
7. Test the NPC in-game.

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

## Release And Publishing Notes

- Hangar page copy: [docs/hangar.md](docs/hangar.md)
- Publishing and outreach notes: [docs/project-next-steps.md](docs/project-next-steps.md)
- Server test checklist: [docs/testing-checklist.md](docs/testing-checklist.md)
- Stable release readiness: [docs/release-readiness.md](docs/release-readiness.md)
- GitHub Wiki draft pages: [docs/wiki/Home.md](docs/wiki/Home.md)
- Updated sample guide dialogue: [docs/examples/guide.yml](docs/examples/guide.yml)
- Example merchant dialogue: [docs/examples/merchant.yml](docs/examples/merchant.yml)
- Draft `v0.1.3` release notes: [docs/release-v0.1.3.md](docs/release-v0.1.3.md)
- Previous `v0.1.2` release notes: [docs/release-v0.1.2.md](docs/release-v0.1.2.md)

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
