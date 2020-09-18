package space.devport.wertik.badges.system.badge.struct;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.CustomisationManager;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.badges.BadgePlugin;

public class Badge {

    @Getter
    private final String name;

    @Getter
    @Setter
    private String displayName;

    @Getter
    @Setter
    private ItemBuilder displayItem;

    @Getter
    @Setter
    private ItemBuilder notCollectedItem;

    public Badge(String name) {
        this.name = name;
    }

    public static Badge from(Configuration configuration, String path) {
        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            ConsoleOutput.getInstance().err("Could not load Badge at " + configuration.getFile().getName() + "@" + path + ", section is invalid.");
            return null;
        }

        String name = section.getName();

        Badge badge = new Badge(name);

        String displayName = section.getString("display-name", name);

        badge.setDisplayName(displayName);

        ItemBuilder defaultDisplay = BadgePlugin.getInstance().getManager(CustomisationManager.class).getItemBuilder("default-badge-display");
        ItemBuilder displayItem = configuration.getItemBuilder(path + ".display-item", defaultDisplay);

        badge.setDisplayItem(new ItemBuilder(displayItem));

        ItemBuilder defaultNotCollected = BadgePlugin.getInstance().getManager(CustomisationManager.class).getItemBuilder("default-not-collected-display");
        ItemBuilder notCollectedItem = configuration.getItemBuilder(path + ".not-collected", defaultNotCollected);

        badge.setNotCollectedItem(new ItemBuilder(notCollectedItem));

        return badge;
    }
}