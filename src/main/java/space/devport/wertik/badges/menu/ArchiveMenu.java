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
import java.util.Collection;
import java.util.List;

public class ArchiveMenu extends Menu {

    private final BadgePlugin plugin;

    private final Player player;
    private final User user;

    private final int slotsPerPage;

    private int page = 1;

    public ArchiveMenu(BadgePlugin plugin, Player player, User user) {
        super("badges_archive_menu");
        this.plugin = plugin;
        this.player = player;
        this.user = user;

        this.slotsPerPage = MenuUtil.countMatrixSlots(plugin.getManager(CustomisationManager.class).getMenuBuilder("archive").construct(), 'x');

        build();
    }

    private void build() {
        MenuBuilder menuBuilder = new MenuBuilder(plugin.getManager(CustomisationManager.class).getMenuBuilder("archive")).construct();

        Placeholders placeholders = new Placeholders(plugin.getGlobalPlaceholders()).addContext(player, user);

        menuBuilder.getTitle().parseWith(placeholders);

        MatrixItem matrixItem = new MatrixItem('x');

        List<Badge> badges = new ArrayList<>(plugin.getBadgeManager().getLoadedBadges().values());

        badges.removeIf(user::hasBadge);

        for (int i = (this.page - 1) * slotsPerPage; i < badges.size() && i < slotsPerPage * this.page; i++) {

            Badge badge = badges.get(i);

            ItemBuilder item = badge.getNotCollectedItem();

            Placeholders badgePlaceholders = new Placeholders(placeholders);

            item.parseWith(badgePlaceholders.addContext(badge));

            MenuItem menuItem = new MenuItem(item, badge.getName(), -1);

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
