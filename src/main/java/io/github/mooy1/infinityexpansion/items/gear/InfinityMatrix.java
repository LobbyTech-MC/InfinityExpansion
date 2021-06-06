package io.github.mooy1.infinityexpansion.items.gear;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public final class InfinityMatrix extends SimpleSlimefunItem<ItemUseHandler> implements Listener, Soulbound, NotPlaceable {
    
    public InfinityMatrix(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(category, item, type, recipe);
        InfinityExpansion.inst().registerListener(this);
    }

    private static void disableFlight(Player p) {
        p.sendMessage( ChatColor.RED + "无尽飞行已关闭!");
        p.setAllowFlight(false);
    }

    private static void enableFlight(Player p) {
        p.sendMessage( ChatColor.GREEN + "无尽飞行已开启!");
        p.setAllowFlight(true);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            ItemStack item = e.getItem();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore == null) {
                return;
            }

            Player p = e.getPlayer();

            Iterator<String> iterator = lore.listIterator();

            while (iterator.hasNext()) {
                String line = iterator.next();

                if (ChatColor.stripColor(line).contains("飞行器绑定于: ")) {
                    String uuid = ChatColor.stripColor(line).substring(6);

                    if (!p.getUniqueId().toString().equals(uuid)) {
                        p.sendMessage( ChatColor.YELLOW + "这个飞行器不归你所有!");
                        return;
                    }

                    if (p.isSneaking()) { //remove owner
                        iterator.remove();
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        p.sendMessage( ChatColor.GOLD + "已解除绑定!");
                        disableFlight(p);

                    } else if (p.getAllowFlight()) {
                        disableFlight(p);
                    } else {
                        enableFlight(p);
                    }

                    return;
                }
            }

            lore.add(ChatColor.GREEN + "飞行器绑定于: " + p.getDisplayName());
            meta.setLore(lore);
            item.setItemMeta(meta);
            p.sendMessage( ChatColor.GOLD + "成功绑定!");
            enableFlight(p);
        };
    }

}