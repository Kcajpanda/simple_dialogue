# Stable Release Readiness

Use this as the gate before removing the beta/pre-release label.

## Must Have

- `plugin.yml` has valid Paper/Bukkit plugin metadata.
- `.\gradlew.bat clean build` passes.
- `/sd validate` passes for bundled examples and at least one existing server dialogue.
- Server testing checklist passes on a clean install.
- Backwards compatibility checklist passes on an upgrade install.
- FancyNpcs custom action and console-command fallback both work.
- README quick start matches the current command behavior.
- GitHub Wiki or `docs/` pages cover install, commands, YAML authoring, and troubleshooting.

## Nice To Have

- A GitHub Actions build check that runs on pull requests.
- A small changelog for each release.
- Screenshots or copied chat output from the sample dialogue.
- A tested compatibility table for Paper and FancyNpcs versions.

## Recommendation

Do not remove the beta label based only on compile success. The plugin is small, but its real contract is server behavior: startup files, YAML compatibility, FancyNpcs actions, and command-driven editing. Run the clean-install and upgrade-install checklist first, then publish the stable release.
