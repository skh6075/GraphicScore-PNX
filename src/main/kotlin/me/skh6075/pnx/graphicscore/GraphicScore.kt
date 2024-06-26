package me.skh6075.pnx.graphicscore

import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerQuitEvent
import cn.nukkit.plugin.PluginBase
import me.skh6075.pnx.graphicscore.placeholder.PlaceholderAPI
import me.skh6075.pnx.graphicscore.placeholder.defaults.GraphicExtension
import me.skh6075.pnx.graphicscore.player.ScorePlayerManager
import me.skh6075.pnx.graphicscore.scoreboard.Scoreboard

class GraphicScore : PluginBase(), Listener {
    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        saveDefaultConfig()
        config.getSection("boards").forEach { (boardName, _) ->
            val scoreboard = Scoreboard(
                name = boardName,
                title = config.getString("boards.$boardName.title"),
                lines = config.getStringList("boards.$boardName.lines")
            )
            boards[boardName] = scoreboard
        }
        val defaultBoardName = config.getString("default-board")
        val findDefaultBoard = boards[defaultBoardName]
        if (findDefaultBoard == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("No scoreboard defined with name $defaultBoardName found.")
            return
        }
        defaultBoard = findDefaultBoard

        PlaceholderAPI.register(GraphicExtension(this))

        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        ScorePlayerManager.addPlayer(event.player)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        ScorePlayerManager.removePlayer(event.player)
    }

    fun getDefaultBoard(): Scoreboard = defaultBoard

    fun getBoard(name: String): Scoreboard? = boards[name]

    companion object {
        private lateinit var instance: GraphicScore

        lateinit var defaultBoard: Scoreboard

        private val boards: MutableMap<String, Scoreboard> = mutableMapOf()

        fun getInstance(): GraphicScore = instance
    }
}