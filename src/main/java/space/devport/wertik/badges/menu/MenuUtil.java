package space.devport.wertik.badges.menu;

import lombok.experimental.UtilityClass;
import space.devport.utils.menu.MenuBuilder;

@UtilityClass
public class MenuUtil {

    public int countMatrixSlots(MenuBuilder menuBuilder, char character) {
        int count = 0;
        for (String line : menuBuilder.getBuildMatrix()) {
            for (char c : line.toCharArray())
                if (c == character) count++;
        }
        return count;
    }
}
