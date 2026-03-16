# GameRule Manager

This mod provide new ways to control gamerules.

## Features

- Change default gamerule values. (Via config)
- Lock specific gamerule. (Via config)
- Set gamerule standalone for each dimension. (Via command)

## How to use

### Config

Config file is at `.minecraft/config/gamerule_manager.json`. If this file is not existed, create it. Example:

```json5
{
  //Apply to all dimension
  "default": {
    "doDaylightCycle": false,
    "doMobSpawning": {
      "value": false,
      "lock": true //Lock key is optional
    },
    "doTraderSpawning": {
      "value": false,
      "lock": false
    }
  },
  //Apply to specific dimension. NOTE: You need to use "/gamerulemanager" to split out first to apply
  "minecraft:overworld": {
    "keepInventory": {
      "value": true,
      "lock": true
    }
  }
}
```

### Command

`/gamerulemanager <create/remove/list>`: Used to control whether specific dimension use standalone gamerules.

Once you split out, you can join that dimension and use `/gamerule` to change gamerule only for that dimension.
