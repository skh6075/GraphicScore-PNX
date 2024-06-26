package me.skh6075.pnx.graphicscore.api

import cn.nukkit.Player
import cn.nukkit.network.protocol.RemoveObjectivePacket
import cn.nukkit.network.protocol.SetDisplayObjectivePacket
import cn.nukkit.network.protocol.SetScorePacket
import cn.nukkit.scoreboard.data.DisplaySlot
import cn.nukkit.scoreboard.data.SortOrder
import java.util.UUID

object ScoreFactory {
    private const val OBJECTIVE_ID = "objective"
    private const val MIN_LINE = 0
    private const val MAX_LINE = 15

    private val scoreboards: MutableMap<UUID, MutableMap<Int, String>> = mutableMapOf()

    fun sendScore(player: Player, displayName: String) {
        if (!player.isConnected) {
            return
        }
        if (hasScore(player)) {
            removeToPlayer(player)
        }

        scoreboards[player.uniqueId] = mutableMapOf(0 to OBJECTIVE_ID)

        player.session.sendPacket(SetDisplayObjectivePacket(DisplaySlot.SIDEBAR, OBJECTIVE_ID, displayName, "dummy", SortOrder.ASCENDING))
    }

    fun setLine(player: Player, line: Int, message: String) {
        if (!player.isConnected) {
            return
        }
        if (!hasScore(player)) {
            return
        }
        val rematchedLine = line + 1
        if (isNotLineValid(rematchedLine)) {
            return
        }

        val entry = SetScorePacket.ScoreInfo(rematchedLine.toLong(), OBJECTIVE_ID, rematchedLine, message)

        val pk = SetScorePacket()
        pk.action = SetScorePacket.Action.SET
        pk.infos.add(entry)

        scoreboards[player.uniqueId]!![rematchedLine] = message

        player.session.sendPacket(pk)
    }

    fun setLines(player: Player, lines: MutableMap<Int, String>) {
        lines.forEach { (line, message) -> setLine(player, line, message) }
    }

    fun getLine(player: Player, line: Int): String {
        if (!player.isConnected || !hasScore(player)) {
            return ""
        }

        return getScore(player).getOrDefault(line + 1, "")
    }

    fun editLine(player: Player, line: Int, message: String) {
        val rematchedLine = line + 1
        if (!player.isConnected || !hasScore(player) || isNotLineValid(rematchedLine)) {
            return
        }
        removeLine(player, rematchedLine)

        val pk = SetScorePacket()
        pk.action = SetScorePacket.Action.SET
        pk.infos.add(SetScorePacket.ScoreInfo(rematchedLine.toLong(), OBJECTIVE_ID, rematchedLine, message))
        player.session.sendPacket(pk)

        scoreboards[player.uniqueId]!![rematchedLine] = message
    }

    fun removeLine(player: Player, line: Int) {
        val rematchedLine = line + 1
        if (!player.isConnected || !getScore(player).containsKey(rematchedLine)) {
            return
        }

        val pk = SetScorePacket()
        pk.action = SetScorePacket.Action.REMOVE
        pk.infos.add(SetScorePacket.ScoreInfo(rematchedLine.toLong(), OBJECTIVE_ID, rematchedLine, getLine(player, rematchedLine)))

        player.session.sendPacket(pk)
    }

    fun removeToPlayer(player: Player) {
        if (!player.isConnected || hasScore(player)) {
            val objectiveName = scoreboards[player.uniqueId]?.get(0) ?: OBJECTIVE_ID
            player.session.sendPacket(RemoveObjectivePacket(objectiveName))

            scoreboards.remove(player.uniqueId)
        }
    }

    fun getScore(player: Player): MutableMap<Int, String> = scoreboards.getOrDefault(player.uniqueId, mutableMapOf())

    private fun hasScore(player: Player): Boolean = scoreboards.contains(player.uniqueId)

    private fun isNotLineValid(line: Int): Boolean = line < MIN_LINE || line > MAX_LINE
}