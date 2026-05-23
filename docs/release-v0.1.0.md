# Beta Release v0.1.0

Simple Dialogue `v0.1.0` is the first public beta release.

## Summary

Simple Dialogue adds small left/right dialogue trees for Paper servers and integrates with FancyNpcs through a custom `simple_dialogue` action. Dialogue is stored in readable YAML files, so server owners can author branching NPC conversations without writing Java.

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## What's Included

- YAML-backed dialogue files
- Sample `guide.yml` dialogue
- Left-click/right-click branching
- Per-player active dialogue sessions
- MiniMessage support in dialogue lines
- Configurable NPC/player chat prefixes
- FancyNpcs custom action: `simple_dialogue`
- Console-command fallback through `/sd click`
- Basic admin commands for creating, linking, editing, inspecting, resetting, and reloading dialogues
- Javadocs workflow for GitHub Pages

## Known Beta Notes

- Dialogue sessions are stored in memory and reset on reload/restart.
- The in-game editor is intentionally basic. Edit YAML directly for real branching trees.
- No database storage or quest-state requirements are included.
- The file format may change before a stable `v1.0.0`.
- Tested target is Paper `26.1.2`; older versions are not guaranteed.

## Install

1. Stop the server.
2. Install FancyNpcs.
3. Put `simple-dialogue-0.1.0.jar` in `plugins/`.
4. Start the server.
5. Add the action to an NPC:

```text
/npc action guide LEFT_CLICK add simple_dialogue guide
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

## GitHub Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.0.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.0`
- Title: `Simple Dialogue v0.1.0 Beta`
- Paste this file into the release body
