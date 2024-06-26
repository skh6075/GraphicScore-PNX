package me.skh6075.pnx.graphicscore.player

import cn.nukkit.Player
import me.skh6075.pnx.graphicscore.GraphicScore
import java.util.UUID

object ScorePlayerManager {
    private val playerMap: MutableMap<UUID, ScorePlayer> = mutableMapOf()

    fun addPlayer(player: Player) : ScorePlayer {
        val session = ScorePlayer(player, GraphicScore.defaultBoard)
        playerMap[player.uniqueId] = session

        return session
    }

    fun getPlayer(player: Player) : ScorePlayer? {
        return playerMap[player.uniqueId]
    }

    fun removePlayer(player: Player) {
        playerMap.remove(player.uniqueId)
    }
}