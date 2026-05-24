# Compatibility And Releases

Before publishing a release:

- Run `.\gradlew.bat clean build`.
- Test a clean install.
- Test upgrading a server that already has dialogue files when the release changes YAML behavior.
- Confirm FancyNpcs `simple_dialogue` actions still work.
- Confirm console-command fallback actions still work.
- Run `/sd validate` for bundled examples and existing server dialogues.

## Backwards Compatibility Checks

- Existing dialogue files are not overwritten on startup.
- Flat node ids such as `"1.1"` still load.
- Nested node sections still load.
- Existing branch fields `left`, `right`, `left-text`, and `right-text` still behave the same.
- Existing `simple_dialogue` FancyNpcs actions still advance the current session.

## Tested Versions

- Simple Dialogue `1.0.0`
- Paper `26.1.2-63`
- Java `25.0.3`
- FancyNpcs `2.10.0`

## Stable Release Notes

`1.0.0` is the first stable release. It includes YAML dialogue trees, left/right branching, `next` auto-advance nodes, node commands, validation, FancyNpcs integration, console-command fallback, and complete server-command editing.
