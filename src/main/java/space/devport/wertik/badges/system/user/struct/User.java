package space.devport.wertik.badges.system.user.struct;

import lombok.Getter;
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

    private final Set<String> collectedBadges = new HashSet<>();

    public User(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void addBadge(String name) {
        Badge badge = BadgePlugin.getInstance().getBadgeManager().getBadge(name);
        if (badge == null) {
            ConsoleOutput.getInstance().warn("Attempted to assign an invalid badge to player " + uniqueID.toString() + ". Fix the badge and reload the plugin or remove it /badges purgeinvalid");
            return;
        }
        this.collectedBadges.add(name);
    }

    public void addBadge(Badge badge) {
        this.collectedBadges.add(badge.getName());
    }

    public void removeBadge(Badge badge) {
        this.collectedBadges.remove(badge.getName());
    }

    public void removeBadge(String name) {
        this.collectedBadges.remove(name);
    }

    public boolean hasBadge(String badgeName) {
        return this.collectedBadges.contains(badgeName);
    }

    public boolean hasBadge(Badge badge) {
        return this.collectedBadges.contains(badge.getName());
    }

    public int purgeInvalid() {
        AtomicInteger count = new AtomicInteger(0);
        this.collectedBadges.removeIf((name) -> {
            if (BadgePlugin.getInstance().getBadgeManager().getBadge(name) == null) {
                count.incrementAndGet();
                return true;
            }
            return false;
        });
        return count.get();
    }

    public Set<Badge> getBadges() {
        return this.collectedBadges.stream()
                .filter(name -> {
                    Badge badge = BadgePlugin.getInstance().getBadgeManager().getBadge(name);
                    if (badge == null) {
                        ConsoleOutput.getInstance().warn("User " + uniqueID.toString() + " has an invalid badge collected. Fix the badge and reload the plugin or remove it /badges purgeinvalid");
                        return false;
                    }
                    return true;
                })
                .map(name -> BadgePlugin.getInstance().getBadgeManager().getBadge(name))
                .collect(Collectors.toSet());
    }

    public Set<String> getCollectedBadges() {
        return Collections.unmodifiableSet(collectedBadges);
    }
}