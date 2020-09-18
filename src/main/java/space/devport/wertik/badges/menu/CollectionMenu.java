package space.devport.wertik.badges.menu;

import org.bukkit.entity.Player;
import space.devport.utils.CustomisationManager;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.menu.Menu;
import space.devport.utils.menu.MenuBuilder;
import space.devport.utils.menu.item.MatrixItem;
import space.devport.utils.menu.item.MenuItem;
import space.devport.utils.text.Placeholders;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.ArrayList;
import java.util.List;

public class CollectionMenu extends Menu {

    private final BadgePlugin plugin;

    private final Player player;

    private final User user;

    private final int slotsPerPage;

    private int page = 1;

    public CollectionMenu(BadgePlugin plugin, Player player) {
        super("badges_collection_menu");
        this.plugin = plugin;
        this.player = player;
        this.user = plugin.getUserManager().getOrCreateUser(player.getUniqueId());

        this.slotsPerPage = countMatrixSlots(plugin.getManager(CustomisationManager.class).getMenuBuilder("collection").construct(), 'x');

        build();
    }

    private int countMatrixSlots(MenuBuilder menuBuilder, char character) {
        int count = 0;
        for (String line : menuBuilder.getBuildMatrix()) {
            for (char c : line.toCharArray())
                if (c == character) count++;
        }
        return count;
    }

    private void build() {
        MenuBuilder menuBuilder = new MenuBuilder(plugin.getManager(CustomisationManager.class).getMenuBuilder("collection")).construct();

        Placeholders placeholders = new Placeholders()
                .add("%player%", player.getName())
                .add("%collected%", user.getCollectedBadges().size());

        menuBuilder.getTitle().parseWith(placeholders);

        MatrixItem matrixItem = new MatrixItem('x');

        List<Badge> badges = new ArrayList<>(plugin.getBadgeManager().getLoadedBadges().values());

        for (int i = (this.page - 1) * slotsPerPage; i < badges.size() && i < slotsPerPage * this.page; i++) {

            Badge badge = badges.get(i);

            ItemBuilder item = user.hasBadge(badge) ? badge.getDisplayItem() : badge.getNotCollectedItem();

            item.getPlaceholders()
                    .add("%name%", badge.getName())
                    .add("%displayName%", badge.getDisplayName());

            MenuItem menuItem = new MenuItem(item, badge.getName(), -1);

            matrixItem.addItem(menuItem);
        }
        menuBuilder.addMatrixItem(matrixItem);

        // Page control and close

        if (menuBuilder.getItem("close") != null)
            menuBuilder.getItem("close").setClickAction(itemClick -> close());

        if (this.page < this.maxPage() && menuBuilder.getItem("page-next") != null)
            menuBuilder.getMatrixItem('n').getItem("page-next").setClickAction(itemClick -> {
                incPage();
                build();
                reload();
            });
        else
            menuBuilder.removeMatrixItem('n');

        if (this.page > 1 && menuBuilder.getItem("page-previous") != null)
            menuBuilder.getMatrixItem('p').getItem("page-previous").setClickAction(itemClick -> {
                decPage();
                build();
                reload();
            });
        else
            menuBuilder.removeMatrixItem('p');

        setMenuBuilder(menuBuilder.construct());
    }

    private int maxPage() {
        return user.getCollectedBadges().size() / slotsPerPage;
    }

    private void incPage() {
        this.page++;
    }

    private void decPage() {
        this.page--;
    }
}