# Simple Dialogue

Simple Dialogue is a Paper plugin for small left/right dialogue trees that can be triggered from FancyNPCs.

It stores conversations in readable YAML files, remembers each player's current node while they click through a conversation, and automatically prefixes NPC dialogue with the configured NPC display name and colors.

## Requirements

- Paper `26.1.2`, tested against API `26.1.2.build.63-stable`
- Java `25` or newer
- FancyNPCs `2.10.0` or compatible newer version

Paper changed plugin dependency coordinates for `26.1+`. This project uses the newer `26.1.2.build.63-stable` Paper API coordinate to match your server log.

## Building

```text
./gradlew clean build
```

On Windows:

```text
.\gradlew.bat clean build
```

The compiled plugin jar is written to:

```text
build/libs/simple-dialogue-0.1.0.jar
```

## Pterodactyl Install

Your Pterodactyl log shows the correct runtime already:

```text
openjdk version "25.0.2"
Paper 26.1.2-63
Implementing API version 26.1.2.build.63-stable
```

Keep using the `java_25` yolk for this server.

Install steps:

1. Stop the server.
2. Upload `build/libs/simple-dialogue-0.1.0.jar` to `/plugins/`.
3. Upload FancyNPCs `2.10.0` or newer to `/plugins/`.
4. Start the server.
5. Look for `SimpleDialogue` in `/plugins`.
6. Confirm this file exists after startup:

```text
plugins/SimpleDialogue/dialogues/guide.yml
```

If Simple Dialogue loads but the custom `simple_dialogue` FancyNPC action does not appear, confirm FancyNPCs loaded first and check the console for `Registered FancyNPC action: simple_dialogue`.

## FancyNPC Setup

Simple Dialogue registers a custom FancyNPC action:

```text
simple_dialogue
```

Add it to both click types so the plugin can tell left from right:

```text
/npc action guide LEFT_CLICK add simple_dialogue guide
/npc action guide RIGHT_CLICK add simple_dialogue guide
```

The final `guide` is the dialogue id from `plugins/SimpleDialogue/dialogues/guide.yml`.

Command fallback:

```text
/npc action guide LEFT_CLICK add console_command sd click guide left {player}
/npc action guide RIGHT_CLICK add console_command sd click guide right {player}
```

## First Test

1. Create or pick a FancyNPC named `guide`.
2. Add the two FancyNPC actions above.
3. Join the server.
4. Right-click the NPC once. You should see:

```text
<Guide> Road's closed until morning.
<Guide> Right-click to ask why. Left-click to say goodbye.
```

5. Right-click again to follow the right branch, or left-click to follow the left branch.

To create a new dialogue from in-game:

```text
/sd new blacksmith Blacksmith gold
/sd link blacksmith blacksmith
/sd line add blacksmith 1 Need something forged?
```

Then wire your FancyNPC named `blacksmith`:

```text
/npc action blacksmith LEFT_CLICK add simple_dialogue blacksmith
/npc action blacksmith RIGHT_CLICK add simple_dialogue blacksmith
```

## Dialogue YAML

```yaml
id: guide
npc:
  fancy-npc: guide
  name: Guide
  name-color: green
  bracket-color: gray
start: "1"
nodes:
  "1":
    speaker: npc
    lines:
      - "Road's closed until morning."
      - "<gray>Right-click to ask why. Left-click to say goodbye.</gray>"
    right: "1.1"
    left: "1.2"
```

NPC lines are automatically prefixed with the configured style:

```text
<Guide> Road's closed until morning.
```

Dialogue lines support MiniMessage formatting.

## Commands

```text
/sd new <dialogue> <npc-name> [name-color]
/sd npcname <dialogue> <name> <name-color> [bracket-color]
/sd link <dialogue> <fancy-npc>
/sd line add <dialogue> <node> <text...>
/sd click <dialogue> <left|right> [player]
/sd reset [player]
/sd info <dialogue>
/sd reload
```

## Docs

Generate Javadocs locally:

```text
./gradlew javadoc
```

Generated docs are written to:

```text
build/docs/javadoc
```

The included GitHub Actions workflow can publish those Javadocs to GitHub Pages.

To turn that on:

1. Push this repo to GitHub, including `.github/workflows/javadocs.yml`.
2. Open the repository on GitHub.
3. Go to `Settings` -> `Pages`.
4. Under `Build and deployment`, set `Source` to `GitHub Actions`.
5. Push to `main`, or open `Actions` -> `Javadocs` -> `Run workflow`.
6. After the workflow finishes, GitHub will show the Pages URL in the deployment summary.

The workflow builds docs with Java 25 and publishes `build/docs/javadoc`.
