# Demo resolver: add this tag to yourself, then click any FancyNPC that runs the core functions.
# /tag @s add sd.demo.guard
execute if entity @s[tag=sd.demo.guard] run scoreboard players operation @s sd.npc = #guard sd.npc
execute if entity @s[tag=sd.demo.guard] run tag @s add sd.npc.guard
