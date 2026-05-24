# Beta Release v0.1.5

Simple Dialogue `v0.1.5` completes the server-command editor. It focuses on recovering from bad in-game drafts without opening YAML.

## What's Changed

- Added `/sd start <dialogue> <node>`.
- Added `/sd line remove <dialogue> <node> <line-number|all>`.
- Added `/sd node info <dialogue> <node>`.
- Added `/sd node remove <dialogue> <node>`.
- Added `/sd command remove <dialogue> <node> <console|player> <command-number|all>`.
- Added `/sd delete <dialogue> confirm`.
- Adding a branch or `next` target now automatically clears `end: true` on that node.
- Removing a node clears direct `left`, `right`, and `next` references to it.
- Bumped the plugin version to `0.1.5`.
- Updated README, wiki, Hangar, Modrinth, Javadocs, and testing docs.

## Why This Release Exists

The `v0.1.4` command test worked, but it was too easy to create a strange tree and then need to open YAML to fix it. `v0.1.5` adds the missing cleanup commands so server-command editing is complete enough for stable release testing.

## Cleanup Examples

```text
/sd node info blacksmith 1
/sd line remove blacksmith 1 all
/sd branch blacksmith 1 right clear
/sd node next blacksmith Intro clear
/sd command remove blacksmith 1.1 console 1
/sd command clear blacksmith 1.1 console
/sd node remove blacksmith 1.1
/sd delete blacksmith confirm
```

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.5.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.5`
- Title: `Simple Dialogue v0.1.5 Beta`
- Publish the same jar to Hangar as version `v0.1.5`
- After the clean server-command editing test passes, cut `v1.0.0` with the same tested behavior unless fixes are needed
