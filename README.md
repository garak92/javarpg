# Java RPG 

Warning: WIP in very early stages.

The purpose of this project is to build an RPG with a highly linear gameplay based on leveling up in progressively harder zones until enough level is reached to unlock the dungeon and defeat the final boss.

## How to run 

- Clone the repo
- Run ./gradlew run

## Packaging

I personally use packr-all.jar, a tool developed by the libgdx team. Example:
- java -jar packr-all.jar --platform windows64 --jdk jdk/ --useZgcIfSupportedOs --executable javarpg --classpath app/build/libs/javarpg.jar --main class rpg.App --output out-win

Note that I use the Liberica jdk/jre because they already ship JavaFX

## Features so far
- Questing system including editable XML-based quests
- Dialog boxes
- Basic enemy AI
- Item pickups such as health potions
- Enemy and player attacks
- Villagers
- Portals (teleporters between levels)
- Music (MIDI)
- Custom tile-based level format and loader
- Basic status bar

I will work to improve on these features, but no new features are on the roadmap except for game saves and the main menu. For the moment, I'll try to actually make the game with what I have, and see if I need to implement anything else along the way.

## Sample gameplay (still with the place-holder assets)

[![](https://markdown-videos-api.jorgenkh.no/youtube/k-vtR7d79Q0)](https://youtu.be/k-vtR7d79Q0)
