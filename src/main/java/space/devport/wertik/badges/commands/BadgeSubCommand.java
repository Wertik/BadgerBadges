package space.devport.wertik.badges.commands;

import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.wertik.badges.BadgePlugin;

public abstract class BadgeSubCommand extends SubCommand {

    protected final BadgePlugin plugin;

    public BadgeSubCommand(String name, BadgePlugin plugin) {
        super(name);
        setPermissions();
        this.plugin = plugin;
    }

    @Override
    public @Nullable
    abstract String getDefaultUsage();

    @Override
    public @Nullable
    abstract String getDefaultDescription();

    @Override
    public @Nullable
    abstract ArgumentRange getRange();
}