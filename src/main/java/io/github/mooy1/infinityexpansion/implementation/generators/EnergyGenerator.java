package io.github.mooy1.infinityexpansion.implementation.generators;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.AllArgsConstructor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.implementation.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinityexpansion.utils.Util;
import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractContainer;
import io.github.mooy1.infinitylib.slimefun.presets.LorePreset;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
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

/**
 * Solar panels and some other basic generators
 *
 * @author Mooy1
 *
 * Thanks to panda for some stuff to work off of
 */
public final class EnergyGenerator extends AbstractContainer implements EnergyNetProvider {
    

	private static final int HYDRO_ENERGY = 5;
    private static final int ADVANCED_HYDRO_ENERGY = 45;
    private static final int GEO_ENERGY = 35;
    private static final int ADVANCED_GEO_ENERGY = 210;
    private static final int BASIC_SOLAR_ENERGY = 9;
    private static final int ADVANCED_SOLAR_ENERGY = 150;
    private static final int CELESTIAL_ENERGY = 750;
    private static final int VOID_ENERGY = 3000;
    private static final int INFINITY_ENERGY = 60000;
    
    public final static void setup(InfinityExpansion plugin) {
    	new EnergyGenerator(Categories.BASIC_MACHINES, HYDRO_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.MAGSTEEL, Items.MACHINE_CIRCUIT, Items.MAGSTEEL,
                new ItemStack(Material.BUCKET), SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.BUCKET),
                Items.MAGSTEEL, Items.MACHINE_CIRCUIT, Items.MAGSTEEL
        }, HYDRO_ENERGY, Type.WATER1).register(plugin);
        new EnergyGenerator(Categories.ADVANCED_MACHINES, REINFORCED_HYDRO_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                HYDRO_GENERATOR, Items.MACHINE_CIRCUIT, HYDRO_GENERATOR,
                Items.MAGSTEEL_PLATE, Items.MACHINE_CORE, Items.MAGSTEEL_PLATE,
                HYDRO_GENERATOR, Items.MACHINE_CIRCUIT, HYDRO_GENERATOR
        }, ADVANCED_HYDRO_ENERGY, Type.WATER2).register(plugin);

        new EnergyGenerator(Categories.ADVANCED_MACHINES, GEOTHERMAL_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.MAGSTEEL_PLATE, Items.MAGSTEEL_PLATE, Items.MAGSTEEL_PLATE,
                SlimefunItems.LAVA_GENERATOR_2, SlimefunItems.LAVA_GENERATOR_2, SlimefunItems.LAVA_GENERATOR_2,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CORE, Items.MACHINE_CIRCUIT
        }, GEO_ENERGY, Type.GEOTHERMAL1).register(plugin);
        new EnergyGenerator(Categories.ADVANCED_MACHINES, REINFORCED_GEOTHERMAL_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                GEOTHERMAL_GENERATOR, Items.MACHINE_CIRCUIT, GEOTHERMAL_GENERATOR,
                Items.MACHINE_PLATE, Items.MACHINE_CORE, Items.MACHINE_PLATE,
                GEOTHERMAL_GENERATOR, Items.MACHINE_CIRCUIT, GEOTHERMAL_GENERATOR
        }, ADVANCED_GEO_ENERGY, Type.GEOTHERMAL2).register(plugin);

        new EnergyGenerator(Categories.BASIC_MACHINES, BASIC_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.MAGSTEEL, Items.MAGSTEEL_PLATE, Items.MAGSTEEL,
                SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL, SlimefunItems.SOLAR_PANEL,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CIRCUIT, Items.MACHINE_CIRCUIT
        }, BASIC_SOLAR_ENERGY, Type.SOLAR1).register(plugin);
        new EnergyGenerator(Categories.ADVANCED_MACHINES, ADVANCED_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                BASIC_PANEL, BASIC_PANEL, BASIC_PANEL,
                Items.TITANIUM, SlimefunItems.SOLAR_GENERATOR_4, Items.TITANIUM,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CIRCUIT, Items.MACHINE_CIRCUIT
        }, ADVANCED_SOLAR_ENERGY, Type.SOLAR2).register(plugin);

        new EnergyGenerator(Categories.ADVANCED_MACHINES, CELESTIAL_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.MACHINE_PLATE, Items.MACHINE_PLATE, Items.MACHINE_PLATE,
                ADVANCED_PANEL, ADVANCED_PANEL, ADVANCED_PANEL,
                Items.MACHINE_CIRCUIT, Items.MACHINE_CORE, Items.MACHINE_CIRCUIT
        }, CELESTIAL_ENERGY, Type.SOLAR3).register(plugin);
        new EnergyGenerator(Categories.ADVANCED_MACHINES, VOID_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.VOID_INGOT, Items.VOID_INGOT, Items.VOID_INGOT,
                CELESTIAL_PANEL, CELESTIAL_PANEL, CELESTIAL_PANEL,
                Items.MAGNONIUM, Items.MAGNONIUM, Items.MAGNONIUM
        }, VOID_ENERGY, Type.LUNAR).register(plugin);

        new EnergyGenerator(Categories.INFINITY_CHEAT, INFINITE_PANEL, InfinityWorkbench.TYPE, new ItemStack[] {
                EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL,
                EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL, EnergyGenerator.CELESTIAL_PANEL,
                Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY,
                Items.INFINITY, Items.INFINITE_CIRCUIT, Items.INFINITE_CORE, Items.INFINITE_CORE, Items.INFINITE_CIRCUIT, Items.INFINITY,
                Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY,
                EnergyGenerator.VOID_PANEL, EnergyGenerator.VOID_PANEL, EnergyGenerator.VOID_PANEL, EnergyGenerator.VOID_PANEL, EnergyGenerator.VOID_PANEL, EnergyGenerator.VOID_PANEL
        }, INFINITY_ENERGY, Type.INFINITY).register(plugin);
    }
    

	public static final SlimefunItemStack HYDRO_GENERATOR = new SlimefunItemStack(
            "HYDRO_GENERATOR",
            Material.PRISMARINE_WALL,
            "&9水力发电机",
            "&7使用流动的水发电",
            "",
            "&8\u21E8 &e\u26A1 &7 500 J 可储存",
            "&8\u21E8 &e\u26A1 &7 5 J/s"
    );
    public static final SlimefunItemStack REINFORCED_HYDRO_GENERATOR = new SlimefunItemStack(
            "REINFORCED_HYDRO_GENERATOR",
            Material.END_STONE_BRICK_WALL,
            "&f强化 &9水力发电机",
            "&7使用流动的水发电",
            "",
            "&8\u21E8 &e\u26A1 &7 4,500 J 可储存",
            "&8\u21E8 &e\u26A1 &7 45 J/s"
    );
    public static final SlimefunItemStack GEOTHERMAL_GENERATOR = new SlimefunItemStack(
            "GEOTHERMAL_GENERATOR",
            Material.MAGMA_BLOCK,
            "&c热力发电机",
            "&7使用热力发电",
            "",
            "&8\u21E8 &e\u26A1 &7 3,500 J 可储存",
            "&8\u21E8 &e\u26A1 &7 35 J/s"
    );
    public static final SlimefunItemStack REINFORCED_GEOTHERMAL_GENERATOR = new SlimefunItemStack(
            "REINFORCED_GEOTHERMAL_GENERATOR",
            Material.SHROOMLIGHT,
            "&f强化 &c热力发电机",
            "&7使用热力发电",
            "",
            "&8\u21E8 &e\u26A1 &7 21,000 J 可储存",
            "&8\u21E8 &e\u26A1 &7 210 J/s"
    );
    public static final SlimefunItemStack BASIC_PANEL = new SlimefunItemStack(
            "BASIC_PANEL",
            Material.BLUE_GLAZED_TERRACOTTA,
            "&9基础太阳能发电机",
            "&7使用太阳能发电",
            "",
            "&8\u21E8 &e\u26A1 &7 900 J 可储存",
            "&8\u21E8 &e\u26A1 &7 9 J/s"
    );
    public static final SlimefunItemStack ADVANCED_PANEL = new SlimefunItemStack(
            "ADVANCED_PANEL",
            Material.RED_GLAZED_TERRACOTTA,
            "&c高级太阳能发电机",
            "&7使用太阳能发电",
            "",
            "&8\u21E8 &e\u26A1 &7 15,000 J 可储存",
            "&8\u21E8 &e\u26A1 &7 150 J/s"
    );
    public static final SlimefunItemStack CELESTIAL_PANEL = new SlimefunItemStack(
            "CELESTIAL_PANEL",
            Material.YELLOW_GLAZED_TERRACOTTA,
            "&e强化太阳能发电机",
            "&7使用太阳能发电",
            "",
            "&8\u21E8 &e\u26A1 &7 75,000 J 可储存",
            "&8\u21E8 &e\u26A1 &7 750 J/s"
    );
    public static final SlimefunItemStack VOID_PANEL = new SlimefunItemStack(
            "VOID_PANEL",
            Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
            "&8虚空发电机",
            "&7使用暗能量发电",
            "",
            "&8\u21E8 &e\u26A1 &7 300,000 J 可储存",
            "&8\u21E8 &e\u26A1 &7 3000 J/s"
    );
    public static final SlimefunItemStack INFINITE_PANEL = new SlimefunItemStack(
            "INFINITE_PANEL",
            Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
            "&b无尽发电机",
            "&7使用宇宙能量发电",
            "",
            "&8\u21E8 &e\u26A1 &7 6,000,000 J 可储存",
            "&8\u21E8 &e\u26A1 &7 60,000 J/s"
    );

    private final Type type;

    private EnergyGenerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int gen, Type type) {
        super(category, item, recipeType, recipe);
        this.type = type;
        type.generation = gen;
        type.storage = gen * 100;
    }

    public void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i++) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemStatus, ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(4, MenuPreset.loadingItemRed, ChestMenuUtils.getEmptyClickHandler());
    }


    @Override
    public int getGeneratedOutput(@Nonnull Location l, @Nonnull Config data) {
        @Nullable final BlockMenu inv = BlockStorage.getInventory(l);
        if (inv == null) return 0;

        Type type = getGeneration(l.getBlock(), Objects.requireNonNull(l.getWorld()));

        if (type == null) {
            if (inv.hasViewer()) {
                inv.replaceExistingItem(4, new CustomItem(
                        Material.GREEN_STAINED_GLASS_PANE,
                        "&c不在发电",
                        "&7已储存: &6" + getCharge(l) + " J"
                ));
            }
            return 0;
        } else {
            int gen = type.more ? (type.generation * 2) : type.generation;
            if (inv.hasViewer()) {
                inv.replaceExistingItem(4, new CustomItem(
                        Material.GREEN_STAINED_GLASS_PANE,
                        "&a正在发电",
                        "&7发电机类型: &6" + type.status,
                        "&7发电量: &6" + LorePreset.format(gen) + " J/s ",
                        "&7已储存: &6" + LorePreset.format(getCharge(l)) + " J"
                ));
            }
            return gen;
        }
    }

    private Type getGeneration(@Nonnull Block block, @Nonnull World world) {
        if (this.type == Type.WATER1) {

            // don't check water log every tick
            if (Util.isWaterLogged(block)) {
                return Type.WATER1;
            }
        } else if (this.type == Type.WATER2) {
        	if (Util.isWaterLogged(block)) {
                return Type.WATER2;
            }
        } else if (this.type == Type.INFINITY) {

            return Type.INFINITY;

        } else if (world.getEnvironment() == World.Environment.NETHER) {

            if (this.type == Type.GEOTHERMAL1 || this.type == Type.GEOTHERMAL2) {
                return Type.NETHER;
            }

            if (this.type == Type.LUNAR) {
                return Type.LUNAR;
            }

        } else if (world.getEnvironment() == World.Environment.THE_END) {

            if (this.type == Type.LUNAR) {
                return Type.LUNAR;
            }

        } else if (world.getEnvironment() == World.Environment.NORMAL) {

            if (this.type == Type.GEOTHERMAL1) {
                return Type.GEOTHERMAL1;
            }
            
            if (this.type == Type.GEOTHERMAL2) {
                return Type.GEOTHERMAL2;
            }

            if (world.getTime() >= 13000 || block.getLocation().add(0, 1, 0).getBlock().getLightFromSky() != 15) {

                if (this.type == Type.LUNAR) {
                    return Type.LUNAR;
                }

            } else if (this.type == Type.SOLAR1) {
                return Type.SOLAR1;
            } else if (this.type == Type.SOLAR2) {
                return Type.SOLAR2;
            } else if (this.type == Type.SOLAR3) {
                return Type.SOLAR3;
            }
        }

        return null;
    }

    @Override
    public int getCapacity() {
        return type.storage;
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @AllArgsConstructor
    public enum Type {
        WATER1("水力", false, HYDRO_ENERGY),
        WATER2("水力", false, ADVANCED_HYDRO_ENERGY),
        GEOTHERMAL1("热力", false, GEO_ENERGY),
        GEOTHERMAL2("热力", false, ADVANCED_GEO_ENERGY),
        SOLAR1("太阳能", false, BASIC_SOLAR_ENERGY),
        SOLAR2("太阳能", false, ADVANCED_SOLAR_ENERGY),
        SOLAR3("太阳能", false, CELESTIAL_ENERGY),
        LUNAR("虚空", false, VOID_ENERGY),
        INFINITY("无尽", false, INFINITY_ENERGY),
        
        NETHER("下界 (2x)", true, 0);
        
		Type(String status, boolean more, int gen) {
			this.status = status;
			this.more = more;
			this.generation = gen;
			this.storage = gen * 100;
		}
		private final String status;
        private final boolean more;
        private int generation;
        private int storage;
        
    }

	@Override
	protected int[] getTransportSlots(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
	    return new int[0];
	}

}
