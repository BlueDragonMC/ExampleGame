# ExampleGame
An example game built using BlueDragon's common [library](https://github.com/BlueDragonMC/Server).

For detailed documentation along with a sample implementation, see [`ExampleGame.kt`](./src/main/kotlin/com/bluedragonmc/games/examplegame/ExampleGame.kt).

## Concepts
Each game is represented by a single class that extends [`com.bluedragonmc.server.Game`](https://github.com/BlueDragonMC/Server/blob/main/common/src/main/kotlin/com/bluedragonmc/server/Game.kt).
### Modules
When the class is initialized, it can load "modules" — small, reusable segments of code that handle events.

There are many common features of BlueDragon minigames implemented as modules. Some examples include:
- the countdown at the start of the game
- implementing vanilla Minecraft combat mechanics
- allowing players to spectate
- sending players a chat message when they join the game
- creating GUIs, like shops or forms of user input
- loading maps from a file or a database

The advantage of a modular system is that games can have shared features without having to reuse code. The best example of this is the countdown at the beginning of the game, which is implemented as a module. Most of BlueDragon's games have this, but it was only implemented once.
### Events
Every module gets its own EventNode, which is registered as a child of its parent Game's EventNode.
This EventNode receives events based on two main rules:
- Events that inherit `PlayerEvent` are received if the subject player is in the game
- Events that inherit `InstanceEvent` are received if the subject instance is the same as the game's instance (see [`InstanceModule`](https://github.com/BlueDragonMC/Server/blob/main/common/src/main/kotlin/com/bluedragonmc/server/module/instance/InstanceModule.kt))

*Other event filtering rules can be seen [here](https://github.com/BlueDragonMC/Server/blob/1d40e7c466d4719b245f298d0bba9d047e232022/common/src/main/kotlin/com/bluedragonmc/server/Game.kt#L93-L116), but they should be regarded as implementation details and not relied on.

*For more information about how Minestom's event system works, see [their wiki](https://wiki.minestom.net/feature/events).*

### Plugin Loader System
BlueDragon's Minestom implementation loads all JAR files in the `games` folder in its current working directory on startup.
There must be a [`game.properties`](./src/main/resources/game.properties) file at the root of each JAR, which specifies the name of the game and its main class.

*The "main class" is the fully-qualified name of the class in the JAR which extends `com.bluedragonmc.server.Game`. There should only be one per game plugin.*

### Using another server software or programming language
To integrate another server software with BlueDragon's queue, party, messaging, and player tracking systems, follow the [Implementation Guide](https://github.com/BlueDragonMC/Server/blob/main/INTEGRATION.md) in the `Server` repository.