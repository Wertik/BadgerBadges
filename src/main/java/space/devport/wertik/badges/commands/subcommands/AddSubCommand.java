package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
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

        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args.length > 1 ? args[0] : null);

        if (target == null)
            return CommandResult.NO_CONSOLE;

        Badge badge = plugin.getBadgeManager().getBadge(args[0]);

        if (badge == null) {
            language.getPrefixed("Commands.Invalid-Badge")
                    .replace("%param%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());
        user.addBadge(badge);
        language.getPrefixed(target == sender ? "Commands.Add.Done" : "Commands.Add.Done-Others")
                .replace("%badge%", badge.getName())
                .replace("%badgeDisplay%", badge.getDisplayName())
                .replace("%player%", target.getName())
                .send(sender);
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
