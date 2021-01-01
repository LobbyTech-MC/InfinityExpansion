package io.github.mooy1.infinityexpansion.utils;

import io.github.mooy1.infinitylib.items.LoreUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of utils for making recipes
 *
 * @author Mooy1
 */
public final class RecipeUtils {
    
    @Nonnull
    public static ItemStack[] Compress(@Nonnull ItemStack item) {
        return new ItemStack[]{
                item, item, item, item, item, item, item, item, item,
        };
    }

    @Nonnull
    public static ItemStack[] MiddleItem(@Nonnull ItemStack item) {
        return new ItemStack[] {
                null, null, null, null , item, null, null, null, null
        };
    }

    @Nonnull
    public static ItemStack getDisplayItem(@Nonnull ItemStack output) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "");
        lore.add(ChatColor.GREEN + "-------------------");
        lore.add(ChatColor.GREEN + "\u21E8 点击制作");
        lore.add(ChatColor.GREEN + "-------------------");
        
        LoreUtils.addLore(output, lore);

        return output;
    }
}
