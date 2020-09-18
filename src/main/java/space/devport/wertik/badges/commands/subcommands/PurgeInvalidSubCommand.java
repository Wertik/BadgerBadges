package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;

public class PurgeInvalidSubCommand extends BadgeSubCommand {

    public PurgeInvalidSubCommand(BadgePlugin plugin) {
        super("purgeinvalid", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        //TODO
        sender.sendMessage(StringUtil.color("&7Starting..."));
        plugin.getUserManager().purgeInvalid().thenAcceptAsync((count) -> {
            //TODO
            sender.sendMessage(StringUtil.color("&7Removed &f%count% &7invalid badge references."
                    .replace("%count%", String.valueOf(count))));
        });
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