package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;

public class PurgeInvalidSubCommand extends BadgeSubCommand {

    public PurgeInvalidSubCommand(BadgePlugin plugin) {
        super("purgeinvalid", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        language.sendPrefixed(sender, "Commands.Purge-Invalid.Starting");
        plugin.getUserManager().purgeInvalid().thenAcceptAsync((count) ->
                language.getPrefixed("Commands.Purge-Invalid.Done")
                        .replace("%count%", count)
                        .send(sender));
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% purgeinvalid";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "Purge invalid badges from user data.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}