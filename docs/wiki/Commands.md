# Commands

Admin/editing commands require `simpledialogue.admin`, which defaults to server operators.

```text
/sd new <dialogue> <npc-name> [name-color]
/sd delete <dialogue> confirm
/sd start <dialogue> <node>
/sd npcname <dialogue> <name> <name-color> [bracket-color]
/sd link <dialogue> <fancy-npc>
/sd line add <dialogue> <node> <text...>
/sd line remove <dialogue> <node> <line-number|all>
/sd node add <dialogue> <node> [npc|player] [text...]
/sd node remove <dialogue> <node>
/sd node info <dialogue> <node>
/sd node end <dialogue> <node> <true|false>
/sd node next <dialogue> <node> <target|clear>
/sd command add <dialogue> <node> <console|player> <command...>
/sd command remove <dialogue> <node> <console|player> <command-number|all>
/sd command clear <dialogue> <node> <console|player>
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
/sd start blacksmith 1
/sd node add blacksmith 1 npc Need something forged?
/sd branch blacksmith 1 left 1.1 Leave
/sd branch blacksmith 1 right 1.2 Ask for work
/sd node add blacksmith 1.1 npc Then keep your blade sharp.
/sd node end blacksmith 1.1 true
/sd node add blacksmith 1.2 npc Bring me iron and coal.
/sd node end blacksmith 1.2 true
/sd validate blacksmith
```

Add an intro that automatically enters the main node:

```text
/sd node add blacksmith Intro npc Hello, I'm the blacksmith.
/sd node next blacksmith Intro 1
```

Run a command when a node is reached:

```text
/sd command add blacksmith 1.2 console give <player> minecraft:bread 4
/sd command add blacksmith 1.2 console playsound minecraft:entity.experience_orb.pickup player <player>
```

Use `clear` as the branch target to remove a branch:

```text
/sd branch blacksmith 1 left clear
```

Remove mistakes:

```text
/sd node info blacksmith 1.2
/sd line remove blacksmith 1.2 1
/sd command remove blacksmith 1.2 console 1
/sd node remove blacksmith 1.2
/sd delete blacksmith confirm
```
