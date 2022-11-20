package com.bluedragonmc.games.examplegame

import com.bluedragonmc.server.Game
import com.bluedragonmc.server.module.DependsOn
import com.bluedragonmc.server.module.GameModule
import com.bluedragonmc.server.module.instance.SharedInstanceModule
import com.bluedragonmc.server.module.map.AnvilFileMapProviderModule
import com.bluedragonmc.server.module.minigame.CountdownModule
import com.bluedragonmc.server.module.minigame.VoidDeathModule
import com.bluedragonmc.server.module.minigame.WinModule
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.event.player.PlayerStartSneakingEvent
import java.nio.file.Paths

/**
 * This class represents a [Game]. Each game has a player list, a state (starting/waiting for players/in-game/ending),
 * and a set of modules which define its logic.
 */
class ExampleGame(mapName: String) : Game(name = "ExampleGame", mapName = mapName) {

    init {
        use(VoidDeathModule(threshold = 0.0)) // Players will instantly be killed when they're below y = 0.0
        use(CountdownModule(threshold = 2, countdownSeconds = 5)) // Starts a countdown when the game has 2 players, counting down from 5 seconds
        use(MyModule()) // When MyModule is used, its initialize method is called and its event node is added as a child to the game's event node.
        use(WinModule()) // The WinModule handles many common tasks related to declaring a winner for a game.
        use(AnvilFileMapProviderModule(Paths.get("worlds/$name/$mapName"))) // This module loads a map from a file path, but it does not define how instances should be created with it.
        use(SharedInstanceModule()) // This module uses the above module to load a world, then creates a SharedInstance for this game using the map.

        // If a module isn't necessary, you can use the handleEvent method.
        // This is useful for event handlers that should not be reused from one game to another.
        handleEvent<PlayerChatEvent> { event ->
            // Note: internally, using [handleEvent] uses a module so that this event listener can easily be unregistered at any time.
            event.player.exp += 10
        }

        // Games call the "ready" method to show that they've initialized successfully and are ready to receive players.
        // By default, if games are not ready within 15 seconds, they will be shut down and removed.
        ready()
    }

    /**
     * Modules can be created by extending the GameModule class.
     * They represent small, reusable segments of code that multiple games can use.
     * Examples of modules include:
     *  - the countdown at the start of the game
     *  - implementing vanilla Minecraft combat mechanics
     *  - allowing players to spectate
     *  - sending players a chat message when they join the game
     *  - creating GUIs, like shops or forms of user input
     *  - loading maps from a file or a database
     *
     * The "common" subproject in [BlueDragonMC/Server](https://github.com/BlueDragonMC/Server)
     * contains many simple, frequently-used modules.
     *
     * Modules can also interface with each other using the [com.bluedragonmc.server.ModuleHolder] API.
     * Each module has its own event node, which receives events for players and instances related to
     * the game which the module is a part of.
     *
     * Modules can specify dependencies with the @[DependsOn] annotation. Modules cannot be initialized
     * before all of their dependencies are initialized. Calling the [ready] method ensures all modules'
     * dependencies are met before allowing players to join the game.
     */
    @DependsOn(WinModule::class)
    class MyModule : GameModule() {

        override fun initialize(parent: Game, eventNode: EventNode<Event>) {
            eventNode.addListener(PlayerSpawnEvent::class.java) { event ->
                event.player.sendMessage(Component.text("Hello, world!", NamedTextColor.AQUA))
            }
            // When a player in the game starts sneaking, this event handler will fire.
            eventNode.addListener(PlayerStartSneakingEvent::class.java) { event ->
                // Game#getModule<T> returns another module from the parent game with type T.
                // The line below, for example, is getting the WinModule which was used earlier.
                parent.getModule<WinModule>().declareWinner(event.player)
            }
        }

        override fun deinitialize() {
            // The "deinitialize" method is called when a module is unregistered.
            // All modules are unregistered when a game ends.
            // Note: this method does not have to be implemented, and the default behavior is a no-op.
        }
    }
}