# Beta Release v0.1.4

Simple Dialogue `v0.1.4` adds node actions. A dialogue node can now send text and run commands when the player reaches it.

## What's Changed

- Added YAML `commands` for console commands that run when a node is reached.
- Added YAML `player-commands` for commands run as the clicking player.
- Added `/sd command add <dialogue> <node> <console|player> <command...>`.
- Added `/sd command clear <dialogue> <node> <console|player>`.
- Added command placeholders: `<player>`, `<uuid>`, `<dialogue>`, and `<node>`.
- Bumped the plugin version to `0.1.4`.
- Updated README, wiki, Hangar, Modrinth, and testing docs.

## Examples

Reward a player when a node is reached:

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

Add the same action in-game:

```text
/sd command add commandtest Do console say <player> reached commandtest Do
/sd command add commandtest Do console playsound minecraft:entity.experience_orb.pickup player <player>
```

## Update Notes

Existing dialogue files still load. `commands` and `player-commands` are optional fields, so older dialogue files do not need changes.

This is a good final beta candidate before `v1.0.0`, but it should be tested on a real Paper server before stable release.

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.4.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.4`
- Title: `Simple Dialogue v0.1.4 Beta`
- Publish the same jar to Hangar as version `v0.1.4`
- After the clean server test passes, cut `v1.0.0` with the same tested behavior unless fixes are needed
