package me.skh6075.pnx.graphicscore.placeholder.defaults

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.player.PlayerItemHeldEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerQuitEvent
import cn.nukkit.plugin.Plugin
import me.skh6075.pnx.graphicscore.placeholder.PlaceholderExtension
import me.skh6075.pnx.graphicscore.player.ScorePlayerManager

class GraphicExtension(private val ownedPlugin: Plugin) : PlaceholderExtension() {
    override fun getOwnedPlugin(): Plugin = ownedPlugin

    override fun getIdentifier(): String = "graphic"

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        onPlaceholderUpdate(event.player)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        onPlaceholderUpdate(event.player)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerItemHeldEvent(event: PlayerItemHeldEvent) {
        onPlaceholderUpdate(event.player)
    }

    private fun onPlaceholderUpdate(player: Player) {
        val session = ScorePlayerManager.getPlayer(player) ?: return
        session.update("graphic:player_name")
        session.update("graphic:held_item_name")
        session.update("graphic:held_item_count")
        session.update("graphic:online")
        session.update("graphic:online_max")
    }

    override fun onRequest(player: Player, params: String): String? {
        return when(params) {
            "player_name" -> player.name
            "held_item_name" -> player.inventory.itemInHand.displayName
            "held_item_count" -> player.inventory.itemInHand.count.toString()
            "online" -> Server.getInstance().onlinePlayers.size.toString()
            "online_max" -> Server.getInstance().maxPlayers.toString()
            else -> null
        }
    }
}