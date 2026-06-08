# MMO HUD

MMO HUD is a RuneLite external plugin that adds MMO-style unit frames for
your character, your current target, and RuneLite party members.

## Features

- Shows a movable player HUD frame with Hitpoints, Prayer, combat level, and a portrait orb.
- Shows a mirrored target HUD frame while fighting an NPC or player.
- Uses RuneLite party data to show smaller party member frames under the player frame.
- Supports configurable portrait behavior, enemy model portraits, target frames, party frames, and bar animations.
- Uses RuneLite overlay movement, snapping, and reset behavior.

## Known issue

- After hopping worlds, the player portrait orb can appear blank until the player changes equipment. Changing any gear refreshes the portrait. The HUD bars continue to update.

## Notes

- Enemy Hitpoints are limited to the health data RuneLite exposes. The plugin does not know true NPC max HP unless RuneLite exposes a useful ratio for that target.
- Party frames use RuneLite's built-in Party plugin data. This plugin does not run its own server or send party data anywhere.
- This repository intentionally does not include an `icon.png`.
