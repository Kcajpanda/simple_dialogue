# Modrinth Publishing Notes

Use Modrinth after Hangar once the release jar is tested.

## Project Setup

- Project type: Plugin
- Supported environments: Server
- Loaders/platforms: Paper
- License: MIT
- Source code: `https://github.com/Kcajpanda/simple_dialogue`
- Issues: `https://github.com/Kcajpanda/simple_dialogue/issues`

## Description

Simple Dialogue is a lightweight Paper plugin for left/right NPC conversation trees. It integrates with FancyNpcs through a custom `simple_dialogue` action and stores conversations as YAML files in `plugins/SimpleDialogue/dialogues/`.

Server owners can build trees directly with `/sd` commands or edit YAML files for larger conversations. Dialogue lines support MiniMessage, per-player sessions, choice prompts, and validation with `/sd validate`.

## Version Upload

- Version number: `v0.1.6`
- Version type: Beta
- Game versions: use the Paper/Minecraft versions tested in-game
- File: `simple-dialogue-0.1.6.jar`
- Dependency: FancyNpcs, required

## Version Changelog

`v0.1.6` fixes FancyNpcs async action execution and keeps the completed command-built dialogue tree tools:

- Reschedules FancyNpcs dialogue clicks and node commands onto the main server thread
- Create or update nodes with `/sd node add`
- Set the start node with `/sd start`
- Auto-advance intro nodes with `/sd node next`
- Mark endings with `/sd node end`
- Wire left/right branches with `/sd branch`
- Clear branches with `/sd branch <dialogue> <node> <left|right> clear`
- Run console/player commands when a node is reached with `/sd command add`
- Remove lines, nodes, node commands, and whole test dialogues without opening YAML
