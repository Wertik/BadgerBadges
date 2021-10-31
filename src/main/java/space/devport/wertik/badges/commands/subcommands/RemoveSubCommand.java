package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.struct.Context;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveSubCommand extends BadgeSubCommand {

    public RemoveSubCommand(BadgePlugin plugin) {
        super("remove", plugin);
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args.length > 1 ? args[0] : null);

        if (target == null) return CommandResult.NO_CONSOLE;

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());
        Context context = new Context(user).fromPlayer(target);

        if (!user.hasBadge(args[0])) {
            language.getPrefixed(sender.equals(target) ? "Commands.Does-Not-Have" : "Commands.Does-Not-Have-Others")
                    .send(sender, context);
            return CommandResult.FAILURE;
        }

        user.removeBadge(args[0]);

        Badge badge = plugin.getBadgeManager().getBadge(args[0]);
        language.getPrefixed(sender.equals(target) ? "Commands.Remove.Done" : "Commands.Remove.Done-Others")
                .send(sender, context.add(badge));
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> requestTabComplete(@NotNull CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getBadgeManager().getLoadedBadges().keySet());
        } else if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
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
