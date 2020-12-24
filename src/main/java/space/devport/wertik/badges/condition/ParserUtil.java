package space.devport.wertik.badges.condition;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.struct.Context;
import space.devport.utils.text.Placeholders;
import space.devport.wertik.badges.BadgePlugin;

@UtilityClass
public class ParserUtil {

    @Nullable
    public Object parseObject(String str) {

        if (Strings.isNullOrEmpty(str)) return str;

        str = str.trim();

        Object obj = null;

        try {
            obj = Integer.parseInt(str);
        } catch (NumberFormatException ignored) {
        }

        if (obj == null)
            try {
                obj = Double.parseDouble(str);
            } catch (NumberFormatException ignored) {
            }

        return obj == null ? str : obj;
    }

    @Nullable
    public String parsePlaceholders(@Nullable String str, @Nullable OfflinePlayer player) {

        if (str == null) return null;

        return new Placeholders(BadgePlugin.getInstance().getGlobalPlaceholders())
                .addContext(new Context().fromPlayer(player))
                .parse(str);
    }
}
