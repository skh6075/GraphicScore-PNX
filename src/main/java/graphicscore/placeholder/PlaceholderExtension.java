package graphicscore.placeholder;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.Plugin;

public abstract class PlaceholderExtension implements Listener {
    public abstract Plugin getOwnedPlugin();

    public abstract String getIdentifier();

    public abstract String onRequest(Player player, String params);
}