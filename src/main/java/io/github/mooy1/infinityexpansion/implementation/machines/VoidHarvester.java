package io.github.mooy1.infinityexpansion.implementation.machines;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.implementation.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinitylib.core.PluginUtils;
import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractMachine;
import io.github.mooy1.infinitylib.slimefun.presets.LorePreset;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
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
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * harvests void bits from... the void
 *
 * @author Mooy1
 */
public final class VoidHarvester extends AbstractMachine implements RecipeDisplayItem {

    public static void setup(InfinityExpansion plugin) {
        new VoidHarvester(Categories.ADVANCED_MACHINES, BASIC, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.TITANIUM, Items.TITANIUM, Items.TITANIUM,
                Items.MACHINE_PLATE, SlimefunItems.GEO_MINER, Items.MACHINE_PLATE,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CORE, Items.MACHINE_CIRCUIT
        }, 120, 1).register(plugin);
        new VoidHarvester(Categories.INFINITY_CHEAT, INFINITE, InfinityWorkbench.TYPE, new ItemStack[] {
                Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE,
                Items.MAGNONIUM, Items.VOID_INGOT, Items.VOID_INGOT, Items.VOID_INGOT, Items.VOID_INGOT, Items.MAGNONIUM,
                Items.MAGNONIUM, Items.VOID_INGOT, Items.INFINITE_CIRCUIT, Items.INFINITE_CIRCUIT, Items.VOID_INGOT, Items.MAGNONIUM,
                Items.MAGNONIUM, Items.VOID_INGOT, Items.INFINITE_CORE, Items.INFINITE_CORE, Items.VOID_INGOT, Items.MAGNONIUM,
                Items.MAGNONIUM, Items.VOID_INGOT, Items.VOID_INGOT, Items.VOID_INGOT, Items.VOID_INGOT, Items.MAGNONIUM,
                Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE
        }, 1200, 32).register(plugin);
    }
    
    public static final SlimefunItemStack BASIC = new SlimefunItemStack(
            "VOID_HARVESTER",
            Material.OBSIDIAN,
            "&8虚空收集者",
            "&7缓慢生产 &8虚空粒&7 ...",
            "",
            LorePreset.speed(1),
            LorePreset.energyPerSecond(120)
    );
    public static final SlimefunItemStack INFINITE = new SlimefunItemStack(
            "INFINITE_VOID_HARVESTER",
            Material.CRYING_OBSIDIAN,
            "&b无尽 &8虚空收集者",
            "&7缓慢生产 &8虚空粒&7 ...",
            "",
            LorePreset.speed(32),
            LorePreset.energyPerSecond(1200)
    );
    
    public static final RecipeType TYPE = new RecipeType(PluginUtils.getKey("void_harvester"), BASIC);
    
    private static final int[] OUTPUT_SLOTS = {
        13
    };
    private static final int STATUS_SLOT = 4;
    private static final int TIME = (int) (100 * InfinityExpansion.getDifficulty()) + 900;

    private final int speed;
    private final int energy;

    private VoidHarvester(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int energy, int speed) {
        super(category, item, type, recipe);
        this.speed = speed;
        this.energy = energy;
        
        registerBlockHandler(getId(), (p, b, stack, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                inv.dropItems(b.getLocation(), OUTPUT_SLOTS);
            }

            return true;
        });
    }

    @Override
    protected int getStatusSlot() {
        return STATUS_SLOT;
    }

    @Override
    protected int getEnergyConsumption() {
        return this.energy;
    }

    @Override
    protected boolean process(@Nonnull BlockMenu inv, @Nonnull Block b, @Nonnull Config data) {
        int progress = Integer.parseInt(getProgress(b));

        if (progress >= TIME) { //reached full progress

            ItemStack output = Items.VOID_BIT;

            if (inv.fits(output, OUTPUT_SLOTS)) {

                inv.pushItem(output.clone(), OUTPUT_SLOTS);

                progress = 0;

            } else if (inv.hasViewer()) {
                inv.replaceExistingItem(STATUS_SLOT, MenuPreset.notEnoughRoom);
                return false;
            }
        } else {
            progress+= this.speed;
        }

        setProgress(b, progress);
        if (inv.hasViewer()) { //update status
            inv.replaceExistingItem(STATUS_SLOT, new CustomItem(Material.LIME_STAINED_GLASS_PANE,
                    "&a生产中 - " + progress * 100 / TIME + "%",
                    "&7(" + progress + "/" + TIME + ")"
            ));
        }
        return true;
    }
    
    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupMenu(blockMenuPreset);
        for (int i = 0; i < 13; i++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i = 14; i < 18; i++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) {
            return OUTPUT_SLOTS;
        }

        return new int[0];
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        if (getProgress(b) == null) {
            setProgress(b, 0);
        }
    }
    
    private static void setProgress(Block b, int progress) {
        BlockStorage.addBlockInfo(b, "progress", String.valueOf(progress));
    }

    private static String getProgress(Block b) {
        return BlockStorage.getLocationInfo(b.getLocation(), "progress");
    }

    @Override
    public int getCapacity() {
        return this.energy * 2;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> items = new ArrayList<>();

        items.add(null);
        items.add(Items.VOID_BIT);

        return items;
    }

    @Nonnull
    @Override
    public String getRecipeSectionLabel(@Nonnull Player p) {
        return "&7生产:";
    }

}

