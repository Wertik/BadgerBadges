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

public class RemoveSubCommand extends BadgeSubCommand {

    public RemoveSubCommand(BadgePlugin plugin) {
        super("remove", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args.length > 1 ? args[0] : null);

        if (target == null) return CommandResult.NO_CONSOLE;

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());
        if (!user.hasBadge(args[0])) {
            language.getPrefixed(target == sender ? "Commands.Does-Not-Have" : "Commands.Does-Not-Have-Others")
                    .replace("%player%", target.getName())
                    .send(sender);
            return CommandResult.FAILURE;
        }

        user.removeBadge(args[0]);

        Badge badge = plugin.getBadgeManager().getBadge(args[0]);

        language.getPrefixed(sender == target ? "Commands.Remove.Done" : "Commands.Remove.Done-Others")
                .replace("%badge%", badge.getName())
                .replace("%badgeDisplay%", badge.getDisplayName())
                .replace("%player%", target.getName())
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% remove <badge> (player)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "Remove player a badge.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(1, 2);
    }
}
