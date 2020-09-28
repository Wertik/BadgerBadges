package space.devport.wertik.badges.system.user.struct;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.badge.struct.Badge;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class User {

    @Getter
    private final UUID uniqueID;

    private final Set<CollectedBadge> collectedBadges = new HashSet<>();

    public User(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void addBadge(Badge badge) {
        this.collectedBadges.add(new CollectedBadge(badge.getName()));
    }

    public void addBadge(String name) {
        Badge badge = BadgePlugin.getInstance().getBadgeManager().getBadge(name);
        if (badge == null) {
            ConsoleOutput.getInstance().warn("Attempted to assign an invalid badge to player " + uniqueID.toString() + ". Fix the badge and reload the plugin or remove it /badges purgeinvalid");
            return;
        }
        addBadge(badge);
    }

    public void removeBadge(String name) {
        this.collectedBadges.remove(new CollectedBadge(name));
    }

    public void removeBadge(Badge badge) {
        removeBadge(badge.getName());
    }

    public boolean hasBadge(String badgeName) {
        return this.collectedBadges.contains(new CollectedBadge(badgeName));
    }

    public boolean hasBadge(Badge badge) {
        return hasBadge(badge.getName());
    }

    public int purgeInvalid() {
        AtomicInteger count = new AtomicInteger(0);
        this.collectedBadges.removeIf(collectedBadge -> {
            if (BadgePlugin.getInstance().getBadgeManager().getBadge(collectedBadge.getBadgeName()) == null) {
                count.incrementAndGet();
                return true;
            }
            return false;
        });
        return count.get();
    }

    public Set<Badge> getBadges() {
        return this.collectedBadges.stream()
                .filter(collectedBadge -> {
                    Badge badge = BadgePlugin.getInstance().getBadgeManager().getBadge(collectedBadge.getBadgeName());
                    if (badge == null) {
                        ConsoleOutput.getInstance().warn("User " + uniqueID.toString() + " has an invalid badge collected. Fix the badge and reload the plugin or remove it /badges purgeinvalid");
                        return false;
                    }
                    return true;
                })
                .map(collectedBadge -> BadgePlugin.getInstance().getBadgeManager().getBadge(collectedBadge.getBadgeName()))
                .collect(Collectors.toSet());
    }

    @Nullable
    public CollectedBadge getCollectedBadge(String name) {
        return this.collectedBadges.stream().filter(b -> b.getBadgeName().equals(name)).findAny().orElse(null);
    }

    public Set<CollectedBadge> getCollectedBadges() {
        return Collections.unmodifiableSet(collectedBadges);
    }
}