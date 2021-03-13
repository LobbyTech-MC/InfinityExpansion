package io.github.mooy1.infinityexpansion.categories;

import io.github.mooy1.infinitylib.PluginUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;

/**
 * Categories for this addon
 *
 * @author Mooy1
 */
public final class Categories {
    
    public static final Category MAIN_MATERIALS = new SubCategory(PluginUtils.getKey("main_materials"),
            new CustomItem(Material.NETHER_STAR, "&b无尽 &7科技")
    );

    public static final Category BASIC_MACHINES = new SubCategory(PluginUtils.getKey("basic_machines"),
            new CustomItem(Material.LOOM, "&9基础 &7机器")
    );

    public static final Category ADVANCED_MACHINES = new SubCategory(PluginUtils.getKey("advanced_machines"),
            new CustomItem(Material.BLAST_FURNACE, "&c高级 &7机器")
    );

    public static final Category STORAGE_TRANSPORT = new SubCategory(PluginUtils.getKey("storage_transport"),
            new CustomItem(Material.BEEHIVE, "&6存储")
    );
    
    public static final Category MOB_SIMULATION = new SubCategory(PluginUtils.getKey("mob_simulation"),
            new CustomItem(Material.BEACON, "&b怪物分析")
    );
    
    public static final Category INFINITY_MATERIALS = new SubCategory(PluginUtils.getKey("infinity_materials"),
            new CustomItem(Material.NETHERITE_BLOCK, "&b无尽 &a材料")
    );
    
    public static final Category INFINITY_CHEAT = new SubCategory(PluginUtils.getKey("infinity_cheat"),
            new CustomItem(Material.RESPAWN_ANCHOR, "&b无尽 &7配方 &c- 不正确的配方")
    );
    
}