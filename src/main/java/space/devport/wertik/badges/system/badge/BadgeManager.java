package space.devport.wertik.badges.system.badge;

import lombok.extern.java.Log;
import space.devport.dock.configuration.Configuration;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.badge.struct.Badge;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log
public class BadgeManager {

    private final BadgePlugin plugin;

    private final Map<String, Badge> loadedBadges = new LinkedHashMap<>();

    private final Configuration configuration;

    public BadgeManager(BadgePlugin plugin) {
        this.plugin = plugin;
        this.configuration = new Configuration(plugin, "badges");
    }

    public Badge getBadge(String name) {
        return this.loadedBadges.get(name);
    }

    public void load() {
        this.loadedBadges.clear();
        this.configuration.load();

        for (String name : configuration.getFileConfiguration().getKeys(false)) {
            Badge badge = Badge.from(configuration, name);

            if (badge == null)
                continue;

            this.loadedBadges.put(name, badge);
            log.fine("Loaded badge " + name);
        }
        log.info("Loaded " + this.loadedBadges.size() + " badge(s)...");
    }

    public Set<Badge> getBadges(Predicate<Badge> condition) {
        return this.loadedBadges.values().stream().filter(condition).collect(Collectors.toSet());
    }

    public Map<String, Badge> getLoadedBadges() {
        return Collections.unmodifiableMap(loadedBadges);
    }
}