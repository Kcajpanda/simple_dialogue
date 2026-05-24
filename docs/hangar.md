## Summary

Simple Dialogue is a lightweight Paper plugin for small NPC conversation trees.

It integrates with FancyNpcs through a custom `simple_dialogue` action. Add the action to an NPC's left-click and right-click triggers, then players can move through branching YAML dialogue by clicking the NPC.

Dialogue files are plain YAML, stored in `plugins/SimpleDialogue/dialogues/`, and support MiniMessage in dialogue lines. The plugin keeps each player's active conversation node in memory while they click through the tree.

This is the first stable release of Simple Dialogue.

## Features

- YAML dialogue files
- Left-click and right-click branching
- FancyNpcs custom action: `simple_dialogue`
- Console-command fallback with `/sd click`
- Per-player active dialogue sessions
- MiniMessage dialogue lines
- Configurable NPC/player prefixes
- RPG-style choice prompts with `left-text` and `right-text`
- Sample dialogue included on first startup
- Extra merchant example dialogue included for testing
- Admin commands for creating dialogues, adding nodes, auto-advancing intros, wiring branches, running node commands, and marking endings
- `/sd validate` command for checking dialogue tree mistakes

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

Spigot is not supported.

## Setup

After installing Simple Dialogue and FancyNpcs, add both actions to your NPC:

```text
/npc action guide LEFT_CLICK add simple_dialogue guide
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

The last argument is the dialogue id from the YAML file, such as `guide` for `plugins/SimpleDialogue/dialogues/guide.yml`.

## Stable Notes

- Dialogue sessions reset on plugin reload/server restart.
- YAML editing is still recommended for reviewing large trees.
- Node commands are run on the main server thread, including when triggered through FancyNpcs.

## Links

- Source: https://github.com/Kcajpanda/simple_dialogue
- Issues: https://github.com/Kcajpanda/simple_dialogue/issues
- Documentation: https://github.com/Kcajpanda/simple_dialogue#readme
- Release: https://github.com/Kcajpanda/simple_dialogue/releases/tag/v1.0.0
- License: MIT

## Version Channel Text

`v1.0.0` is the first stable release. It includes YAML dialogue trees, left/right branching, `next` intro nodes, node commands, `/sd validate`, complete server-command editing, FancyNpcs `simple_dialogue` actions, console-command fallback, and thread-safe node command execution from FancyNpcs clicks.

## Manual Version Checklist

- Version name: `v1.0.0`
- Channel: `Release`
- Platform: Paper
- Platform versions: `26.1.2`
- File: `simple-dialogue-1.0.0.jar`
- External release URL: `https://github.com/Kcajpanda/simple_dialogue/releases/tag/v1.0.0`
- Dependency: FancyNpcs, required
- Save/publish the version, then check the public `/versions` page while logged out or in a private window.
