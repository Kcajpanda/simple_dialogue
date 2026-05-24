# Beta Release v0.1.2

Simple Dialogue `v0.1.2` adds validation tools and another bundled example dialogue for safer testing.

## What's Changed

- Added `/sd validate [dialogue]`.
- Validation reports missing branch targets, missing `left-text`/`right-text`, missing start nodes, empty node lines, unusual speakers, and nodes marked `end: true` while still having branches.
- Added a bundled `merchant.yml` test dialogue.
- Added `docs/examples/merchant.yml` for easy upload to existing servers.
- Bumped plugin metadata to `0.1.2`.
- Added plugin description, author, and website metadata.

## New Command

Validate every loaded dialogue:

```text
/sd validate
```

Validate one dialogue:

```text
/sd validate guide
```

If everything looks good, the command prints:

```text
SimpleDialogue validation passed.
```

## Update Notes

Existing server dialogue files are not overwritten automatically. New installs will receive both:

```text
plugins/SimpleDialogue/dialogues/guide.yml
plugins/SimpleDialogue/dialogues/merchant.yml
```

For existing servers, upload examples from:

```text
docs/examples/guide.yml
docs/examples/merchant.yml
```

## Requirements

- Paper `26.1.2`
- Java `25` or newer
- FancyNpcs `2.10.0` or compatible newer version

## Release Checklist

- Build with `.\gradlew.bat clean build`
- Upload `build/libs/simple-dialogue-0.1.2.jar`
- Mark the release as `Pre-release`
- Tag: `v0.1.2`
- Title: `Simple Dialogue v0.1.2 Beta`
- Publish the same jar to Hangar as version `v0.1.2`
