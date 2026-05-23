# Hangar Page Draft

Use this as the starting text for the Hangar project page.

## Project Name

Simple Dialogue

## Short Description

Small YAML dialogue trees for FancyNpcs, with left-click/right-click branching.

## Category Suggestions

- Chat
- Roleplay
- Miscellaneous
- Utility

## Long Description

Simple Dialogue is a lightweight Paper plugin for small NPC conversation trees.

It integrates with FancyNpcs through a custom `simple_dialogue` action. Add the action to an NPC's left-click and right-click triggers, then players can move through branching YAML dialogue by clicking the NPC.

Dialogue files are plain YAML, stored in `plugins/SimpleDialogue/dialogues/`, and support MiniMessage in dialogue lines. The plugin keeps each player's active conversation node in memory while they click through the tree.

This is a beta release intended for testing, small servers, and feedback.

## Features

- YAML dialogue files
- Left-click and right-click branching
- FancyNpcs custom action: `simple_dialogue`
- Console-command fallback with `/sd click`
- Per-player active dialogue sessions
- MiniMessage dialogue lines
- Configurable NPC/player prefixes
- Sample dialogue included on first startup
- Basic admin commands for creating and linking dialogues

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

## Beta Notes

- This is early beta software.
- Dialogue sessions reset on plugin reload/server restart.
- YAML editing is recommended for real branching trees.
- The file format and command set may change before `v1.0.0`.

## Links

- Source: add GitHub repo URL
- Issues: add GitHub issues URL
- Documentation: add README or GitHub Pages URL
- License: MIT

## Version Channel Text

`v0.1.0` is the first public beta. Please test on a non-production server first and report issues with your Paper, Java, and FancyNpcs versions.
