package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.struct.Context;
import space.devport.dock.text.message.Message;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

public class ListSubCommand extends BadgeSubCommand {

    public ListSubCommand(BadgePlugin plugin) {
        super("list", plugin);
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {

        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args.length > 0 ? args[0] : null);

        if (target == null)
            return CommandResult.NO_CONSOLE;

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());

        Context context = new Context(user).fromPlayer(target);

        if (user.getBadges().isEmpty()) {
            language.getPrefixed(target == sender ? "Commands.No-Badges" : "Commands.No-Badges-Others")
                    .send(sender, context);
            return CommandResult.FAILURE;
        }

        Message list = language.get("Commands.List.Header");
        String lineFormat = language.get("Commands.List.Line").toString();

        for (Badge badge : user.getBadges()) {
            list.append(new Message(lineFormat)
                    .parseWith(plugin.getGlobalPlaceholders())
                    .context(context.add(badge))
                    .parse()
                    .toString());
        }

        list.replace("%count%", user.getBadges().size())
                .send(sender, context);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% list (player)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "List player badges.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}
