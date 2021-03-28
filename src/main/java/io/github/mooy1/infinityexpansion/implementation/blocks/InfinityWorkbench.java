package io.github.mooy1.infinityexpansion.implementation.blocks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.categories.InfinityCategory;
import io.github.mooy1.infinityexpansion.implementation.abstracts.AbstractEnergyCrafter;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinityexpansion.utils.Util;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.mooy1.infinitylib.slimefun.recipes.RecipeMap;
import io.github.mooy1.infinitylib.slimefun.recipes.SimpleRecipeMap;
import io.github.mooy1.infinitylib.slimefun.recipes.inputs.MultiInput;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

/**
 * A 6x6 crafting table O.o
 *
 * @author Mooy1
 */
public final class InfinityWorkbench extends AbstractEnergyCrafter {

    private static final int ENERGY = 10_000_000;
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "INFINITY_FORGE",
            Material.RESPAWN_ANCHOR,
            "&6无尽工作台",
            "&7用于合成无尽物品",
            "",
            "&8\u21E8 &e\u26A1 &7100,000 J " + "/个物品"
    );
    
    public static final int[] INPUT_SLOTS = {
        0, 1, 2, 3, 4, 5,
        9, 10, 11, 12, 13, 14,
        18, 19, 20, 21, 22, 23,
        27, 28, 29, 30, 31, 32,
        36, 37, 38, 39, 40, 41,
        45, 46, 47, 48, 49, 50
    };
    
    private static final int[] OUTPUT_SLOTS = {MenuPreset.slot3 + 27};
    private static final int STATUS_SLOT = MenuPreset.slot3;
    private static final int[] STATUS_BORDER = {6, 8, 15, 17, 24, 25, 26};
    private static final int RECIPE_SLOT = 7;
    
    public static final RecipeMap<MultiInput, ItemStack> RECIPES = new SimpleRecipeMap<>();
    public static final LinkedHashMap<String, Pair<SlimefunItemStack, ItemStack[]>> ITEMS = new LinkedHashMap<>();
    public static final List<String> IDS = new ArrayList<>();
    
    public static final RecipeType TYPE = new RecipeType(InfinityExpansion.inst().getKey("infinity_forge"), ITEM, (stacks, stack) -> {
        SlimefunItemStack item = (SlimefunItemStack) stack;
        RECIPES.put(new MultiInput(stacks), item);
        ITEMS.put(item.getItemId(), new Pair<>(item, stacks));
        IDS.add(item.getItemId());
    }, "", "&c请放入正确的无尽配方!");
    
    public InfinityWorkbench() {
        super(Categories.MAIN_MATERIALS, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.VOID_INGOT, Items.MACHINE_PLATE, Items.VOID_INGOT,
                SlimefunItems.ENERGIZED_CAPACITOR, new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.ENERGIZED_CAPACITOR,
                Items.VOID_INGOT, Items.MACHINE_PLATE, Items.VOID_INGOT
        }, ENERGY, STATUS_SLOT);
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu inv, @Nonnull Location l) {
        inv.dropItems(l, OUTPUT_SLOTS);
        inv.dropItems(l, INPUT_SLOTS);
    }

    @Override
    public void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i : MenuPreset.slotChunk3) {
            blockMenuPreset.addItem(i + 27, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : STATUS_BORDER) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemStatus, ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(RECIPE_SLOT, new CustomItem(Material.BOOK, "&6配方"), ChestMenuUtils.getEmptyClickHandler());
        blockMenuPreset.addItem(STATUS_SLOT, MenuPreset.invalidInput, ChestMenuUtils.getEmptyClickHandler());
    }
    
    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        menu.addMenuClickHandler(STATUS_SLOT, (p, slot, item, action) -> {
            craft(b, menu, p);
            return false;
        });
        menu.addMenuClickHandler(RECIPE_SLOT, (p, slot, item, action) -> {
            InfinityCategory.open(p, new InfinityCategory.BackEntry(menu, null), true);
            return false;
        });
    }

    public void craft(@Nonnull Block b, @Nonnull BlockMenu inv, @Nonnull  Player p) {
        int charge = getCharge(b.getLocation());
         
        if (charge < ENERGY) { //not enough energy
            p.sendMessage( new String[] {
                    ChatColor.RED + "电力不足!",
                    ChatColor.GREEN + "电力: " + ChatColor.RED + charge + ChatColor.GREEN + "/" + ENERGY + " J"
            });
            return;
        }
        
        ItemStack output = RECIPES.get(new MultiInput(inv, INPUT_SLOTS));
        
        if (output == null) { //invalid
            p.sendMessage( ChatColor.RED + "无效的配方!");
            return;
        }
            
        if (!inv.fits(output, OUTPUT_SLOTS)) { //not enough room
            p.sendMessage( ChatColor.GOLD + "没有足够的空间!");
            return;
        }

        for (int slot : INPUT_SLOTS) {
            if (inv.getItemInSlot(slot) != null) {
                inv.consumeItem(slot);
            }
        }

        p.sendMessage( ChatColor.GREEN + "成功合成: " + ChatColor.WHITE + output.getItemMeta().getDisplayName());

        inv.pushItem(output.clone(), OUTPUT_SLOTS);
        setCharge(b.getLocation(), 0);
            
    }
    
    @Override
    public void update(@Nonnull BlockMenu inv) {

        ItemStack output = RECIPES.get(new MultiInput(inv, INPUT_SLOTS));

        if (output == null) { //invalid

            inv.replaceExistingItem(STATUS_SLOT, MenuPreset.invalidRecipe);

        } else { //correct recipe

            inv.replaceExistingItem(STATUS_SLOT, Util.getDisplayItem(output.clone()));
            
        }
        
    }

}
