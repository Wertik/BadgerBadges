package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.menu.Menu;
import space.devport.utils.struct.Context;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.menu.ArchiveMenu;
import space.devport.wertik.badges.system.user.struct.User;

public class ArchiveSubCommand extends BadgeSubCommand {

    public ArchiveSubCommand(BadgePlugin plugin) {
        super("archive", plugin);
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

        User user = plugin.getUserManager().getUser(target.getUniqueId());

        Context context = new Context(user).fromPlayer(target);

        if (user == null) {
            language.getPrefixed("Commands.No-Badges-Others")
                    .send(sender, context);
            return CommandResult.FAILURE;
        }

        Player player = (Player) sender;
        new ArchiveMenu(plugin, player, user)
                .open(player);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% archive (player)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "View badges that you have not yet collected.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0, 1);
    }
}