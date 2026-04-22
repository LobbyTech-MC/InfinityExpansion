package io.github.mooy1.infinityexpansion.items.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.items.abstracts.AbstractEnergyCrafter;
import io.github.mooy1.infinityexpansion.utils.NbtProtectedSlot;
import io.github.mooy1.infinityexpansion.utils.Util;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

/**
 * Combines slimefun items, exceeds vanilla anvil limits
 *
 * @author Mooy1
 */
public final class AdvancedAnvil extends AbstractEnergyCrafter {

    private static final Map<Enchantment, Integer> MAX_LEVELS = Util.getEnchants(Objects.requireNonNull(
            InfinityExpansion.config().getConfigurationSection("advanced-anvil-max-levels")
    ));
    private static final ItemStack ANVIL_SLOT = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " ");
    private static final int[] INPUT_SLOTS = {
            10, 13
    };
    private static final int[] OUTPUT_SLOTS = {
            16
    };
    private static final int STATUS_SLOT = 40;
    private static final int[] ANVIL_SLOTS = {
            30, 31, 32, 39, 41, 47, 48, 49, 50, 51

    };
    private static final int[] BACKGROUND = {
            27, 28, 29, 33, 34, 35,
            36, 37, 38, 42, 43, 44,
            45, 46, 52, 53
    };

    public AdvancedAnvil(ItemGroup category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int energy) {
        super(category, item, type, recipe, energy, STATUS_SLOT);
    }

    @Override
    protected void setup(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(BACKGROUND);
        blockMenuPreset.drawBackground(ANVIL_SLOT, ANVIL_SLOTS);
        blockMenuPreset.drawBackground(INPUT_BORDER, new int[] {
                0, 1, 2, 3, 4, 5,
                9, 11, 12, 14,
                18, 19, 20, 21, 22, 23
        });
        blockMenuPreset.drawBackground(OUTPUT_BORDER, new int[] {
                6, 7, 8,
                15, 17,
                24, 25, 26
        });
        blockMenuPreset.addItem(STATUS_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    @Override
    protected int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
    	
        menu.addMenuClickHandler(STATUS_SLOT, (player, i, itemStack, clickAction) -> {
        	NbtProtectedSlot.protectSlot(player, player.getOpenInventory().getTopInventory(), STATUS_SLOT, itemStack, JavaPlugin.getPlugin(InfinityExpansion.class));
            craft(menu, b, player);
            return false;
        });
    }

    private void craft(BlockMenu inv, Block b, Player p) {
        Location l = b.getLocation();
        if (getCharge(l) < this.energy) { //not enough energy
            p.sendMessage(ChatColor.RED + "电力不足!",
                    ChatColor.GREEN + "当前电力: " + ChatColor.RED + getCharge(l) + ChatColor.GREEN + "/" + this.energy + " J");
            return;
        }

        ItemStack item1 = inv.getItemInSlot(INPUT_SLOTS[0]);
        SlimefunItem sfItem1 = SlimefunItem.getByItem(inv.getItemInSlot(INPUT_SLOTS[0]));
        ItemStack item2 = inv.getItemInSlot(INPUT_SLOTS[1]);
        SlimefunItem sfItem2 = SlimefunItem.getByItem(inv.getItemInSlot(INPUT_SLOTS[1]));

        if (item1 == null || item2 == null || (item2.getType() != Material.ENCHANTED_BOOK && item1.getType() != item2.getType())) {
            p.sendMessage(ChatColor.RED + "物品不存在!");
            return;
        }

        if(sfItem2 != null && !sfItem2.isDisenchantable()){
            p.sendMessage(ChatColor.RED + "该粘液物品无法祛魔!");
            return;
        }

        if(sfItem1 != null && !sfItem1.isEnchantable()){
            p.sendMessage(ChatColor.RED + "该粘液物品无法附魔!");
            return;
        }

        ItemStack output = getOutput(item1, item2);

        if (output == null) {
            p.sendMessage(ChatColor.RED + "无法升级!");
            return;
        }

        if (!inv.fits(output, OUTPUT_SLOTS)) {
            p.sendMessage(ChatColor.GOLD + "空间不足!");
            return;
        }

        p.playSound(l, Sound.BLOCK_ANVIL_USE, 1, 1);
        item1.setAmount(item1.getAmount() - 1);
        item2.setAmount(item2.getAmount() - 1);
        inv.pushItem(output, OUTPUT_SLOTS);
        removeCharge(l, this.energy);
        update(inv);
    }

    @Nullable
    private static ItemStack getOutput(@Nonnull ItemStack item1, @Nonnull ItemStack item2) {
        Map<Enchantment, Integer> enchants1 = getEnchants(item1.getItemMeta());
        Map<Enchantment, Integer> enchants2 = getEnchants(item2.getItemMeta());
        if (enchants1.size() == 0 && enchants2.size() == 0) {
            return null;
        }
        return combineEnchants(Maps.difference(enchants1, enchants2), item1, item2);
    }

    @Nonnull
    private static Map<Enchantment, Integer> getEnchants(@Nonnull ItemMeta meta) {
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta book = (EnchantmentStorageMeta) meta;
            if (book.hasStoredEnchants()) {
                return book.getStoredEnchants();
            }
        }
        else if (meta.hasEnchants()) {
            return meta.getEnchants();
        }

        return new HashMap<>();
    }

    private static void setEnchants(@Nonnull ItemStack item, @Nonnull ItemMeta meta, @Nonnull Map<Enchantment, Integer> enchants) {
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta book = (EnchantmentStorageMeta) meta;
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                book.addStoredEnchant(entry.getKey(), entry.getValue(), true);
            }
            item.setItemMeta(book);
        }
        else {
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }
    }

    @Nullable
    private static ItemStack combineEnchants(@Nonnull MapDifference<Enchantment, Integer> dif, @Nonnull ItemStack item1, @Nonnull ItemStack item2) {
        ItemStack item = item1.clone();
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();

        Map<Enchantment, Integer> enchants = new HashMap<>();

        boolean changed = false;

        //upgrades (same enchant and level)
        for (Map.Entry<Enchantment, Integer> e : dif.entriesInCommon().entrySet()) {
            if (MAX_LEVELS.containsKey(e.getKey()) && e.getValue() < MAX_LEVELS.get(e.getKey())) {
                enchants.put(e.getKey(), e.getValue() + 1);
                changed = true;
            }
        }

        //override (same enchant different level)
        for (Map.Entry<Enchantment, MapDifference.ValueDifference<Integer>> e : dif.entriesDiffering().entrySet()) {
            if (e.getValue().rightValue() > e.getValue().leftValue()) {
                enchants.put(e.getKey(), e.getValue().rightValue());
                changed = true;
            }
        }

        boolean bookOntoTool = item2.getType() == Material.ENCHANTED_BOOK && item1.getType() != Material.ENCHANTED_BOOK;

        //unique (different enchants from 2nd item)
        for (Map.Entry<Enchantment, Integer> e : dif.entriesOnlyOnRight().entrySet()) {
            if (bookOntoTool) {
                if (!e.getKey().canEnchantItem(item)) {
                    continue;
                }
            }
            enchants.put(e.getKey(), e.getValue());
            changed = true;
        }

        if (changed) {
            setEnchants(item, meta, enchants);
            return item;
        }
        else {
            return null;
        }
    }

    
    public static boolean isEnchantable(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (item.getType() == Material.ENCHANTED_BOOK) return true;

        Material material = item.getType();
        String name = material.name();

        // 基于名称模式匹配，兼容所有版本，包括1.21新增的MACE
        return name.contains("MACE") ||           
               name.contains("_SWORD") ||
               name.contains("_PICKAXE") ||
               name.contains("_AXE") ||
               name.contains("_SHOVEL") ||
               name.contains("_HOE") ||
               name.contains("_HELMET") ||
               name.contains("_CHESTPLATE") ||
               name.contains("_LEGGINGS") ||
               name.contains("_BOOTS") ||
               name.equals("BOW") ||
               name.equals("CROSSBOW") ||
               name.equals("TRIDENT") ||
               name.equals("FISHING_ROD") ||
               name.equals("SHEARS") ||
               name.equals("FLINT_AND_STEEL") ||
               name.equals("CARROT_ON_A_STICK") ||
               name.equals("WARPED_FUNGUS_ON_A_STICK") ||
               name.equals("ELYTRA") ||
               name.equals("SHIELD") ||
               name.equals("TURTLE_HELMET");
    }
    
    public static boolean hasAtLeastOneCompatibleEnchantment(ItemStack target, ItemStack sacrifice) {
        if (target == null || sacrifice == null) return false;
        
        Map<Enchantment, Integer> sacrificeEnchants = getEnchants(sacrifice.getItemMeta());
        
        for (Enchantment enchant : sacrificeEnchants.keySet()) {
            // 检查附魔是否适用于目标物品
            if (enchant.canEnchantItem(target)) {
                // 检查是否与现有附魔冲突
                boolean hasConflict = false;
                Map<Enchantment, Integer> targetEnchants = getEnchants(target.getItemMeta());
                for (Enchantment existing : targetEnchants.keySet()) {
                    if (enchant.conflictsWith(existing)) {
                        hasConflict = true;
                        break;
                    }
                }
                if (!hasConflict) {
                    return true; // 找到至少一个兼容的附魔
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void update(@Nonnull BlockMenu inv) {
        ItemStack item1 = inv.getItemInSlot(INPUT_SLOTS[0]);
        ItemStack item2 = inv.getItemInSlot(INPUT_SLOTS[1]);

        // 在你的铁砧检查方法中
        if (item1 == null || item2 == null) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.BARRIER, "&c无效物品!"));
            return;
        }

        // 检查目标物品是否可被附魔
        if (!isEnchantable(item1) && item2.getType() != Material.ENCHANTED_BOOK) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.BARRIER, "&c该物品无法被附魔!"));
            return;
        }

        // 检查物品类型匹配（非附魔书的情况）
        if (item2.getType() != Material.ENCHANTED_BOOK && item1.getType() != item2.getType()) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.BARRIER, "&c物品类型不匹配!"));
            return;
        }

        // 核心检查：牺牲品的附魔是否兼容目标物品
        if (!hasAtLeastOneCompatibleEnchantment(item1, item2)) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.BARRIER, "&c附魔不兼容!"));
            return;
        }

        // 所有检查通过，继续处理...

        ItemStack output = getOutput(item1, item2);

        if (output == null) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.BARRIER, "&c无法升级!"));
            return;
        }

        inv.replaceExistingItem(STATUS_SLOT, Util.getDisplayItem(output));

    }

}
