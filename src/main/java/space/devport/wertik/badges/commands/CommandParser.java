package space.devport.wertik.badges.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.wertik.badges.BadgePlugin;

public class CommandParser {

    private final BadgePlugin plugin;

    public CommandParser(BadgePlugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public OfflinePlayer parseTarget(CommandSender sender, String[] args) {
        OfflinePlayer target;
        if (args.length > 1) {
            target = Bukkit.getOfflinePlayer(args[1]);
        } else {
            if (!(sender instanceof Player))
                return null;

            target = (Player) sender;
        }
        return target;
    }
}