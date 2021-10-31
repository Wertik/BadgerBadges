package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.dock.commands.struct.ArgumentRange;
import space.devport.dock.commands.struct.CommandResult;
import space.devport.dock.text.message.Message;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.system.badge.struct.Badge;

public class ListBadgesSubCommand extends BadgeSubCommand {

    public ListBadgesSubCommand(BadgePlugin plugin) {
        super("listbadges", plugin);
    }

    @Override
    protected @NotNull CommandResult perform(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Message header = language.get("Commands.List-Badges.Header").replace("%count%", plugin.getBadgeManager().getLoadedBadges().size());
        String lineFormat = language.get("Commands.List-Badges.Line").toString();
        for (Badge badge : plugin.getBadgeManager().getLoadedBadges().values()) {
            header.append(new Message(lineFormat)
                    .parseWith(plugin.getGlobalPlaceholders())
                    .context(badge)
                    .parse()
                    .toString());
        }
        header.send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% listbadges";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "List all available badges.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}
