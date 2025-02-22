package com.archyx.aureliumskills.menu.templates;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.lang.MenuMessage;
import com.archyx.aureliumskills.skills.PlayerSkill;
import com.archyx.aureliumskills.skills.Skill;
import com.archyx.aureliumskills.util.ItemUtils;
import com.archyx.aureliumskills.util.LoreUtil;
import com.archyx.aureliumskills.util.NumberUtil;
import com.archyx.aureliumskills.util.RomanNumber;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class InProgressTemplate extends ConfigurableTemplate {

    private final ProgressLevelItem levelItem;

    public InProgressTemplate(AureliumSkills plugin) {
        super(plugin, TemplateType.IN_PROGRESS, new String[] {"level_number", "rewards", "ability", "mana_ability", "progress", "in_progress"});
        this.levelItem = new ProgressLevelItem(plugin);
    }

    public ItemStack getItem(Skill skill, PlayerSkill playerSkill, int level, Player player, Locale locale) {
        ItemStack item = baseItem.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(applyPlaceholders(LoreUtil.replace(displayName,"{level_in_progress}", LoreUtil.replace(Lang.getMessage(MenuMessage.LEVEL_IN_PROGRESS, locale),"{level}", RomanNumber.toRoman(level))), player));
            List<String> builtLore = new ArrayList<>();
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                Set<String> placeholders = lorePlaceholders.get(i);
                for (String placeholder : placeholders) {
                    switch (placeholder) {
                        case "level_number":
                            line = LoreUtil.replace(line,"{level_number}", LoreUtil.replace(Lang.getMessage(MenuMessage.LEVEL_NUMBER, locale),"{level}", String.valueOf(level)));
                            break;
                        case "rewards":
                            line = LoreUtil.replace(line,"{rewards}", levelItem.getRewardsLore(skill, level, locale));
                            break;
                        case "ability":
                            line = LoreUtil.replace(line,"{ability}", levelItem.getAbilityLore(skill, level, locale));
                            break;
                        case "mana_ability":
                            line = LoreUtil.replace(line, "{mana_ability}", levelItem.getManaAbilityLore(skill, level, locale));
                            break;
                        case "progress":
                            double currentXp = playerSkill.getXp(skill);
                            double xpToNext = plugin.getLeveler().getLevelRequirements().get(level - 2);
                            line = LoreUtil.replace(line,"{progress}", LoreUtil.replace(Lang.getMessage(MenuMessage.PROGRESS, locale)
                                    ,"{percent}", NumberUtil.format2(currentXp / xpToNext * 100)
                                    ,"{current_xp}", NumberUtil.format2(currentXp)
                                    ,"{level_xp}", String.valueOf((int) xpToNext)));
                            break;
                        case "in_progress":
                            line = LoreUtil.replace(line,"{in_progress}", Lang.getMessage(MenuMessage.IN_PROGRESS, locale));
                            break;
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
