# Troubleshooting

## Unknown Dialogue

Run:

```text
/sd info <dialogue>
```

If the dialogue is missing, confirm the YAML file exists in `plugins/SimpleDialogue/dialogues/`, then run `/sd reload`.

## Branch Does Nothing

A blank or missing branch ends the current conversation. Check the current node for `left` or `right`, then run:

```text
/sd validate <dialogue>
```

## FancyNpcs Action Is Missing

Confirm FancyNpcs loaded before Simple Dialogue. If needed, use console-command actions:

```text
/npc action guide LEFT_CLICK add console_command sd click guide left {player}
/npc action guide RIGHT_CLICK add console_command sd click guide right {player}
```

## YAML Changes Do Not Appear

Run:

```text
/sd reload
```

Then test again. If validation reports errors, fix those before testing branches.
