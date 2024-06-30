package graphicscore.scoreboard;

import graphicscore.placeholder.PlaceholderAPI;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Scoreboard {
    private final String name;
    private final String title;
    private final List<String> lines;
    private final Map<Integer, List<String>> placeholders;
    private final Map<String, Integer> placeholderRegistryBindMap;

    public Scoreboard(String name, String title, List<String> lines) {
        this.name = name;
        this.title = title;
        this.lines = lines;
        this.placeholders = new HashMap<>();
        this.placeholderRegistryBindMap = new HashMap<>();

        for (int index = 0; index < lines.size(); index++) {
            String text = lines.get(index);
            List<String> params = PlaceholderAPI.splitPlaceholders(text);
            if (!params.isEmpty()) {
                placeholders.put(index, params);
                for (String param : params) {
                    placeholderRegistryBindMap.put(param, index);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLines() {
        return lines;
    }

    public Integer getPlaceholderParamsLine(String params) {
        return placeholderRegistryBindMap.get(params);
    }
}


