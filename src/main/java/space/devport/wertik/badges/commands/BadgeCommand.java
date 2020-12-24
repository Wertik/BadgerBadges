package space.devport.wertik.badges.commands;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.subcommands.*;

public class BadgeCommand extends MainCommand {

    public BadgeCommand(BadgePlugin plugin) {
        super("badgerbadges");

        addSubCommand(new ReloadSubCommand(plugin));
        addSubCommand(new CollectionSubCommand(plugin));
        addSubCommand(new AddSubCommand(plugin));
        addSubCommand(new AddAllCommand(plugin));
        addSubCommand(new RemoveSubCommand(plugin));
        addSubCommand(new PurgeInvalidSubCommand(plugin));
        addSubCommand(new ListSubCommand(plugin));
        addSubCommand(new ArchiveSubCommand(plugin));
        addSubCommand(new ListBadgesSubCommand(plugin));
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        return super.perform(sender, label, args);
    }

    @Override
    public String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public String getDefaultDescription() {
        return "Displays this.";
    }
}