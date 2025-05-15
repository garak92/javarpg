# Java RPG 

Warning: WIP in early stages.

The purpose of this project is to build a bullet hell 2d game with some RPG mechanics using pure Java. You'll explore 20+ zones in a post-apocalyptic world, completing quests, gaining new skills, and defeating bosses, in a quest to bring progress to your village using the ancient world's knowledge and technology.

Features:

- [ x ] Moddable XML-based questing system
- [ x ] MIDI music system (might be replaced with mp3)
- [ x ] Enemies, items, power-ups, static turrets, and environment (rocks, trees, etc.)
- [ x ] Command console for debugging purposes
- [ x ] Moddable tile level format
- [ ] Saving system
- [ ] Main menu
- [ ] Player skills tied to experience points
- [ ] Original soundtrack
- [ ] Boss battles

## How to run 

- Clone the repo
- Run ./gradlew run

## Packaging

I personally use packr-all.jar, a tool developed by the libgdx team. Example:
- java -jar packr-all.jar --platform windows64 --jdk jdk/ --jre jre/ --useZgcIfSupportedOs --executable javarpg --classpath app/build/libs/javarpg.jar --main class rpg.App --output out-win

Note that I use the Liberica jdk/jre because they already ship JavaFX

## Controls

Controls are for now statically fixed:
- Arrows for movement
- "E" to interact with things and NPCs
- "R" for shooting an ice ball
- "C" for opening the command console

## Assets
- Player, Igrenne, enemies, items, trees, rocks, and bushes are FREE assets taken from craftpix.net (https://craftpix.net/file-licenses/)
- Laser beams by Bonsaiheldin (https://opengameart.org/content/sci-fi-space-simple-bullets) CC0 1.0 Universal 
- Laser cannon by Nido (https://opengameart.org/content/tower-defence-basic-towers) CC0 1.0 Universal
- Photo realistic tile set by Screaming Brain Studios (https://opengameart.org/content/photorealistic-texture-pack-3) CC0 1.0 Universal
- Scroll UI container used as player status bar by Chad Wolfe (https://opengameart.org/content/scroll-ui-container) CC0 1.0 Universal

## Gameplay demo video
[![15 May 2025](https://img.youtube.com/vi/jmEx8VBgCNE/0.jpg)](https://www.youtube.com/watch?v=jmEx8VBgCNE)
