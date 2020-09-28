package space.devport.wertik.badges;

import lombok.Getter;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.wertik.badges.commands.BadgeCommand;
import space.devport.wertik.badges.commands.CommandParser;
import space.devport.wertik.badges.commands.subcommands.AddSubCommand;
import space.devport.wertik.badges.commands.subcommands.ArchiveSubCommand;
import space.devport.wertik.badges.commands.subcommands.CollectionSubCommand;
import space.devport.wertik.badges.commands.subcommands.ListSubCommand;
import space.devport.wertik.badges.commands.subcommands.PurgeInvalidSubCommand;
import space.devport.wertik.badges.commands.subcommands.ReloadSubCommand;
import space.devport.wertik.badges.commands.subcommands.RemoveSubCommand;
import space.devport.wertik.badges.system.GsonHelper;
import space.devport.wertik.badges.system.badge.BadgeManager;
import space.devport.wertik.badges.system.user.UserManager;

import java.time.format.DateTimeFormatter;


public class BadgePlugin extends DevportPlugin {

    @Getter
    private BadgeManager badgeManager;

    @Getter
    private UserManager userManager;

    @Getter
    private CommandParser commandParser;

    @Getter
    private GsonHelper gsonHelper;

    @Getter
    private DateTimeFormatter dateFormat;

    public static BadgePlugin getInstance() {
        return getPlugin(BadgePlugin.class);
    }

    @Override
    public void onPluginEnable() {

        this.gsonHelper = new GsonHelper(this);

        userManager = new UserManager(this);
        badgeManager = new BadgeManager(this);

        loadOptions();

        badgeManager.load();
        userManager.load();

        new BadgeLanguage(this);

        this.commandParser = new CommandParser(this);

        addMainCommand(new BadgeCommand())
                .addSubCommand(new ReloadSubCommand(this))
                .addSubCommand(new CollectionSubCommand(this))
                .addSubCommand(new AddSubCommand(this))
                .addSubCommand(new RemoveSubCommand(this))
                .addSubCommand(new PurgeInvalidSubCommand(this))
                .addSubCommand(new ListSubCommand(this))
                .addSubCommand(new ArchiveSubCommand(this));
    }

    private void loadOptions() {
        this.dateFormat = DateTimeFormatter.ofPattern(configuration.getString("formats.date", "MM/dd/yyyy HH:mm:ss"));
    }

    @Override
    public void onPluginDisable() {
        userManager.save();
    }

    @Override
    public void onReload() {
        loadOptions();
        badgeManager.load();
    }

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[]{UsageFlag.CONFIGURATION, UsageFlag.LANGUAGE, UsageFlag.COMMANDS, UsageFlag.CUSTOMISATION, UsageFlag.MENUS};
    }
}
