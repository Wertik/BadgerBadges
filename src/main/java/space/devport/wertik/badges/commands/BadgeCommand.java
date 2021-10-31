package space.devport.wertik.badges.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.commands.MainCommand;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.subcommands.*;

public class BadgeCommand extends MainCommand {

    public BadgeCommand(BadgePlugin plugin) {
        super(plugin, "badgerbadges");

        withSubCommand(new ReloadSubCommand(plugin));
        withSubCommand(new CollectionSubCommand(plugin));
        withSubCommand(new AddSubCommand(plugin));
        withSubCommand(new AddAllCommand(plugin));
        withSubCommand(new RemoveSubCommand(plugin));
        withSubCommand(new PurgeInvalidSubCommand(plugin));
        withSubCommand(new ListSubCommand(plugin));
        withSubCommand(new ArchiveSubCommand(plugin));
        withSubCommand(new ListBadgesSubCommand(plugin));
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
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