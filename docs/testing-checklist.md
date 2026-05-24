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
- `/sd line remove blacksmith 1 1` removes the first line
- `/sd line remove blacksmith 1 all` clears all lines
- `/sd node add blacksmith 1.1 npc Bring me iron and coal.` creates a branch node
- `/sd branch blacksmith 1 right 1.1 Ask for work` wires a branch and choice prompt
- `/sd branch blacksmith 1 right clear` removes the branch and choice text
- `/sd node end blacksmith 1.1 true` marks the node as an ending
- `/sd node add blacksmith Intro npc Hello, I'm the blacksmith.` creates an intro node
- `/sd node next blacksmith Intro 1` auto-advances the intro into node `1`
- `/sd start blacksmith Intro` sets the dialogue start node
- `/sd node info blacksmith Intro` lists node lines, branches, and commands
- `/sd command add blacksmith 1.1 console say <player> reached blacksmith 1.1` adds a console command
- `/sd command add blacksmith 1.1 console playsound minecraft:entity.experience_orb.pickup player <player>` adds a sound command
- `/sd command remove blacksmith 1.1 console 1` removes one command
- `/sd command clear blacksmith 1.1 console` clears all console commands from the node
- `/sd node remove blacksmith 1.1` removes the node and direct references to it
- `/sd delete blacksmith confirm` deletes the test dialogue
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
/sd start commandtest Intro
/sd node add commandtest Help npc How can I help?
/sd branch commandtest Help left Leave Leave
/sd branch commandtest Help right Do Ask what you can do here
/sd node add commandtest Leave npc I'll be right here if you have more questions!
/sd node end commandtest Leave true
/sd node add commandtest Do npc You can explore survival, minigames, or creative worlds from here.
/sd command add commandtest Do console say <player> reached commandtest Do
/sd command add commandtest Do console playsound minecraft:entity.experience_orb.pickup player <player>
/sd branch commandtest Do left Leave Leave
/sd branch commandtest Do right Help Ask more questions
/sd validate commandtest
```

Then wire it to a FancyNpcs NPC and confirm:

- The first click sends the intro and `How can I help?` choice prompt together
- Right-click from `Help` goes to `Do`
- Entering `Do` prints a console message and plays the configured sound
- Console has no `Asynchronous Command Dispatched Async` error from SimpleDialogue
- Right-click from `Do` loops back to `Help`
- Left-click from `Help` or `Do` goes to `Leave`
- Clicking again after `Leave` starts from `Intro`

## Cleanup Command Test

Run this on the `commandtest` dialogue after the behavior test:

```text
/sd node info commandtest Do
/sd command remove commandtest Do console 1
/sd command clear commandtest Do console
/sd branch commandtest Do right clear
/sd node next commandtest Intro clear
/sd node remove commandtest Do
/sd validate commandtest
/sd delete commandtest confirm
```

Confirm the remove commands print success messages and `/sd validate commandtest` reports the expected missing branch warnings/errors before deletion.

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
- Confirm clicking FancyNpcs actions that run node commands does not produce async command-dispatch errors
- Save the server log from a clean startup
