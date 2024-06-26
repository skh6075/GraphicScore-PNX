# GraphicScore-PNX
A plugin for adding Scoreboard to PowerNukkitX server, allowing users to handle it easily and conveniently in an event-driven manner.

## Features
The API is developed solely for packet processing without using the Player Scoreboard method provided by PowerNukkitX.
Also, instead of putting a load on the server by updating the scoreboard through Scheduler#Task, ```event-pnx``` + ```placeholder``` You can update your scoreboard in a low-impact and easy way.

## Usage

**NOTE:** How to set `config.yml`.
```yml
---
# Scoreboard name to be used as server default
default-board: default

boards:
  # Scoreboard name
  default:
    # Scoreboard title
    title: "§r§f GraphicScore PNX  §r"
    # Scoreboard lines
    lines:
      - "§r§f ◆ Name: %graphic:player_name% "
      - "§r§f ◆ Item: %graphic:held_item_name% (x%graphic:held_item_count%) "
      - "§r§f ◆ ONLINE: %graphic:online% / %graphic:online_max%"
  mcmmo:
    title: "§r§f GraphicScore - MCMMO  §r"
    lines:
      - "§r§f Job: %mcmmo:job_name% (Stage: %mcmmo:job_stage%) "
      - "§r§f Level: %mcmmo:job_level% (XP: %mcmmo:job_xp_per%) "
      - "§r§f Skill: %mcmmo:job_skill_name% (%mcmmo:job_skill_cooldown% left..)"
...
```

## PlaceholderAPI built into GraphicScore
Create an example by linking with external plugin data
```kotlin
import me.skh6075.pnx.graphicscore.placeholder.PlaceholderAPI
import me.skh6075.pnx.graphicscore.placeholder.PlaceholderExtension

class MCMMOPlugin : PluginBase() {
    override fun onEnable() {
        PlaceholderAPI.register(MCMMOPlaceholderExtension(this))
    }
}

class MCMMOPlaceholderExtension(private val plugin: MCMMOPlugin) : PlaceholderExtension() {
    override fun getOwnedPlugin(): Plugin = plugin

    override fun getIdentifier(): String = "mcmmo"

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerMCMMODataUpdateEvent(event : PlayerMCMMODataUpdateEvent) {
        val session = ScorePlayerManager.getPlayer(event.player) ?: return
        session.update("mcmmo:job_name")
        session.update("mcmmo:job_stage")
        session.update("mcmmo:job_level")
        session.update("mcmmo:job_xp_per")
        session.update("mcmmo:job_skill_name")
        session.update("mcmmo:job_skill_cooldown")
    }

    override fun onRequest(player: Player, params: String): String? {
        return when(params) {
            "job_name" -> API.getPlayerJob(player)
            "job_stage" -> API.getPlayerJobStage(player).toString()
            "job_level" -> API.getPlayerJobLevel(player).toString()
            "job_xp_per" -> API.getPlayerJobXpPercentage(player).toString()
            "job_skill_name" -> API.getPlayerJobSkill(player).name
            "job_skill_cooldown" -> API.getPlayerJobSkill(player).leftCooldown
            else -> null
        }
    }
}
```
