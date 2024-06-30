package graphicscore.placeholder.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.Plugin;
import graphicscore.placeholder.PlaceholderExtension;
import graphicscore.player.ScorePlayer;
import graphicscore.player.ScorePlayerManager;

public class GraphicExtension extends PlaceholderExtension {

    private final Plugin ownedPlugin;

    public GraphicExtension(Plugin ownedPlugin) {
        this.ownedPlugin = ownedPlugin;
    }

    @Override
    public Plugin getOwnedPlugin() {
        return ownedPlugin;
    }

    @Override
    public String getIdentifier() {
        return "graphic";
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        onPlaceholderUpdate(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        onPlaceholderUpdate(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        onPlaceholderUpdate(event.getPlayer());
    }

    private void onPlaceholderUpdate(Player player) {
        ScorePlayer session = ScorePlayerManager.getPlayer(player);
        if (session == null) return;
        session.update("graphic:player_name");
        session.update("graphic:held_item_name");
        session.update("graphic:held_item_count");
        session.update("graphic:online");
        session.update("graphic:online_max");
    }

    @Override
    public String onRequest(Player player, String params) {
        switch (params) {
            case "player_name":
                return player.getName();
            case "held_item_name":
                return player.getInventory().getItemInHand().getDisplayName();
            case "held_item_count":
                return String.valueOf(player.getInventory().getItemInHand().getCount());
            case "online":
                return String.valueOf(Server.getInstance().getOnlinePlayers().size());
            case "online_max":
                return String.valueOf(Server.getInstance().getMaxPlayers());
            default:
                return null;
        }
    }
}
