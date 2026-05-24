# Beta Release v0.1.6

Simple Dialogue `v0.1.6` fixes the last server-test blocker found with FancyNpcs node commands.

## What's Changed

- Fixed `Asynchronous Command Dispatched Async` errors when FancyNpcs executes `simple_dialogue` actions from its action executor thread.
- Dialogue clicks invoked off-thread are now rescheduled onto the main Bukkit server thread before sending messages, changing sessions, and running node commands.
- Kept the completed server-command editor from `v0.1.5`: start-node editing, cleanup commands, node inspection, and dialogue deletion.
- Bumped the plugin version to `0.1.6`.
- Updated README, Hangar, Modrinth, and testing docs.

## Test Focus

Retest a node with console commands through a real FancyNpcs click action:

```text
/sd command add commandtest Do console say <player> reached commandtest Do
/sd command add commandtest Do console playsound minecraft:entity.experience_orb.pickup player <player>
```

Expected result:

- The dialogue advances normally.
- The `say` command runs.
- The sound plays.
- The console does not log `Asynchronous Command Dispatched Async`.

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.6.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.6`
- Title: `Simple Dialogue v0.1.6 Beta`
- Publish the same jar to Hangar as version `v0.1.6`
- If the async-command test passes, cut `v1.0.0` with the same behavior unless fixes are needed
