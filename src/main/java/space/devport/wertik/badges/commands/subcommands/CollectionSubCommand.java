package space.devport.wertik.badges.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.commands.BadgeSubCommand;
import space.devport.wertik.badges.menu.CollectionMenu;

public class CollectionSubCommand extends BadgeSubCommand {

    public CollectionSubCommand(BadgePlugin plugin) {
        super("Collection", plugin);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        new CollectionMenu(plugin, player)
                .open(player);
        return CommandResult.SUCCESS;
    }

    @Override
    public @Nullable String getDefaultUsage() {
        return "/%label% collection";
    }

    @Override
    public @Nullable String getDefaultDescription() {
        return "View your badge collection.";
    }

    @Override
    public @Nullable ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}