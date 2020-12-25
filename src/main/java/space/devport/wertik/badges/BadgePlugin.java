package space.devport.wertik.badges;

import lombok.Getter;
import org.bukkit.entity.Player;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.wertik.badges.commands.BadgeCommand;
import space.devport.wertik.badges.commands.CommandParser;
import space.devport.wertik.badges.system.badge.BadgeManager;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.UserManager;
import space.devport.wertik.badges.system.user.struct.CollectedBadge;
import space.devport.wertik.badges.system.user.struct.User;

import java.time.format.DateTimeFormatter;


public class BadgePlugin extends DevportPlugin {

    @Getter
    private BadgeManager badgeManager;

    @Getter
    private UserManager userManager;

    @Getter
    private CommandParser commandParser;

    @Getter
    private DateTimeFormatter dateFormat;

    public static BadgePlugin getInstance() {
        return getPlugin(BadgePlugin.class);
    }

    @Override
    public void onPluginEnable() {

        // Add dynamic parsing based on context to global placeholders.
        this.getGlobalPlaceholders()
                .addDynamicPlaceholder("%collectedDate%", CollectedBadge::getDateFormatted, CollectedBadge.class)
                .addDynamicPlaceholder("%collectedName%", CollectedBadge::getBadgeName, CollectedBadge.class)
                .addDynamicPlaceholder("%collectedDisplayName%", b -> b.getBadge().getDisplayName(), CollectedBadge.class)
                .addDynamicPlaceholder("%collectedCount%", p -> {
                    User user = BadgePlugin.getInstance().getUserManager().getOrCreateUser(p.getUniqueId());
                    return String.valueOf(user.getCollectedBadges().size());
                }, Player.class)
                .addDynamicPlaceholder("%badgeName%", Badge::getName, Badge.class)
                .addDynamicPlaceholder("%badgeDisplayName%", Badge::getDisplayName, Badge.class);

        userManager = new UserManager(this);
        badgeManager = new BadgeManager(this);

        loadOptions();

        badgeManager.load();
        userManager.load();

        new BadgeLanguage(this);

        this.commandParser = new CommandParser(this);

        addMainCommand(new BadgeCommand(this));

        userManager.startAutoSave();
    }

    private void loadOptions() {
        this.dateFormat = DateTimeFormatter.ofPattern(configuration.getString("formats.date", "MM/dd/yyyy HH:mm:ss"));
    }

    @Override
    public void onPluginDisable() {
        userManager.stopAutoSave();
        userManager.save();
    }

    @Override
    public void onReload() {
        loadOptions();
        badgeManager.load();

        userManager.reloadAutoSave();
    }

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[]{UsageFlag.CONFIGURATION, UsageFlag.LANGUAGE, UsageFlag.COMMANDS, UsageFlag.CUSTOMISATION, UsageFlag.MENUS};
    }
}
