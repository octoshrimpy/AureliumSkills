package com.archyx.aureliumskills.menu.items;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.lang.MenuMessage;
import com.archyx.aureliumskills.util.ItemUtils;
import com.archyx.aureliumskills.util.LoreUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class BackItem extends ConfigurableItem {

    public BackItem(AureliumSkills plugin) {
        super(plugin, ItemType.BACK, new String[] {"back_click"});
    }

    public ItemStack getItem(Player player) {
        Locale locale = Lang.getLanguage(player);
        ItemStack item = baseItem.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(applyPlaceholders(LoreUtil.replace(displayName,"{back}", Lang.getMessage(MenuMessage.BACK, locale)), player));
            List<String> builtLore = new ArrayList<>();
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                Set<String> placeholders = lorePlaceholders.get(i);
                for (String placeholder : placeholders) {
                    if (placeholder.equals("back_click")) {
                        line = LoreUtil.replace(line, "{back_click}", Lang.getMessage(MenuMessage.BACK_CLICK, locale));
                    }
                }
                builtLore.add(line);
            }
            meta.setLore(ItemUtils.formatLore(applyPlaceholders(builtLore, player)));
            item.setItemMeta(meta);
        }
        return item;
    }
}
