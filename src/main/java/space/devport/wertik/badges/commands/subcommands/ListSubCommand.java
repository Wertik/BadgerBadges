package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.system.user.struct.User;

public class ListSubCommand extends BadgeSubCommand {

    public ListSubCommand(BadgePlugin plugin) {
        super("list", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        OfflinePlayer target = plugin.getCommandParser().parseTarget(sender, args);

        if (target == null)
            return CommandResult.NO_CONSOLE;

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());

        if (user.getCollectedBadges().isEmpty()) {
            //TODO
            sender.sendMessage(StringUtil.color("&cUser has not badges collected."));
            return CommandResult.FAILURE;
        }

        //TODO
        StringBuilder list = new StringBuilder("&8&m    &6 Collected Badges");
        user.getBadges().forEach(b -> list.append("\n&8 - &f%displayName%".replace("%displayName%", b.getDisplayName())));
        sender.sendMessage(StringUtil.color(list.toString()));
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
