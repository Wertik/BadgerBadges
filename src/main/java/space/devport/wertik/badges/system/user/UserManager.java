package space.devport.wertik.badges.system.user;

import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import space.devport.dock.util.json.GsonHelper;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log
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
        log.info(String.format("Started auto save with an interval of %d seconds.", interval));
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
        log.fine("Created user " + uniqueID.toString());
        return user;
    }

    public void deleteUser(UUID uniqueID) {
        this.loadedUsers.remove(uniqueID);
        log.fine("Deleted user " + uniqueID.toString());
    }

    public void load() {
        gsonHelper.loadMapAsync(plugin.getDataFolder().getPath() + "/user-data.json", UUID.class, User.class).thenAcceptAsync(loaded -> {

            if (loaded == null)
                loaded = new HashMap<>();

            loadedUsers.clear();
            loadedUsers.putAll(loaded);

            log.fine(String.format("Loaded %d user(s)...", loadedUsers.size()));
        }).exceptionally(e -> {
            log.severe(String.format("Could not load users: %s", e.getMessage()));
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
            log.info(String.format("Purged %d empty account(s)...", count));
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
        purgeEmpty().thenRunAsync(() -> {
            gsonHelper.save(plugin.getDataFolder().getPath() + "/user-data.json", this.loadedUsers).ifFailed(e -> {
                log.severe("Failed to save users.");
                e.printStackTrace();
            }).ifPresent((unused) -> {
                log.info("Saved " + this.loadedUsers.size() + " user(s)...");
            });
        });
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