package io.github.mooy1.infinityexpansion.implementation.machines;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinityexpansion.implementation.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.implementation.materials.Singularity;
import io.github.mooy1.infinityexpansion.setup.categories.Categories;
import io.github.mooy1.infinityexpansion.utils.Triplet;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.abstracts.AbstractMachine;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
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
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Constructs singularities form many items
 *
 * @author Mooy1
 */
public final class SingularityConstructor extends AbstractMachine implements RecipeDisplayItem {

    public static void setup(InfinityExpansion plugin) {
        new SingularityConstructor(Categories.ADVANCED_MACHINES, BASIC, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.MAGSTEEL, Items.MAGSTEEL, Items.MAGSTEEL,
                Items.MACHINE_PLATE, SlimefunItems.CARBON_PRESS_3, Items.MACHINE_PLATE,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CORE, Items.MACHINE_CIRCUIT
        }, 120, 1).register(plugin);
        new SingularityConstructor(Categories.INFINITY_CHEAT, INFINITY, InfinityWorkbench.TYPE, new ItemStack[] {
                null, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE, null,
                null, Items.VOID_INGOT, Items.INFINITE_CIRCUIT, Items.INFINITE_CIRCUIT, Items.VOID_INGOT, null,
                null, Items.VOID_INGOT, BASIC, BASIC, Items.VOID_INGOT, null,
                null, Items.VOID_INGOT, BASIC, BASIC, Items.VOID_INGOT, null,
                null, Items.INFINITY, Items.INFINITE_CORE, Items.INFINITE_CORE, Items.INFINITY, null,
                Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY
        }, 1200, 32).register(plugin);
    }

    public static final SlimefunItemStack BASIC = new SlimefunItemStack(
            "SINGULARITY_CONSTRUCTOR",
            Material.QUARTZ_BRICKS,
            "&f奇异结构",
            "&7用于生产大量资源",
            "",
            LorePreset.speed(1),
            LorePreset.energyPerSecond(120)
    );
    public static final SlimefunItemStack INFINITY = new SlimefunItemStack(
            "INFINITY_CONSTRUCTOR",
            Material.CHISELED_QUARTZ_BLOCK,
            "&b无尽 &f结构",
            "&7用于生产大量资源",
            "",
            LorePreset.speed(32),
            LorePreset.energyPerSecond(1200)
    );
    public static final RecipeType TYPE = new RecipeType(PluginUtils.getKey("singularity_constructor"), BASIC);

    private static final int STATUS_SLOT = 13;
    private static final int INPUT_SLOT = 10;
    private static final int OUTPUT_SLOT = 16;

    private final int speed;
    private final int energy;

    private SingularityConstructor(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int energy, int speed) {
        super(category, item, type, recipe);
        this.speed = speed;
        this.energy = energy;

        registerBlockHandler(getId(), (p, b, stack, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                int progress = getProgress(b);
                Integer progressID = getProgressID(b);
                Location l = b.getLocation();

                inv.dropItems(l, OUTPUT_SLOT, INPUT_SLOT);

                if (progress > 0 && progressID != null) {

                    Triplet<SlimefunItemStack, String, Integer> triplet = Singularity.getRECIPES().get(progressID);

                    if (triplet != null) {
                        ItemStack drop = StackUtils.getItemByIDorType(triplet.getB(), 64);
                        
                        if (drop != null) {

                            int stacks = progress / 64;

                            if (stacks > 0) {
                                for (int i = 0 ; i < stacks ; i++) {
                                    b.getWorld().dropItemNaturally(l, drop);
                                }
                            }

                            int remainder = progress % 64;

                            if (remainder > 0) {
                                drop.setAmount(remainder);
                                b.getWorld().dropItemNaturally(l, drop);
                            }
                        }
                    }
                }
            }

            setProgressID(b, null);
            setProgress(b, 0);

            return true;
        });
    }

    @Override
    protected boolean process(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Config data) {
        ItemStack input = menu.getItemInSlot(INPUT_SLOT);
        String inputID = StackUtils.getIDorTypeOfNullable(input);

        // load data
        Integer progressID = getProgressID(b);
        int progress = getProgress(b);

        Triplet<SlimefunItemStack, String, Integer> triplet;
        boolean takeCharge = false;

        if (progressID == null || progress == 0) {
            // not started
            if (inputID != null) {
                Pair<Integer, Triplet<SlimefunItemStack, String, Integer>> pair = Singularity.getRecipeByID(inputID);
                if (pair != null) {
                    progress = Math.min(this.speed, input.getAmount());
                    input.setAmount(input.getAmount() - progress);
                    progressID = pair.getFirstValue();
                    triplet = pair.getSecondValue();
                    takeCharge = true;
                } else {
                    // invalid input
                    triplet = null;
                }
            } else {
                // still haven't started
                triplet = null;
            }
        } else {
            // started
            triplet = Singularity.getRecipeByIndex(progressID);
            if (inputID != null) {
                int max = Math.min(triplet.getC() - progress, Math.min(this.speed, input.getAmount()));
                if (max > 0) {
                    if (triplet.getB().equals(inputID)) {
                        progress += max;
                        input.setAmount(input.getAmount() - max);
                        takeCharge = true;
                    } // invalid input
                } // already done
            }
        }

        // show status and output if done
        if (triplet != null) {
            if (progress >= triplet.getC() && menu.fits(triplet.getA(), OUTPUT_SLOT)) {
                menu.pushItem(triplet.getA().clone(), OUTPUT_SLOT);
                progress = 0;
                progressID = null;

                if (menu.hasViewer()) {
                    menu.replaceExistingItem(STATUS_SLOT, new CustomItem(
                            Material.LIME_STAINED_GLASS_PANE,
                            "&a生产 " + triplet.getA().getDisplayName() + "...",
                            "&7完成"
                    ));
                }
            } else if (menu.hasViewer()) {
                menu.replaceExistingItem(STATUS_SLOT, new CustomItem(
                        Material.LIME_STAINED_GLASS_PANE,
                        "&a生产 " + triplet.getA().getDisplayName() + "...",
                        "&7" + progress + " / " + triplet.getC()
                ));
            }
        } else if (menu.hasViewer()) {
            invalidInput(menu);
        }

        // save data
        setProgressID(b, progressID);
        setProgress(b, progress);

        return takeCharge;
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupMenu(blockMenuPreset);
        MenuPreset.setupBasicMenu(blockMenuPreset);
    }

    @Override
    protected int getStatusSlot() {
        return STATUS_SLOT;
    }

    @Override
    protected int getEnergyConsumption() {
        return this.energy;
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, ItemStack item) {
        if (flow == ItemTransportFlow.INSERT) {
            return new int[] {INPUT_SLOT};
        } else if (flow == ItemTransportFlow.WITHDRAW) {
            return new int[] {OUTPUT_SLOT};
        } else {
            return new int[0];
        }
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block) {
        invalidInput(blockMenu);
    }
    
    private static void invalidInput(BlockMenu menu) {
        menu.replaceExistingItem(STATUS_SLOT, new CustomItem(
                Material.RED_STAINED_GLASS_PANE,
                "&c清放入正确的一对结构"
        ));
    }
    
    private static void setProgress(Block b, int progress) {
        BlockStorage.addBlockInfo(b, "progress", String.valueOf(progress));
    }

    private static void setProgressID(Block b, @Nullable Integer progressID) {
        if (progressID == null) {
            BlockStorage.addBlockInfo(b, "progressid", null);
        } else {
            BlockStorage.addBlockInfo(b, "progressid", String.valueOf(progressID));
        }
    }

    private static int getProgress(Block b) {
        try {
            return Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "progress"));
        } catch (NumberFormatException e) {
            setProgress(b, 0);
            return 0;
        }
    }

    private static Integer getProgressID(Block b) {
        String id = BlockStorage.getLocationInfo(b.getLocation(), "progressid");
        if (id == null) {
            return null;
        } else try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            setProgressID(b, null);
            return null;
        }
    }

    @Override
    public int getCapacity() {
        return this.energy * 2;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> items = new ArrayList<>();

        for (int i = 0 ; i < Singularity.getRECIPES().size() ; i++) {
            Triplet<SlimefunItemStack, String, Integer> triplet = Singularity.getRECIPES().get(i);
            items.add(StackUtils.getItemByIDorType(triplet.getB(), 1));
            items.add(triplet.getA());
        }

        return items;
    }

}
