package space.devport.wertik.badges.system.user;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.GsonHelper;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserManager {

    private final BadgePlugin plugin;

    private final GsonHelper gsonHelper;

    private final Map<UUID, User> loadedUsers = new HashMap<>();

    public UserManager(BadgePlugin plugin) {
        this.plugin = plugin;
        this.gsonHelper = plugin.getGsonHelper();
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
        this.loadedUsers.clear();

        Map<UUID, User> loadedData = gsonHelper.load(plugin.getDataFolder() + "/user-data.json", new TypeToken<Map<UUID, User>>() {
        }.getType());

        if (loadedData == null) loadedData = new HashMap<>();

        this.loadedUsers.putAll(loadedData);

        plugin.getConsoleOutput().info("Loaded " + this.loadedUsers.size() + " user(s)...");
    }

    public void purgeEmpty() {
        int count = 0;
        for (User user : new HashSet<>(loadedUsers.values())) {
            if (user.getBadges().isEmpty()) {
                deleteUser(user.getUniqueID());
                count++;
            }
        }
        plugin.getConsoleOutput().info("Purged " + count + " empty account(s)...");
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
        purgeEmpty();

        gsonHelper.save(this.loadedUsers, plugin.getDataFolder() + "/user-data.json");
        plugin.getConsoleOutput().info("Saved " + this.loadedUsers.size() + " user(s)...");
    }
}