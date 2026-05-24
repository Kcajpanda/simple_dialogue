# Beta Release v0.1.3

Simple Dialogue `v0.1.3` is the command-authoring beta. It adds the missing server commands needed to build and wire dialogue trees without editing YAML by hand.

## What's Changed

- Added `/sd node add <dialogue> <node> [npc|player] [text...]`.
- Added `/sd node end <dialogue> <node> <true|false>`.
- Added `/sd node next <dialogue> <node> <target|clear>` for intro nodes and automatic line flow.
- Added `/sd branch <dialogue> <node> <left|right> <target|clear> [choice text...]`.
- Improved tab completion for the new nested command syntax.
- Corrected `plugin.yml` `api-version` to `1.21`.
- Updated README and dialogue authoring docs for command-built trees.
- Added GitHub Wiki draft pages under `docs/wiki/`.
- Added stable release readiness notes.
- Added a GitHub Actions build workflow.

## New Command Examples

```text
/sd new blacksmith Blacksmith gold
/sd node add blacksmith Intro npc Hello, I'm the blacksmith.
/sd node next blacksmith Intro 1
/sd node add blacksmith 1 npc Need something forged?
/sd branch blacksmith 1 left 1.1 Leave
/sd branch blacksmith 1 right 1.2 Ask for work
/sd node add blacksmith 1.1 npc Then keep your blade sharp.
/sd node end blacksmith 1.1 true
/sd node add blacksmith 1.2 npc Bring me iron and coal.
/sd node end blacksmith 1.2 true
/sd validate blacksmith
```

Clear a branch:

```text
/sd branch blacksmith 1 left clear
```

## Update Notes

Existing server dialogue files are not overwritten automatically. This release changes command tooling and plugin metadata, but does not intentionally change the dialogue YAML format.

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.3.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.3`
- Title: `Simple Dialogue v0.1.3 Beta`
- Publish the same jar to Hangar as version `v0.1.3`
- After the clean server test passes, cut `v1.0.0` with the same tested behavior unless fixes are needed
