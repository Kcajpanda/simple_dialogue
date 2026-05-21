# Simple Dialogue

Tiny datapack API for adding left/right click dialogue trees to FancyNPC.

## FancyNPC setup

Put these two commands on every NPC:

```mcfunction
function simple_dialogue:click/right
function simple_dialogue:click/left
```

The functions assume FancyNPC runs the command as the player who clicked. If your FancyNPC action runs as console, use the plugin's player placeholder/selector support so the function executes as the clicking player.

## How it works

`simple_dialogue:click/right` sets `@s sd.input` to `1`.

`simple_dialogue:click/left` sets `@s sd.input` to `2`.

Both functions then run:

```mcfunction
function #simple_dialogue:resolve
function #simple_dialogue:logic
```

Other datapacks can add functions to those tags:

```json
{
  "replace": false,
  "values": [
    "my_pack:dialogue/resolve",
    "my_pack:dialogue/main"
  ]
}
```

Use `#simple_dialogue:resolve` to figure out which NPC was clicked, usually by adding a temporary player tag or setting `@s sd.npc`.

Use `#simple_dialogue:logic` to advance the player's dialogue node based on:

- `@s sd.input`: `1` is right click, `2` is left click.
- `@s sd.node`: the player's current dialogue node.
- `@s sd.npc`: optional numeric NPC id.
- player tags such as `sd.npc.guard`, `quest.started`, or your own tags.

The core click functions clear `sd.input`, `sd.npc`, and temporary `sd.click.*` tags after the logic tag runs.

## Included demo

Run:

```mcfunction
/tag @s add sd.demo.guard
```

Then click an NPC wired to the two core functions. Right and left clicks will step through a small guard dialogue tree.

Remove the demo tag with:

```mcfunction
/tag @s remove sd.demo.guard
```

## Files to copy for your own dialogue

- `data/simple_dialogue/function/example/resolve.mcfunction`
- `data/simple_dialogue/function/example/guard.mcfunction`
- `data/simple_dialogue/function/example/guard_nodes/*.mcfunction`
- `data/simple_dialogue/tags/function/resolve.json`
- `data/simple_dialogue/tags/function/logic.json`
