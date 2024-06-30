package graphicscore.placeholder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceholderAPI {
    private static final Map<String, PlaceholderExtension> extensions = new HashMap<>();

    public static void register(PlaceholderExtension extension) {
        extensions.put(extension.getIdentifier(), extension);
        Server.getInstance().getPluginManager().registerEvents(extension, extension.getOwnedPlugin());
    }

    public static String setPlaceholders(Player player, String text) {
        String result = text;
        for (String find : splitPlaceholders(text)) {
            String[] split = find.split(":");
            PlaceholderExtension extension = extensions.get(split[0]);
            if (extension != null) {
                result = result.replace("%" + find + "%", extension.onRequest(player, split[1]) != null ? extension.onRequest(player, split[1]) : "");
            }
        }
        return result;
    }

    public static List<String> splitPlaceholders(String text) {
        Pattern pattern = Pattern.compile("%(.+?)%");
        Matcher matcher = pattern.matcher(text);
        return matcher.results()
            .map(result -> result.group(1))
        .collect(Collectors.toList());
    }
}

