package me.skh6075.pnx.graphicscore.placeholder

import cn.nukkit.Player
import cn.nukkit.Server

object PlaceholderAPI {
    private val extensions: MutableMap<String, PlaceholderExtension> = mutableMapOf()

    fun register(extension: PlaceholderExtension) {
        extensions[extension.getIdentifier()] = extension
        Server.getInstance().pluginManager.registerEvents(extension, extension.getOwnedPlugin())
    }

    fun setPlaceholders(player: Player, text: String) : String {
        var result = text
        splitPlaceholders(text).forEach { find ->
            val split: List<String> = find.split(":")
            val extension: PlaceholderExtension? = extensions[split[0]]
            if (extension !== null) {
                result = result.replace("%$find%", extension.onRequest(player, split[1]) ?: "")
            }
        }

        return result
    }

    fun splitPlaceholders(text: String) : List<String> {
        return "%(.*?)%".toRegex().findAll(text).map { it.groupValues[1] }.toList()
    }
}