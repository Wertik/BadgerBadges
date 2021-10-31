package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;

public class ReloadSubCommand extends BadgeSubCommand {

    public ReloadSubCommand(BadgePlugin plugin) {
        super("reload", plugin);
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        plugin.reload(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% reload";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "Reload the plugin.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}
