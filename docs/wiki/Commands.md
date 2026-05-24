# Commands

Admin/editing commands require `simpledialogue.admin`, which defaults to server operators.

```text
/sd new <dialogue> <npc-name> [name-color]
/sd npcname <dialogue> <name> <name-color> [bracket-color]
/sd link <dialogue> <fancy-npc>
/sd line add <dialogue> <node> <text...>
/sd node add <dialogue> <node> [npc|player] [text...]
/sd node end <dialogue> <node> <true|false>
/sd branch <dialogue> <node> <left|right> <target|clear> [choice text...]
/sd click <dialogue> <left|right> [player]
/sd reset [player]
/sd info <dialogue>
/sd validate [dialogue]
/sd reload
```

## Build A Tree With Commands

```text
/sd new blacksmith Blacksmith gold
/sd node add blacksmith 1 npc Need something forged?
/sd branch blacksmith 1 left 1.1 Leave
/sd branch blacksmith 1 right 1.2 Ask for work
/sd node add blacksmith 1.1 npc Then keep your blade sharp.
/sd node end blacksmith 1.1 true
/sd node add blacksmith 1.2 npc Bring me iron and coal.
/sd node end blacksmith 1.2 true
/sd validate blacksmith
```

Use `clear` as the branch target to remove a branch:

```text
/sd branch blacksmith 1 left clear
```
