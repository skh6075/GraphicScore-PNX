package graphicscore.player;

import cn.nukkit.Player;
import graphicscore.GraphicScore;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class ScorePlayerManager {
    private static final Map<UUID, ScorePlayer> playerMap = new HashMap<>();

    public static ScorePlayer addPlayer(Player player) {
        ScorePlayer session = new ScorePlayer(player, GraphicScore.getDefaultBoard());
        playerMap.put(player.getUniqueId(), session);
        return session;
    }

    public static ScorePlayer getPlayer(Player player) {
        return playerMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        playerMap.remove(player.getUniqueId());
    }
}

