# Project Next Steps

## GitHub

- Keep release notes on GitHub Releases after publishing. It is fine to keep a draft in `docs/` temporarily while preparing a release.
- Keep public usage docs in `README.md` and `docs/`.
- Add screenshots or short chat-output examples after the choice prompt format is tested in-game.
- Use GitHub Issues for bugs and feature ideas.
- Consider adding a `CHANGELOG.md` once there is more than one public release.

## Hangar

Hangar should stay the primary public listing because it is Paper's plugin repository.

For the first version, check:

- The project slug is spelled as intended.
- A version entry exists under the project's Versions page.
- The version is not still a draft.
- The version has a channel selected.
- The version has a Paper platform/file or a valid external download link.
- The version has at least one compatible Paper version selected.
- The visibility/publication button was completed after the file/link step.

For the current update, use `v0.1.1`, the GitHub Release URL, Paper `26.1.2`, and the `simple-dialogue-0.1.1.jar` file.

## Other Plugin Sites

Recommended order:

1. Hangar
2. Modrinth
3. CurseForge/BukkitDev
4. SpigotMC, later if you want another audience

CurseForge is optional. It can help discovery because Bukkit plugins still exist there, but it is another listing to maintain. Modrinth is usually a cleaner second stop for modern Minecraft projects. If you publish anywhere else, keep the same version number, changelog, supported Paper version, dependency note, and GitHub source link.

CurseForge Bukkit plugin uploads must be `.jar` files, so use the normal built plugin jar, not the javadoc jar.

## FancyNpcs Outreach

Suggested message:

```text
Hi! I made a small MIT-licensed Paper plugin that integrates with FancyNpcs through a custom action called simple_dialogue.

It lets server owners wire left/right-click NPC dialogue trees from YAML files. I tested it with FancyNpcs 2.10.0 on Paper 26.1.2 and it is now in beta:

https://github.com/Kcajpanda/simple_dialogue
https://github.com/Kcajpanda/simple_dialogue/releases/tag/v0.1.1

No pressure, but I would appreciate feedback on whether I am using the FancyNpcs action API in the intended way. If this is useful to your users, I would also be happy to adjust the docs or naming to make the integration clearer.
```

It is fine to mention that AI helped write the code if you want to be transparent, but lead with what the plugin does and how it was tested.

## Javadocs vs Wiki

Javadocs are generated API/reference pages from Java source comments. They are useful for developers who want to inspect classes, methods, records, and package summaries.

They are not a user guide, a tutorial, or a real wiki. For Simple Dialogue, the best public docs are still:

- `README.md` for install and quick start
- `docs/dialogues.md` for dialogue authoring
- Hangar description for discovery
- Javadocs only as a developer reference

A GitHub Wiki is optional. For now, repo docs are better because they are versioned with the code and show up naturally in pull requests.
