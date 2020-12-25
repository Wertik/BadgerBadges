package space.devport.wertik.badges.system.user;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.utility.json.GsonHelper;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserManager {

    private final BadgePlugin plugin;

    private final GsonHelper gsonHelper;

    private final Map<UUID, User> loadedUsers = new HashMap<>();

    private BukkitTask autoSave;

    public UserManager(BadgePlugin plugin) {
        this.plugin = plugin;
        this.gsonHelper = new GsonHelper();
    }

    public void stopAutoSave() {
        if (autoSave == null)
            return;

        autoSave.cancel();
        autoSave = null;
    }

    public void startAutoSave() {
        if (autoSave != null)
            stopAutoSave();

        int interval = plugin.getConfig().getInt("auto-save.interval", 300);

        this.autoSave = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, interval * 20L, interval * 20L);
    }

    public void reloadAutoSave() {
        stopAutoSave();

        if (plugin.getConfig().getBoolean("auto-save.enabled", false))
            startAutoSave();
    }

    @NotNull
    public User getOrCreateUser(UUID uniqueID) {
        User user = getUser(uniqueID);
        return user == null ? createUser(uniqueID) : user;
    }

    public User getUser(UUID uniqueID) {
        return this.loadedUsers.get(uniqueID);
    }

    public User createUser(UUID uniqueID) {
        User user = new User(uniqueID);
        this.loadedUsers.put(uniqueID, user);
        plugin.getConsoleOutput().debug("Created user " + uniqueID.toString());
        return user;
    }

    public void deleteUser(UUID uniqueID) {
        this.loadedUsers.remove(uniqueID);
        plugin.getConsoleOutput().debug("Deleted user " + uniqueID.toString());
    }

    public void load() {
        gsonHelper.loadMapAsync(plugin.getDataFolder().getPath() + "/user-data.json", UUID.class, User.class).thenAcceptAsync(loaded -> {

            if (loaded == null)
                loaded = new HashMap<>();

            loadedUsers.clear();
            loadedUsers.putAll(loaded);

            plugin.getConsoleOutput().info(String.format("Loaded %d user(s)...", loadedUsers.size()));
        }).exceptionally(e -> {
            plugin.getConsoleOutput().err(String.format("Could not load users: %s", e.getMessage()));
            e.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<Void> purgeEmpty() {
        return CompletableFuture.runAsync(() -> {
            int count = 0;
            for (User user : new HashSet<>(loadedUsers.values())) {
                if (user.getBadges().isEmpty()) {
                    deleteUser(user.getUniqueID());
                    count++;
                }
            }
            plugin.getConsoleOutput().info(String.format("Purged %d empty account(s)...", count));
        });
    }

    public CompletableFuture<Integer> purgeInvalid() {
        return CompletableFuture.supplyAsync(() -> {
            int count = 0;
            for (User user : this.loadedUsers.values()) {
                count += user.purgeInvalid();
            }
            return count;
        });
    }

    public void save() {
        purgeEmpty().thenRunAsync(() ->
                gsonHelper.save(this.loadedUsers, plugin.getDataFolder().getPath() + "/user-data.json")
                        .thenRunAsync(() -> plugin.getConsoleOutput().info("Saved " + this.loadedUsers.size() + " user(s)..."))
                        .exceptionally(e -> {
                            plugin.getConsoleOutput().err(String.format("Could not save users: %s", e.getMessage()));
                            e.printStackTrace();
                            return null;
                        }));
    }

    public Collection<User> getUsers() {
        return loadedUsers.values();
    }

    public Set<User> getUsers(Predicate<User> condition) {
        return loadedUsers.values().stream()
                .filter(condition)
                .collect(Collectors.toSet());
    }
}