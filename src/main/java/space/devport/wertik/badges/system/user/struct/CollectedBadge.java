package space.devport.wertik.badges.system.user.struct;

import lombok.Getter;
import space.devport.wertik.badges.BadgePlugin;

import java.time.LocalDateTime;
import java.util.Objects;

public class CollectedBadge {

    @Getter
    private final String badgeName;
    @Getter
    private final LocalDateTime timeStamp;

    public CollectedBadge(String badgeName) {
        this.badgeName = badgeName;
        this.timeStamp = LocalDateTime.now();
    }

    public CollectedBadge(String badgeName, LocalDateTime timeStamp) {
        this.badgeName = badgeName;
        this.timeStamp = timeStamp;
    }

    public String getDateFormatted() {
        return BadgePlugin.getInstance().getDateFormat().format(timeStamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectedBadge that = (CollectedBadge) o;
        return badgeName.equals(that.badgeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(badgeName);
    }
}
