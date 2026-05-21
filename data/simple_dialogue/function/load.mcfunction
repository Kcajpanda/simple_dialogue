scoreboard objectives add sd.input dummy
scoreboard objectives add sd.node dummy
scoreboard objectives add sd.npc dummy
scoreboard objectives add sd.once dummy
scoreboard players set #right sd.input 1
scoreboard players set #left sd.input 2
scoreboard players set #none sd.input 0
scoreboard players set #guard sd.npc 1
tellraw @a [{"text":"[simple_dialogue] ","color":"gold"},{"text":"loaded. FancyNPC can call "},{"text":"function simple_dialogue:click/right","color":"aqua"},{"text":" and "},{"text":"function simple_dialogue:click/left","color":"aqua"},{"text":"."}]
