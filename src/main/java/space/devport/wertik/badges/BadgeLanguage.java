package space.devport.wertik.badges;

import space.devport.utils.DevportPlugin;
import space.devport.utils.text.language.LanguageDefaults;

public class BadgeLanguage extends LanguageDefaults {

    public BadgeLanguage(DevportPlugin plugin) {
        super(plugin);
    }

    @Override
    public void setDefaults() {
        addDefault("Commands.Invalid-Badge", "&f%param% &cis not a valid badge.");

        addDefault("Commands.No-Badges-Others", "&cUser &f%player% &chas no badges.");
        addDefault("Commands.No-Badges", "&cYou have no badges.");

        addDefault("Commands.Does-Not-Have", "&cYou don't have this badge.");
        addDefault("Commands.Does-Not-Have-Others", "&cPlayer &f%player% &cdoesn't have this badge.");

        addDefault("Commands.Add.Done", "&7You granted yourself the badge %badgeDisplayName% &7( &f%badgeName% &7)");
        addDefault("Commands.Add.Done-Others", "&7You granted &f%player% the badge %badgeDisplayName% &7( &f%badgeName% &7)");

        addDefault("Commands.Add-All.Done", "&7Added badge &f%badgeName% &7to &f%count% &7user(s).");

        addDefault("Commands.List.Header", "&8&m    &e Collected Badges &7( &f%collectedCount% &7)");
        addDefault("Commands.List.Line", "&8 - %badgeDisplay% &7( &f%badgeName%&7, &f%collectedDate% &7)");

        addDefault("Commands.List-Badges.Header", "&8&m    &e Badges &7( &f%count% &7)");
        addDefault("Commands.List-Badges.Line", "&8 - &f%badgeDisplayName% &7( &f%badgeName% &7)");

        addDefault("Commands.Purge-Invalid.Starting", "&7&oStarting...");
        addDefault("Commands.Purge-Invalid.Done", "&7Purged &f%count% &7invalid badge references.");

        addDefault("Commands.Remove.Done", "&7Removed yourself the badge &f%badgeName%&7.");
        addDefault("Commands.Remove.Done-Others", "&7Remove badge &f%badgeName% &7from &f%player%&7's collection.");
    }
}
