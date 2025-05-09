package io.github.mooy1.infinityexpansion.items.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Storage {

    public static final SlimefunItemStack STORAGE_FORGE = new SlimefunItemStack(
            "STORAGE_FORGE",
            Material.BEEHIVE,
            "&6存储单元工作台",
            "&7制造或升级存储单元"
    );

    private static final int BASIC_AMOUNT = 6400;
    private static final int ADVANCED_AMOUNT = 25600;
    private static final int REINFORCED_AMOUNT = 102400;
    private static final int VOID_AMOUNT = 409600;
    private static final int INFINITY_AMOUNT = 1_600_000_000;

    public static final SlimefunItemStack BASIC_STORAGE = new SlimefunItemStack(
            "BASIC_STORAGE",
            Material.OAK_WOOD,
            "&9基础&8存储单元",
            "&6容量: &e" + MachineLore.format(BASIC_AMOUNT) + " &e个物品"
    );
    public static final SlimefunItemStack ADVANCED_STORAGE = new SlimefunItemStack(
            "ADVANCED_STORAGE",
            Material.DARK_OAK_WOOD,
            "&c高级&8存储单元",
            "&6容量: &e" + MachineLore.format(ADVANCED_AMOUNT) + " &e个物品"
    );
    public static final SlimefunItemStack REINFORCED_STORAGE = new SlimefunItemStack(
            "REINFORCED_STORAGE",
            Material.ACACIA_WOOD,
            "&f超级&8存储单元",
            "&6容量: &e" + MachineLore.format(REINFORCED_AMOUNT) + " &e个物品"
    );
    public static final SlimefunItemStack VOID_STORAGE = new SlimefunItemStack(
            "VOID_STORAGE",
            Material.CRIMSON_HYPHAE,
            "&8虚空&8存储单元",
            "&6容量: &e" + MachineLore.format(VOID_AMOUNT) + " &e个物品"
    );
    public static final SlimefunItemStack INFINITY_STORAGE = new SlimefunItemStack(
            "INFINITY_STORAGE",
            Material.WARPED_HYPHAE,
            "&b无尽&8存储单元",
            "&6容量: &e" + MachineLore.format(INFINITY_AMOUNT) + " &e个物品"
    );

    public static void setup(InfinityExpansion plugin) {
        new StorageForge(Groups.STORAGE, STORAGE_FORGE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL, new ItemStack(Material.ANVIL), Materials.MAGSTEEL,
                Materials.MAGSTEEL, new ItemStack(Material.CRAFTING_TABLE), Materials.MAGSTEEL,
                Materials.MAGSTEEL, new ItemStack(Material.BARREL), Materials.MAGSTEEL,
        }).register(plugin);
        new StorageUnit(BASIC_STORAGE, BASIC_AMOUNT, new ItemStack[] {
                new ItemStack(Material.OAK_LOG), Materials.MAGSTEEL, new ItemStack(Material.OAK_LOG),
                new ItemStack(Material.OAK_LOG), new ItemStack(Material.BARREL), new ItemStack(Material.OAK_LOG),
                new ItemStack(Material.OAK_LOG), Materials.MAGSTEEL, new ItemStack(Material.OAK_LOG)
        }).register(plugin);
        new StorageUnit(ADVANCED_STORAGE, ADVANCED_AMOUNT, new ItemStack[] {
                Materials.MAGSTEEL, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL,
                Materials.MAGSTEEL, BASIC_STORAGE, Materials.MAGSTEEL,
                Materials.MAGSTEEL, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL
        }).register(plugin);
        new StorageUnit(REINFORCED_STORAGE, REINFORCED_AMOUNT, new ItemStack[] {
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL_PLATE,
                Materials.MAGSTEEL_PLATE, ADVANCED_STORAGE, Materials.MAGSTEEL_PLATE,
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_PLATE, Materials.MAGSTEEL_PLATE
        }).register(plugin);
        new StorageUnit(VOID_STORAGE, VOID_AMOUNT, new ItemStack[] {
                Materials.VOID_INGOT, Materials.MACHINE_PLATE, Materials.VOID_INGOT,
                Materials.MAGNONIUM, REINFORCED_STORAGE, Materials.MAGNONIUM,
                Materials.VOID_INGOT, Materials.MACHINE_CORE, Materials.VOID_INGOT
        }).register(plugin);
        new StorageUnit(INFINITY_STORAGE, INFINITY_AMOUNT, new ItemStack[] {
                Materials.INFINITE_INGOT, Materials.VOID_INGOT, Materials.INFINITE_INGOT,
                Materials.INFINITE_INGOT, VOID_STORAGE, Materials.INFINITE_INGOT,
                Materials.INFINITE_INGOT, Materials.VOID_INGOT, Materials.INFINITE_INGOT
        }).register(plugin);
    }

}
