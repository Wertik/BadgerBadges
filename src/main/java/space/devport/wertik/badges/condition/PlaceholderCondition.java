package space.devport.wertik.badges.condition;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.badges.condition.operator.OperatorWrapper;
import space.devport.wertik.badges.condition.operator.SignOperator;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.function.Predicate;

public class PlaceholderCondition implements Predicate<User> {

    private final String condition;

    public PlaceholderCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(User user) {

        if (this.condition == null || user == null)
            return false;

        String condition = this.condition;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUniqueID());

        condition = ParserUtil.parsePlaceholders(condition, offlinePlayer);

        if (Strings.isNullOrEmpty(condition))
            return true;

        OperatorWrapper wrapper = SignOperator.fromString(condition);

        // No operator, true by default
        if (wrapper == null)
            return true;

        String[] args = condition.split(wrapper.sign());

        if (args.length != 2) {
            ConsoleOutput.getInstance().warn("Invalid condition: " + this.condition);
            return false;
        }

        String leftSide = args[0];
        String rightSide = args[1];

        Object parsedLeft = ParserUtil.parseObject(leftSide);
        Object parsedRight = ParserUtil.parseObject(rightSide);

        return wrapper.operator().test(parsedLeft, parsedRight);
    }

    @Override
    public String toString() {
        return condition;
    }
}