package me.skh6075.pnx.graphicscore.player

import cn.nukkit.Player
import me.skh6075.pnx.graphicscore.api.ScoreFactory
import me.skh6075.pnx.graphicscore.placeholder.PlaceholderAPI
import me.skh6075.pnx.graphicscore.scoreboard.Scoreboard

class ScorePlayer(
    private val player: Player,
    private var scoreboard: Scoreboard
) {
    init {
        refresh()
    }

    fun update(params: String) {
        val line = scoreboard.getPlaceholderParamsLine(params) ?: return
        ScoreFactory.editLine(player, line, PlaceholderAPI.setPlaceholders(player, scoreboard.lines[line]))
    }

    private fun refresh() {
        val lines: MutableMap<Int, String> = mutableMapOf()
        scoreboard.lines.forEachIndexed { index, line ->
            lines[index] = PlaceholderAPI.setPlaceholders(player, line)
        }

        ScoreFactory.sendScore(player, scoreboard.title)
        ScoreFactory.setLines(player, lines)
    }

    fun changeScoreboard(scoreboard: Scoreboard) {
        this.scoreboard = scoreboard
        ScoreFactory.removeToPlayer(player)
        refresh()
    }
}