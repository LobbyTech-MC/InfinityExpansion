package io.github.mooy1.infinityexpansion.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NbtProtectedSlot {
    
    private static NamespacedKey PROTECTED_KEY;
    
    public static void init(JavaPlugin plugin) {
        PROTECTED_KEY = new NamespacedKey(plugin, "protected_slot");
    }
    
    public static ItemStack markProtected(ItemStack item, int slotId) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(PROTECTED_KEY, 
            PersistentDataType.INTEGER, slotId);
        item.setItemMeta(meta);
        return item;
    }
    
    public static boolean isProtected(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                   .has(PROTECTED_KEY, PersistentDataType.INTEGER);
    }
    
    public static void protectSlot(Player player, Inventory inventory, 
                                   int slot, ItemStack protectedItem, 
                                   JavaPlugin plugin) {
        
        // 标记物品
        ItemStack markedItem = markProtected(protectedItem.clone(), slot);
        inventory.setItem(slot, markedItem);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || 
                    player.getOpenInventory().getTopInventory() != inventory) {
                    this.cancel();
                    return;
                }
                
                ItemStack current = inventory.getItem(slot);
                
                // 检查槽位中的物品是否还是原来的受保护物品
                if (current == null || !isProtected(current)) {
                    // 物品丢失或未被标记 → 恢复
                    ItemStack restored = markProtected(protectedItem.clone(), slot);
                    inventory.setItem(slot, restored);
                    
                    // 检查玩家光标上是否有受保护的物品
                    ItemStack cursor = player.getItemOnCursor();
                    if (isProtected(cursor)) {
                        player.setItemOnCursor(null); // 清除光标
                        player.sendMessage("§c你不能拿走这个物品！");
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}