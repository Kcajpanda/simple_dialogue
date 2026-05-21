execute unless entity @s[tag=sd.npc.guard] run return 0
execute if score @s sd.once matches 0 if score @s sd.input matches 1 if score @s sd.node matches 0 run function simple_dialogue:example/guard_nodes/right_0
execute if score @s sd.once matches 0 if score @s sd.input matches 2 if score @s sd.node matches 0 run function simple_dialogue:example/guard_nodes/left_0
execute if score @s sd.once matches 0 if score @s sd.input matches 1 if score @s sd.node matches 1 run function simple_dialogue:example/guard_nodes/right_1
execute if score @s sd.once matches 0 if score @s sd.input matches 2 if score @s sd.node matches 1 run function simple_dialogue:example/guard_nodes/left_1
execute if score @s sd.once matches 0 if score @s sd.input matches 1 if score @s sd.node matches 2 run function simple_dialogue:example/guard_nodes/right_2
execute if score @s sd.once matches 0 if score @s sd.input matches 2 if score @s sd.node matches 2 run function simple_dialogue:example/guard_nodes/left_2
