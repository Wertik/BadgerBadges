package space.devport.wertik.badges.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.dock.struct.Context;
import space.devport.dock.text.language.LanguageManager;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

public class CommandParser {

    private final BadgePlugin plugin;

    private final LanguageManager language;

    public CommandParser(BadgePlugin plugin) {
        this.plugin = plugin;
        this.language = plugin.getManager(LanguageManager.class);
    }

    public User parseUser(CommandSender sender, String arg) {
        OfflinePlayer target = parseTarget(sender, arg);
        return target == null ? null : plugin.getUserManager().getOrCreateUser(target.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    @Nullable
    public OfflinePlayer parseTarget(CommandSender sender, String arg) {
        OfflinePlayer target;
        if (arg != null) {
            target = Bukkit.getOfflinePlayer(arg.trim());
        } else {
            if (!(sender instanceof Player))
                return null;

            target = (Player) sender;
        }
        return target;
    }

    public Badge parseBadge(CommandSender sender, String arg, Context context) {
        Badge badge = plugin.getBadgeManager().getBadge(arg);
        if (badge == null)
            language.getPrefixed("Commands.Invalid-Badge")
                    .replace("%param%", arg)
                    .send(sender, context);
        return badge;
    }
}