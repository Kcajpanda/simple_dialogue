# Beta Release v0.1.1

Simple Dialogue `v0.1.1` is a small but important beta update focused on cleaner dialogue choice prompts and public release metadata.

## What's Changed

- Added `left-text` and `right-text` fields for dialogue nodes.
- Added automatic RPG-style choice prompt lines after dialogue text.
- Added configurable choice prompt formats in `config.yml`.
- Updated the bundled `guide.yml` sample to use the new choice fields.
- Updated Gradle/plugin metadata to publish as version `0.1.1`.
- Changed Gradle group metadata and Java package namespace to `io.github.kcajpanda`.
- Removed an unused FancyNpcs import warning.
- Updated public docs and Hangar page draft for the new version.

## Choice Prompt Example

```yaml
"1":
  speaker: npc
  lines:
    - "Road's closed until morning."
  left-text: "Say goodbye"
  right-text: "Ask why"
  right: "1.1"
  left: "1.2"
```

With the default config, players see:

```text
<Guide> Road's closed until morning.
<Left> Say goodbye | <Right> Ask why
```

## Update Notes

Existing server files in `plugins/SimpleDialogue/dialogues/` are not overwritten automatically. To see the new guide prompt style on an existing server, replace or edit:

```text
plugins/SimpleDialogue/dialogues/guide.yml
```

The updated sample is included in the repo at:

```text
src/main/resources/dialogues/guide.yml
docs/examples/guide.yml
```

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## Known Beta Notes

- Dialogue sessions are stored in memory and reset on reload/restart.
- YAML editing is recommended for real branching trees.
- Choice text is treated as plain text; prompt colors and layout come from `config.yml`.
- The file format and command set may still change before `v1.0.0`.

## Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.1.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.1`
- Title: `Simple Dialogue v0.1.1 Beta`
