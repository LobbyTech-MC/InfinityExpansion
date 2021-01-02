package io.github.mooy1.infinityexpansion.lists;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.setup.SubCategory;
import io.github.mooy1.infinityexpansion.setup.InfinityCategory;
import io.github.mooy1.infinityexpansion.setup.MainCategory;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

/**
 * Categories for this addon
 *
 * @author Mooy1
 */
public final class Categories {
    
    private static final InfinityExpansion instance = InfinityExpansion.getInstance();
    
    public static final MainCategory MAIN = new MainCategory(new NamespacedKey(instance, "main"), 
            new CustomItem(Material.NETHER_STAR, "&b无限&7拓展"), -1
    );

    public static final Category MAIN_MATERIALS = new SubCategory(new NamespacedKey(instance, "main_materials"),
            new CustomItem(Material.NETHER_STAR, "&b无限&7拓展"), 2
    );

    public static final Category BASIC_MACHINES = new SubCategory(new NamespacedKey(instance, "basic_machines"),
            new CustomItem(Material.LOOM, "&9基础&7机械"), 2
    );

    public static final Category ADVANCED_MACHINES = new SubCategory(new NamespacedKey(instance, "advanced_machines"),
            new CustomItem(Material.BLAST_FURNACE, "&c高级的&7机械"), 2
    );

    public static final Category STORAGE_TRANSPORT = new SubCategory(new NamespacedKey(instance, "storage_transport"),
            new CustomItem(Material.ENDER_CHEST, "&6储存"), 2
    );
    
    public static final Category MOB_SIMULATION = new SubCategory(new NamespacedKey(instance, "mob_simulation"),
            new CustomItem(Material.SPAWNER, "&b生物模拟"), 2
    );
    
    public static final Category INFINITY_MATERIALS = new SubCategory(new NamespacedKey(instance, "infinity_materials"),
            new CustomItem(Material.NETHERITE_BLOCK, "&b无限&a材料"), 2
    );

    public static final Category INFINITY_RECIPES = new InfinityCategory(new NamespacedKey(instance, "infinity_recipes"),
            new CustomItem(Material.SMITHING_TABLE, "&b无限&7配方"), 2
    );
    
    public static final Category INFINITY_CHEAT = new SubCategory(new NamespacedKey(instance, "infinity_cheat"),
            new CustomItem(Material.SMITHING_TABLE, "&b无限&7配方 &c- 不是真的配方!"), 2
    );
    
}
