package graphicscore;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import graphicscore.placeholder.PlaceholderAPI;
import graphicscore.placeholder.defaults.GraphicExtension;
import graphicscore.player.ScorePlayerManager;
import graphicscore.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class    GraphicScore extends PluginBase implements Listener {
    private static GraphicScore instance;
    private static Scoreboard defaultBoard;
    private static final Map<String, Scoreboard> boards = new HashMap<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Config config = getConfig();
        for (String boardName : config.getSection("boards").getKeys(false)) {
        Scoreboard scoreboard = new Scoreboard(
            boardName,
            config.getString("boards." + boardName + ".title"),
            config.getStringList("boards." + boardName + ".lines")
        );
        boards.put(boardName, scoreboard);
    }
        String defaultBoardName = config.getString("default-board");
        Scoreboard findDefaultBoard = boards.get(defaultBoardName);
        if (findDefaultBoard == null) {
            getServer().getPluginManager().disablePlugin(this);
            getLogger().warning("No scoreboard defined with name " + defaultBoardName + " found.");
            return;
        }
        defaultBoard = findDefaultBoard;

        PlaceholderAPI.register(new GraphicExtension(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        ScorePlayerManager.addPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        ScorePlayerManager.removePlayer(event.getPlayer());
    }

    public static Scoreboard getDefaultBoard() {
        return defaultBoard;
    }

    public Scoreboard getBoard(String name) {
        return boards.get(name);
    }

    public static GraphicScore getInstance() {
        return instance;
    }
}


