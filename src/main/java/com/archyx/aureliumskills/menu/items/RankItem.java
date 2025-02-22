package com.archyx.aureliumskills.menu.items;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.lang.MenuMessage;
import com.archyx.aureliumskills.skills.Skill;
import com.archyx.aureliumskills.util.ItemUtils;
import com.archyx.aureliumskills.util.LoreUtil;
import com.archyx.aureliumskills.util.NumberUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RankItem extends ConfigurableItem {

    public RankItem(AureliumSkills plugin) {
        super(plugin, ItemType.RANK, new String[] {"out_of", "percent"});
    }

    public ItemStack getItem(Skill skill, Player player, Locale locale) {
        ItemStack item = baseItem.clone();
        ItemMeta meta = item.getItemMeta();
        int rank = plugin.getLeaderboard().getSkillRank(skill, player.getUniqueId());
        int size = plugin.getLeaderboard().getSize();
        if (meta != null) {
            meta.setDisplayName(applyPlaceholders(LoreUtil.replace(displayName,"{your_ranking}", Lang.getMessage(MenuMessage.YOUR_RANKING, locale)), player));
            List<String> builtLore = new ArrayList<>();
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                Set<String> placeholders = lorePlaceholders.get(i);
                for (String placeholder : placeholders) {
                    if (placeholder.equals("out_of")) {
                        line = LoreUtil.replace(line,"{out_of}", LoreUtil.replace(Lang.getMessage(MenuMessage.RANK_OUT_OF, locale),"{rank}", String.valueOf(rank), "{total}", String.valueOf(size)));
                    }
                    else if (placeholder.equals("percent")) {
                        double percent = (double) rank / (double) size * 100;
                        if (percent > 1) {
                            line = LoreUtil.replace(line,"{percent}", LoreUtil.replace(Lang.getMessage(MenuMessage.RANK_PERCENT, locale),"{percent}", String.valueOf((int) percent)));
                        }
                        else {
                            line = LoreUtil.replace(line,"{percent}", LoreUtil.replace(Lang.getMessage(MenuMessage.RANK_PERCENT, locale),"{percent}",  NumberUtil.format2(percent)));
                        }
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
