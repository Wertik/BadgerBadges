package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

public class AddSubCommand extends BadgeSubCommand {

    public AddSubCommand(BadgePlugin plugin) {
        super("add", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args);

        if (target == null)
            return CommandResult.NO_CONSOLE;

        Badge badge = plugin.getBadgeManager().getBadge(args[0]);

        if (badge == null) {
            //TODO
            sender.sendMessage(StringUtil.color("&cInvalid badge."));
            return CommandResult.FAILURE;
        }

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());
        user.addBadge(badge);
        //TODO
        sender.sendMessage(StringUtil.color("&7Added the badge."));
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% add <badge> (player)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "Add a badge for a player.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(1, 2);
    }
}
