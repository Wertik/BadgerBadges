package space.devport.wertik.badges.system.badge.struct;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.bukkit.configuration.ConfigurationSection;
import space.devport.dock.CustomisationManager;
import space.devport.dock.configuration.Configuration;
import space.devport.dock.item.ItemPrefab;
import space.devport.dock.item.impl.PrefabFactory;
import space.devport.wertik.badges.BadgePlugin;

@Log
public class Badge {

    @Getter
    private final String name;

    @Getter
    @Setter
    private String displayName;

    @Getter
    @Setter
    private ItemPrefab displayItem;

    @Getter
    @Setter
    private ItemPrefab notCollectedItem;

    public Badge(String name) {
        this.name = name;
    }

    public static Badge from(Configuration configuration, String path) {
        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            log.severe("Could not load Badge at " + configuration.getFile().getName() + "@" + path + ", section is invalid.");
            return null;
        }

        String name = section.getName();

        Badge badge = new Badge(name);

        String displayName = section.getString("display-name", name);

        badge.setDisplayName(displayName);

        ItemPrefab defaultDisplay = BadgePlugin.getInstance().getManager(CustomisationManager.class).getItem("default-badge-display");
        ItemPrefab displayItem = configuration.getItem(path + ".display-item", defaultDisplay);

        badge.setDisplayItem(PrefabFactory.of(displayItem));

        ItemPrefab defaultNotCollected = BadgePlugin.getInstance().getManager(CustomisationManager.class).getItem("default-not-collected-display");
        ItemPrefab notCollectedItem = configuration.getItem(path + ".not-collected", defaultNotCollected);

        badge.setNotCollectedItem(PrefabFactory.of(notCollectedItem));

        return badge;
    }
}