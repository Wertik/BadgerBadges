package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.struct.Context;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.condition.PlaceholderCondition;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.ArrayList;
import java.util.List;

public class AddAllCommand extends BadgeSubCommand {

    public AddAllCommand(BadgePlugin plugin) {
        super("addall", plugin);
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Badge badge = plugin.getCommandParser().parseBadge(sender, args[0], new Context());

        if (badge == null)
            return CommandResult.FAILURE;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String condition = args.length > 1 ? args[1] : null;

            PlaceholderCondition placeholderCondition = new PlaceholderCondition(condition);

            int count = 0;
            for (User user : condition == null ? plugin.getUserManager().getUsers() : plugin.getUserManager().getUsers(placeholderCondition)) {
                user.addBadge(badge);
                count++;
            }

            language.getPrefixed("Commands.Add-All.Done")
                    .replace("%count%", count)
                    .context(badge)
                    .send(sender);
        });
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull List<String> requestTabComplete(@NotNull CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getBadgeManager().getLoadedBadges().keySet());
        }
        return new ArrayList<>();
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% addall <badge> (condition)";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "Add a badge to all players who meet specified condition. Ex.: %player_seconds_played%>10000";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(1, 2);
    }
}
