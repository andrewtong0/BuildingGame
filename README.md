# BuildingGame

### Description

BuildingGame is a Minecraft Server Spigot plugin that allows server owners to play [Sethbling's Minecraft Building Game](https://www.youtube.com/watch?v=qBOHvRQ3orE) (Minecraft telephone pictionary).

### Features
- Automatic teleportation and prompt assignment
- Name hiding to anonymize the building/guessing process until the end of the game
- Automatic building plot generation, allowing games to be run in any world
- After each game, a chest with a written book is generated in each plot, logging what the build was, who built it, what the guess was, and the date of the game
- In-game timers and chat messages to help guide the game and remind players of their building prompts - no more using papers with anvils!

### Installation

The BuildingGame plugin requires [a server running Spigot](https://minecraft.fandom.com/wiki/Tutorials/Setting_up_a_Spigot_server) version 1.16.x (higher versions may or may not work, due to version API inconsistencies). From there, simply drag the `BuildingGame.jar` file into the `/plugins` folder in the server directory and reload/restart the server.

### Usage

*Currently, the BuildingGame plugin only supports games with a predefined host who maintains the flow of the game.*

1. Designate a player as the game host (`/bghost`).
2. Have all participating players ready up (`/bgready`).
3. The host should move to a clear region of land for the building plots to be generated in, and once ready, run (`/bgstart`).
4. The host should advance the game (`/bgnext`), which will ask all players to enter an initial building prompt (`/bgprompt`).
5. Once all prompts have been added, the host again advances the game (`/bgnext`).
6. All players should then be teleported to their respective building cells, and be assigned a prompt, which appears above their inventory hotbar. The host can then start a timer (`/bgtimer`).
7. Once building is complete, the host should once again proceed the game (`/bgnext`).
8. All players should be teleported to buildings created by other players, and should guess what the build is (`/bgprompt`).
9. Repeat steps 6-8 until the game is complete. The host can then run the tour command to teleport players through the builds to explore how prompts transformed throughout the game (`/bgtour`).

Note: To play music during the game via the provided command, you can use `/bgmusic <song number`. It is recommended that ID `0` is reserved for the guess phase song, and `1`, `2`, and `3` for building rounds (played when the timer is started).

### Commands

- `/bghost`: Sets the host for the game.
- `/bgready`: Toggles a player's ready status.
- `/bgstart`: Generates the building plots from where the host is standing when the command is called, and teleports all players to the lobby.
- `/bgprompt <prompt>`: Used when setting the initial building prompt, or for specifying a guess for a build (replace `<prompt>` with the build prompt/guess). Prompts can be rewritten by calling the command again. The host will get a message in chat notifying them once all participating players have entered a prompt for a given round.
- `/bgnext`: To be called by the host, advances the game to the next phase.
- `/bgsettings <get,set> <new value>`: View or change any of the available game settings.
- `/bgtour`: Called at the end of the game by the host, teleports players through each build "idea" in sequential order. This will also print out information regarding builds once called (e.g. who built the plot, what the given prompt was, what the guess was, etc.).
- `/bgtimer <seconds?>`: Creates a timer at the top of all players' screens. Can be called with no specified time (i.e. `/bgtimer`), and will use the default number of seconds specified in the settings, or can be called with a specific number of seconds (e.g. `/bgtimer 120`) to start a timer with the specified number of seconds. Timers will disappear upon calling `/bgnext`.
- `/bgmusic <song ID>`: Plays music based on the specified song tied to the provided song ID.

### Game Settings
The BuildingGame plugin has several customizable settings that can be specified between games through the `/bgsettings` command. All settings are specified as integers.
- `buildTime`: Determines the default number of seconds a timer is created with `/bgtimer` when called with no specified number of seconds (recommended: 300).
- `buildAreaWidth`: Specifies the width of the building area of each building plot (i.e. the central build platform) in number of blocks (recommended: 20).
- `buildAreaLength`: Specifies the length of the building area of each building plot in number of blocks (recommended: 20).
- `buildAreaHeight`: Specifies the height of the walls surrounding each building plot in number of blocks (recommended: 30).
- `buildAreaPadding`: Specifies the size of the padding area surrounding the central build area (recommended: 5).
- `buildFirstPrompt`: Specifies whether or not players should build their own initial prompt - should be set to 1 with games of 4 or 5 players to maximize the number of building rounds (1 = On, 0 = Off).

### Game Flow
The game follows a DFA. It can be helpful to know the game flow as a host to understand which phase follows when calling `/bgnext`, however, messages in chat generated by the plugin should provide enough guidance.

- `INIT`: The game state after `/bgstart` is called.
    - `INIT` always leads into `INITPROMPT`
- `INITPROMPT`: The game state where players enter their initial prompts
    - `INITPROMPT` always leads into `BUILD`
- `BUILD`: The game state where players build their assigned prompts
    - `BUILD` always leads into `GUESS`
- `GUESS`: The game state where players guess what the build they are looking at is
    - `GUESS` can either lead into another `BUILD` round, or if it is the last round, leads into `ENDGAME`
- `ENDGAME`: The game state where the host can call `/bgtour` to navigate through the builds
    - `ENDGAME` is the final game state and does not lead into any other states

### Motivation

Sethbling's Building Game has certain limitations, such as requiring a custom game map to be loaded in, or requiring a minimum of 7 players to play. My group of friends that I generally played with was 4 people, which restricted us from using any of the map's automated features.

We got creative and reduced the number of building rounds, manually teleported players around, and coordinated to make the game work with our smaller group. However, this process was completely manual.

### Other Stuff
Some recommendations for ways to enhance your game experience.
- My group of friends created a "hall of fame" where we induct our favourite build each game by voting at the end of the game. We take a screenshot of the build, and use the [ImageOnMap](https://www.spigotmc.org/resources/imageonmap.26585/) plugin to transform the image to a map that we can place in an item frame, creating a virtual "museum".
- The game is not about guessing every prompt perfectly - some of the funniest moments come from inaccurate guesses. As a host, remind players that they should not feel stressed to get their builds perfect or for their guesses to be flawless.
- For the group that I host, I specify a custom resource pack in my `server.properties`, where I changed the `ward` music disc to play the Mario Kart countdown timer noise, accompanied by some custom music. Thus, this is played when `/bgadmin` is called. While not necessary, it adds some extra fun to the game, and can add nice background while players are building.

### Developer Notes

#### Updating Plugin on new Minecraft Version
New Minecraft versions may break plugin functionality. To update the plugin to the latest Minecraft build, use the following steps:
1. Open `pom.xml`.
2. Change the Spigot API dependency to the latest available matching version. This is found at the [Spigot snapshot repository](https://hub.spigotmc.org/nexus/content/repositories/snapshots/) under the `/repositories/snapshots/org/spigotmc/spigot-api` directory. You should copy the name of the relevant snapshot and replace the `<version>` tag content in the XML file.
3. Using `BUILDTOOLS`, acquire the latest Spigot jarfile and update the `<systempath>` tag to point to the newly acquired jarfile.
4. Clean, reinstall, and rebuild the plugin.
    - `mvn clean`
    - `mvn install`
    - `Build > Rebuild Project`
    - `Build > Build Artifacts... > Clean`
    - `Build > Build Artifacts... > Build`
