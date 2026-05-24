# Server Testing Checklist

Use this before publishing the beta release or Hangar version.

## Environment

- Paper reports version `26.1.2`
- Java reports version `25` or newer
- FancyNpcs loads successfully
- Simple Dialogue loads successfully
- Console logs `Registered FancyNPC action: simple_dialogue`

## Startup Files

- `plugins/SimpleDialogue/config.yml` exists
- `plugins/SimpleDialogue/dialogues/guide.yml` exists
- `plugins/SimpleDialogue/dialogues/merchant.yml` exists on new installs
- `/plugins` shows `SimpleDialogue`

## Commands

- `/sd info guide` shows the sample dialogue
- `/sd reload` reloads without console errors
- `/sd reset` clears your active session
- `/sd new blacksmith Blacksmith gold` creates `blacksmith.yml`
- `/sd link blacksmith blacksmith` prints the FancyNpcs action commands
- `/sd line add blacksmith 1 Need something forged?` appends a line
- `/sd node add blacksmith 1.1 npc Bring me iron and coal.` creates a branch node
- `/sd branch blacksmith 1 right 1.1 Ask for work` wires a branch and choice prompt
- `/sd node end blacksmith 1.1 true` marks the node as an ending
- `/sd node add blacksmith Intro npc Hello, I'm the blacksmith.` creates an intro node
- `/sd node next blacksmith Intro 1` auto-advances the intro into node `1`
- `/sd validate` reports no errors for the bundled sample dialogues
- `/sd validate guide` reports no errors for only the guide dialogue

## Backwards Compatibility

- Upgrade a server with an existing `plugins/SimpleDialogue/dialogues/guide.yml`
- Confirm startup does not overwrite the existing dialogue file
- Confirm older flat node ids such as `"1.1"` still load
- Confirm nested node sections still load
- Confirm existing FancyNpcs `simple_dialogue` actions still advance left/right branches
- Confirm existing console-command fallbacks still work:

```text
/npc action guide LEFT_CLICK add console_command sd click guide left {player}
/npc action guide RIGHT_CLICK add console_command sd click guide right {player}
```

## FancyNpcs Integration

- Add left-click action:

```text
/npc action guide LEFT_CLICK add simple_dialogue guide
```

- Add right-click action:

```text
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

- Right-click starts the sample dialogue
- Sample branch prompts appear as `<Left> ... | <Right> ...`
- Right-click follows the right branch
- Left-click follows the left branch
- End nodes clear the session so the next click starts over

## Command-Built Dialogue Test

Run these commands on a clean test dialogue:

```text
/sd new commandtest Guide green
/sd node add commandtest Intro npc Hello, I'm the guide.
/sd node next commandtest Intro Help
/sd node add commandtest Help npc How can I help?
/sd branch commandtest Help left Leave Leave
/sd branch commandtest Help right Do Ask what you can do here
/sd node add commandtest Leave npc I'll be right here if you have more questions!
/sd node end commandtest Leave true
/sd node add commandtest Do npc You can explore survival, minigames, or creative worlds from here.
/sd branch commandtest Do left Leave Leave
/sd branch commandtest Do right Help Ask more questions
/sd validate commandtest
```

Then wire it to a FancyNpcs NPC and confirm:

- The first click sends the intro and `How can I help?` choice prompt together
- Right-click from `Help` goes to `Do`
- Right-click from `Do` loops back to `Help`
- Left-click from `Help` or `Do` goes to `Leave`
- Clicking again after `Leave` starts from `Intro`

## YAML Editing

- Edit a line in `guide.yml`
- Run `/sd reload`
- Confirm the changed line appears in-game
- Add a missing branch target intentionally
- Confirm the player sees a useful error and the session clears

## Release Smoke Test

- Run `.\gradlew.bat clean build`
- Install only the release jar from `build/libs/`
- Confirm the jar works without the repo present
- Save the server log from a clean startup
