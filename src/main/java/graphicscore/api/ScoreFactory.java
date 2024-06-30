
package graphicscore.api;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.RemoveObjectivePacket;
import cn.nukkit.network.protocol.SetDisplayObjectivePacket;
import cn.nukkit.network.protocol.SetScorePacket;
import cn.nukkit.scoreboard.data.DisplaySlot;
import cn.nukkit.scoreboard.data.SortOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreFactory {
    private static final String OBJECTIVE_ID = "objective";
    private static final int MIN_LINE = 0;
    private static final int MAX_LINE = 15;

    private static final Map<UUID, Map<Integer, String>> scoreboards = new HashMap<>();

    public static void sendScore(Player player, String displayName) {
        if (!player.isConnected()) {
            return;
        }
        if (hasScore(player)) {
            removeToPlayer(player);
        }

        Map<Integer, String> playerScoreboard = new HashMap<>();
        playerScoreboard.put(0, OBJECTIVE_ID);
        scoreboards.put(player.getUniqueId(), playerScoreboard);

        SetDisplayObjectivePacket packet = new SetDisplayObjectivePacket();
        packet.displaySlot = DisplaySlot.SIDEBAR;
        packet.objectiveName = OBJECTIVE_ID;
        packet.displayName = displayName;
        packet.criteriaName = "dummy";
        packet.sortOrder = SortOrder.ASCENDING;
        player.getSession().sendPacket(packet);
    }

    public static void setLine(Player player, int line, String message) {
        if (!player.isConnected() || !hasScore(player)) {
            return;
        }
        int rematchedLine = line + 1;
        if (isNotLineValid(rematchedLine)) {
            return;
        }

        SetScorePacket.ScoreInfo entry = new SetScorePacket.ScoreInfo(rematchedLine, OBJECTIVE_ID, rematchedLine, message);

        SetScorePacket pk = new SetScorePacket();
        pk.action = SetScorePacket.Action.SET;
        pk.infos.add(entry);

        scoreboards.get(player.getUniqueId()).put(rematchedLine, message);

        player.getSession().sendPacket(pk);
    }

    public static void setLines(Player player, Map<Integer, String> lines) {
        lines.forEach((line, message) -> setLine(player, line, message));
    }

    public static String getLine(Player player, int line) {
        if (!player.isConnected() || !hasScore(player)) {
            return "";
        }

        return scoreboards.get(player.getUniqueId()).getOrDefault(line + 1, "");
    }

    public static void editLine(Player player, int line, String message) {
        int rematchedLine = line + 1;
        if (!player.isConnected() || !hasScore(player) || isNotLineValid(rematchedLine)) {
            return;
        }
        removeLine(player, rematchedLine);

        SetScorePacket pk = new SetScorePacket();
        pk.action = SetScorePacket.Action.SET;
        pk.infos.add(new SetScorePacket.ScoreInfo(rematchedLine, OBJECTIVE_ID, rematchedLine, message));
        player.getSession().sendPacket(pk);

        scoreboards.get(player.getUniqueId()).put(rematchedLine, message);
    }

    public static void removeLine(Player player, int line) {
        int rematchedLine = line + 1;
        if (!player.isConnected() || !scoreboards.get(player.getUniqueId()).containsKey(rematchedLine)) {
            return;
        }

        SetScorePacket pk = new SetScorePacket();
        pk.action = SetScorePacket.Action.REMOVE;
        pk.infos.add(new SetScorePacket.ScoreInfo(rematchedLine, OBJECTIVE_ID, rematchedLine, getLine(player, rematchedLine)));

        player.getSession().sendPacket(pk);
    }

    public static void removeToPlayer(Player player) {
        if (!player.isConnected() || hasScore(player)) {
            String objectiveName = scoreboards.get(player.getUniqueId()).get(0);
            RemoveObjectivePacket packet = new RemoveObjectivePacket();
            packet.objectiveName = objectiveName;
            player.getSession().sendPacket(packet);

            scoreboards.remove(player.getUniqueId());
        }
    }

    public static Map<Integer, String> getScore(Player player) {
        return scoreboards.getOrDefault(player.getUniqueId(), new HashMap<>());
    }

    private static boolean hasScore(Player player) {
        return scoreboards.containsKey(player.getUniqueId());
    }

    private static boolean isNotLineValid(int line) {
        return line < MIN_LINE || line > MAX_LINE;
    }
}
