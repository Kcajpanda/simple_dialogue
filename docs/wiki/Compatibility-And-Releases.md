# Compatibility And Releases

Before publishing a stable release:

- Run `.\gradlew.bat clean build`.
- Test a clean install.
- Test upgrading a server that already has dialogue files.
- Confirm FancyNpcs `simple_dialogue` actions still work.
- Confirm console-command fallback actions still work.
- Run `/sd validate` for bundled examples and existing server dialogues.

## Backwards Compatibility Checks

- Existing dialogue files are not overwritten on startup.
- Flat node ids such as `"1.1"` still load.
- Nested node sections still load.
- Existing branch fields `left`, `right`, `left-text`, and `right-text` still behave the same.
- Existing `simple_dialogue` FancyNpcs actions still advance the current session.

Keep a compatibility table on this page once each Paper/FancyNpcs version is tested in-game.
