# Java RPG 

Warning: WIP in very early stages.

The purpose of this project is to build an RPG with a gameplay similar to WoW: level up by doing quests in the world in order to get into the end-game content, which will consist of dungeons. In principle, it will be single player, but that may change.

## How to run 

- Clone the repo
- Run ./gradlew run

## Packaging

I personally use packr-all.jar, a tool developed by the libgdx team. Example:
- java -jar packr-all.jar --platform windows64 --jdk jdk/ --jre jre/ --useZgcIfSupportedOs --executable javarpg --classpath app/build/libs/javarpg.jar --main class rpg.App --output out-win

Note that I use the Liberica jdk/jre because they already ship JavaFX

## Controls

Controls are for now fixed:
- Arrows for movement
- "E" to interact with things and NPCs
- "R" for shooting an ice ball

## Gameplay demo video
[![15 May 2025](https://img.youtube.com/vi/jmEx8VBgCNE/0.jpg)](https://www.youtube.com/watch?v=jmEx8VBgCNE)
