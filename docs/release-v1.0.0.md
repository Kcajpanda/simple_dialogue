# Stable Release v1.0.0

Simple Dialogue `v1.0.0` is the first stable release.

## What's Included

- YAML dialogue files in `plugins/SimpleDialogue/dialogues/`.
- FancyNpcs `simple_dialogue` action for left-click and right-click NPC conversations.
- Console-command fallback with `/sd click`.
- Per-player active dialogue sessions.
- MiniMessage dialogue lines.
- Configurable NPC/player prefixes and choice prompts.
- `left` and `right` branch targets with `left-text` and `right-text`.
- `next` nodes for intros and automatic line flow.
- Node commands with `commands` and `player-commands`.
- `/sd validate` for missing targets, unusual speakers, blank lines, and command issues.
- Complete server-command editing for creating, inspecting, fixing, and deleting dialogue drafts.
- Thread-safe execution when FancyNpcs runs actions from its action executor thread.

## Tested Environment

- Paper `26.1.2-63`
- Java `25.0.3`
- FancyNpcs `2.10.0`

Server testing confirmed:

- Simple Dialogue loads cleanly.
- FancyNpcs registers the `simple_dialogue` action.
- Command-built dialogue trees validate and play in-game.
- Node console commands run from FancyNpcs clicks without async command-dispatch errors.
- Cleanup commands remove bad lines, branches, node commands, nodes, and test dialogues.

## Release Files

Upload:

```text
build/libs/simple-dialogue-1.0.0.jar
```

Do not upload the javadoc jar as the plugin file.

## Marketplace Checklist

- GitHub tag: `v1.0.0`
- GitHub release title: `Simple Dialogue v1.0.0`
- GitHub release type: stable/latest, not pre-release
- Hangar version: `v1.0.0`
- Modrinth version: `v1.0.0`
- Platform/loader: Paper
- Dependency: FancyNpcs `2.10.0` or compatible newer version
- Java requirement: Java `25` or newer
- Paper tested version: `26.1.2`
