package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.struct.Context;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.menu.CollectionMenu;
import space.devport.wertik.badges.system.user.struct.User;

public class CollectionSubCommand extends BadgeSubCommand {

    public CollectionSubCommand(BadgePlugin plugin) {
        super("collection", plugin);
        this.preconditions.playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        OfflinePlayer target;
        if (args.length > 0) {
            target = Bukkit.getOfflinePlayer(args[0]);

            if (!sender.hasPermission("badgerbadges.view.others"))
                return CommandResult.NO_PERMISSION;
        } else {
            target = (Player) sender;
        }

        User user = plugin.getUserManager().getOrCreateUser(target.getUniqueId());

        Player player = (Player) sender;
        new CollectionMenu(plugin, player, user)
                .archive(!target.getUniqueId().equals(player.getUniqueId()))
                .open(player);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% collection (player)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "View your badge collection.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}