# GameRule Manager

This mod provide new ways to control gamerules.

## Features

- Change default gamerule values. (Via config)
- Lock specific gamerule. (Via config)
- Set gamerule standalone for each dimension. (Via command)

## How to use

### Config

There are 2 config files

#### Global config

Config file is at `.minecraft/config/gamerule_manager/default.json`. If this file is not existed, create it. Example:

```json5
{
  //Apply to all dimension
  "default": {
    "doDaylightCycle": false,
    "doMobSpawning": {
      "value": false,
      //Lock key is optional
      "lock": true
    },
    "doTraderSpawning": {
      "value": false,
      "lock": false
    }
  },
  "difficulty": "easy"
}
```

#### Dimension specific config

Config file is at `.minecraft/config/gamerule_manager/specific.json`. If this file is not existed, create it. Example:

```json5
{
  //Dimension id, NOTE: You need to use "/gamerulemanager" to split out first to apply
  "minecraft:overworld": {
    "gamerules": {
      "keepInventory": {
        "value": true,
        "lock": true
      }
    },
    "difficulty": {
      "value": "hard",
      "lock": true
    }
  }
}
```

### Command

`/gamerulemanager <create/remove/list>`: Used to control whether specific dimension use standalone gamerules.

Once you split out, you can join that dimension and use `/gamerule` and `/difficulty` to change gamerule and difficulty
only for that dimension.
