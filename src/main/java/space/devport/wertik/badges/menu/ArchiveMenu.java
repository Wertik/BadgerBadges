package space.devport.wertik.badges.menu;

import org.bukkit.entity.Player;
import space.devport.dock.CustomisationManager;
import space.devport.dock.item.ItemPrefab;
import space.devport.dock.menu.Menu;
import space.devport.dock.menu.MenuBuilder;
import space.devport.dock.menu.item.MatrixItem;
import space.devport.dock.menu.item.MenuItem;
import space.devport.dock.text.placeholders.Placeholders;
import space.devport.wertik.badges.BadgePlugin;
import space.devport.wertik.badges.system.badge.struct.Badge;
import space.devport.wertik.badges.system.user.struct.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArchiveMenu extends Menu {

    private final BadgePlugin plugin;

    private final Player player;
    private final User user;

    private final int slotsPerPage;

    private int page = 1;

    public ArchiveMenu(BadgePlugin plugin, Player player, User user) {
        super("badges_archive_menu", plugin);
        this.plugin = plugin;
        this.player = player;
        this.user = user;

        this.slotsPerPage = MenuUtil.countMatrixSlots(plugin.getManager(CustomisationManager.class).getMenu("archive").construct(), 'x');

        build();
    }

    private void build() {
        MenuBuilder prefab = plugin.getManager(CustomisationManager.class).getMenu("archive");

        MenuBuilder menuBuilder = new MenuBuilder(prefab.getName(), prefab).construct();

        Placeholders placeholders = Placeholders.of(plugin.getGlobalPlaceholders()).addContext(player, user);

        menuBuilder.getTitle().parseWith(placeholders);

        MatrixItem matrixItem = new MatrixItem('x');

        List<Badge> badges = new ArrayList<>(plugin.getBadgeManager().getLoadedBadges().values());

        badges.removeIf(user::hasBadge);

        for (int i = (this.page - 1) * slotsPerPage; i < badges.size() && i < slotsPerPage * this.page; i++) {

            Badge badge = badges.get(i);

            ItemPrefab item = badge.getNotCollectedItem();

            Placeholders badgePlaceholders = Placeholders.of(placeholders);

            item.parseWith(badgePlaceholders.addContext(badge));

            MenuItem menuItem = new MenuItem(plugin, item, badge.getName(), -1);

            matrixItem.addItem(menuItem);
        }

        menuBuilder.addMatrixItem(matrixItem);

        // Page control and close

        if (menuBuilder.getItem("collection") != null)
            menuBuilder.getItem("collection").setClickAction(itemClick -> {
                new CollectionMenu(plugin, player, user).open(player);
            });

        if (menuBuilder.getItem("close") != null)
            menuBuilder.getItem("close").setClickAction(itemClick -> close());

        if (this.page < this.maxPage(badges) && menuBuilder.getItem("page-next") != null)
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

    private int maxPage(Collection<?> collection) {
        return (int) Math.ceil((float) collection.size() / slotsPerPage);
    }

    private void incPage() {
        this.page++;
    }

    private void decPage() {
        this.page--;
    }
}
