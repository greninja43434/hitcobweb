# Cobweb Trap Mod

A Minecraft Fabric mod that instantly places cobwebs on mobs/players when you hit them with a sword!

## Features

✅ **Instant Cobweb Placement** - Places cobwebs instantly on hit with no turning/aiming required
✅ **Configurable Settings** - Fully customizable via JSON config file
✅ **Multiple Patterns** - Circle, Square, and Random spread patterns
✅ **Target Filtering** - Choose to affect players only, mobs only, or both
✅ **Cooldown System** - Prevent spam with adjustable cooldown
✅ **Trap Chance** - Set probability of trap activation (0-100%)
✅ **Visual Effects** - Particles and sounds on cobweb placement
✅ **Keybind Toggle** - Press K to enable/disable mod
✅ **Adjustable Radius** - Control placement spread distance
✅ **Damage Requirement** - Set minimum sword damage threshold
✅ **Per-Entity Cooldown** - Individual cooldown tracking per player

## Configuration

The mod creates a `config/cobwebtrap.json` file with these options:

```json
{
  "modEnabled": true,
  "toggleKeybind": "K",
  "placementRadius": 3,
  "maxCobwebsPerHit": 8,
  "cobwebDurationTicks": 200,
  "playersOnly": false,
  "mobsOnly": false,
  "includeArmored": true,
  "enableParticles": true,
  "enableSound": true,
  "trapChance": 1.0,
  "spreadPattern": 0,
  "cooldownEnabled": true,
  "cooldownTicks": 20,
  "minimumDamage": 2.0
}
```

## Building

Run the following command to build the mod:

```bash
./gradlew build
```

The compiled JAR will be located in `build/libs/`

## Installation

1. Install Fabric Loader and Fabric API
2. Place the compiled JAR in your `mods` folder
3. Launch Minecraft with the Fabric Loader

## Usage

- Hit any mob or player with a sword to trigger cobweb placement
- Press K to toggle the mod on/off
- Edit `config/cobwebtrap.json` to customize behavior

## Author

Greninja43434
