package io.github.mooy1.infinityexpansion.implementation.abstracts;

import io.github.mooy1.infinityexpansion.utils.Util;
import io.github.mooy1.infinitylib.players.MessageUtils;
import io.github.mooy1.infinitylib.recipes.largestrict.StrictLargeOutput;
import io.github.mooy1.infinitylib.recipes.largestrict.StrictLargeRecipeMap;
import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractTicker;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.mooy1.infinitylib.slimefun.utils.DelayedRecipeType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An abstract crafter
 * 
 * @author Mooy1
 *
 */
public abstract class AbstractCrafter extends AbstractTicker {
    
    protected static final int[] INPUT_SLOTS = MenuPreset.craftingInput;
    private static final int[] OUTPUT_SLOT = MenuPreset.craftingOutput;
    private static final int[] BACKGROUND = {5, 6, 7, 8, 41, 42, 43, 44};
    private static final int[] STATUS_BORDER = {14, 32};
    private static final int STATUS_SLOT = 23;

    private final StrictLargeRecipeMap recipes = new StrictLargeRecipeMap(9);
    
    public AbstractCrafter(Category category, SlimefunItemStack stack, DelayedRecipeType recipeType, RecipeType type, ItemStack[] recipe) {
        super(category, stack, type, recipe);

        recipeType.acceptEach(this.recipes::put);

        registerBlockHandler(getId(), (p, b, slimefunItem, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);
            if (inv != null) {
                inv.dropItems(b.getLocation(), OUTPUT_SLOT);
                inv.dropItems(b.getLocation(), INPUT_SLOTS);
            }
            return true;
        });
    }

    @Nonnull
    @Override
    protected final int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, ItemStack item) {
        return new int[0];
    }

    @Override
    public final void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int slot : MenuPreset.craftingInputBorder) {
            blockMenuPreset.addItem(slot, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : MenuPreset.craftingOutputBorder) {
            blockMenuPreset.addItem(slot, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : BACKGROUND) {
            blockMenuPreset.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : STATUS_BORDER) {
            blockMenuPreset.addItem(slot, MenuPreset.borderItemStatus, ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(STATUS_SLOT, MenuPreset.invalidInput, ChestMenuUtils.getEmptyClickHandler());
    }
    
    public boolean preCraftFail(@Nonnull Location l, @Nonnull BlockMenu inv) {
        return false;
    }

    @Nullable
    public ItemStack preCraftItem(@Nonnull Location l, @Nonnull BlockMenu inv) {
        return null;
    }
    
    @Nullable
    public String preCraftMessage(@Nonnull Location l, @Nonnull BlockMenu inv) {
        return null;
    }
    
    public abstract void postCraft(@Nonnull Location l, @Nonnull BlockMenu inv, @Nonnull Player p);

    @Override
    public final void tick(@Nonnull BlockMenu inv, @Nonnull Block b, @Nonnull Config config) {
        if (inv.hasViewer()) {
            if (preCraftFail(b.getLocation(), inv)) {
                inv.replaceExistingItem(STATUS_SLOT, preCraftItem(b.getLocation(), inv));
                return;
            }

            Pair<ItemStack, int[]> output = getOutput(inv);

            if (output == null) {

                inv.replaceExistingItem(STATUS_SLOT, MenuPreset.invalidRecipe);

            } else {

                inv.replaceExistingItem(STATUS_SLOT, Util.getDisplayItem(output.getFirstValue().clone()));

            }
        }
    }
    
    private void craft(@Nonnull BlockMenu inv, @Nonnull Player p) {
        if (preCraftFail(inv.getLocation(), inv)) {
            inv.replaceExistingItem(STATUS_SLOT, preCraftItem(inv.getLocation(), inv));
            String msg = preCraftMessage(inv.getLocation(), inv);
            if (msg != null) MessageUtils.messageWithCD(p, 1000, msg);
            return;
        }
        
        Pair<ItemStack, int[]> output = getOutput(inv);
        
        if (output == null) { //invalid

            inv.replaceExistingItem(STATUS_SLOT, MenuPreset.invalidRecipe);
            MessageUtils.messageWithCD(p, 1000, ChatColor.RED + "Invalid Recipe!");

        } else {
            
            if (!inv.fits(output.getFirstValue(), OUTPUT_SLOT)) { //not enough room

                inv.replaceExistingItem(STATUS_SLOT, MenuPreset.notEnoughRoom);
                MessageUtils.messageWithCD(p, 1000, ChatColor.GOLD + "Not enough room!");

            } else { //enough room

                for (int i = 0 ; i < INPUT_SLOTS.length ; i++) {
                    int amount = output.getSecondValue()[i];
                    if (amount > 0) {
                        inv.consumeItem(INPUT_SLOTS[i], amount);
                    }
                }
                MessageUtils.messageWithCD(p, 1000, ChatColor.GREEN + "已合成: " + ItemUtils.getItemName(output.getFirstValue()));

                postCraft(inv.getLocation(), inv, p);

                inv.pushItem(output.getFirstValue(), OUTPUT_SLOT);

            }
        }
    }
    
    @Nullable
    private Pair<ItemStack, int[]> getOutput(@Nonnull BlockMenu inv) {
        StrictLargeOutput output = this.recipes.get(inv, INPUT_SLOTS);
        if (output == null) {
            return null;
        }
        ItemStack out = output.getOutput().clone();
        modifyOutput(inv, out);
        return new Pair<>(out, output.getInputConsumption());
    }
    
    protected void modifyOutput(@Nonnull BlockMenu inv, @Nonnull ItemStack output) {
        
    }

    @Override
    public final void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        menu.addMenuClickHandler(STATUS_SLOT, (p, slot, item, action) -> {
            craft(menu, p);
            return false;
        });
    }

}
