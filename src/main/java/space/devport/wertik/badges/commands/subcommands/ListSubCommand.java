package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.text.message.Message;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

public class ListSubCommand extends BadgeSubCommand {

    public ListSubCommand(BadgePlugin plugin) {
        super("list", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args.length > 1 ? args[0] : null);

        if (target == null)
            return CommandResult.NO_CONSOLE;

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());

        if (user.getCollectedBadges().isEmpty()) {
            language.getPrefixed(target == sender ? "Commands.No-Badges" : "Commands.No-Badges-Others")
                    .replace("%player%", target.getName())
                    .send(sender);
            return CommandResult.FAILURE;
        }

        Message list = language.get("Commands.List.Header");
        String lineFormat = language.get("Commands.List.Line").toString();

        for (Badge badge : user.getBadges()) {
            list.append(new Message(lineFormat)
                    .replace("%badgeDisplay%", badge.getDisplayName())
                    .replace("%name%", badge.getName()).toString());
        }

        list.replace("%count%", user.getCollectedBadges().size())
                .send(sender);
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
