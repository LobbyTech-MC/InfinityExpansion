package io.github.mooy1.infinityexpansion.lists;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.implementation.blocks.AdvancedAnvil;
import io.github.mooy1.infinityexpansion.implementation.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.implementation.blocks.Strainer;
import io.github.mooy1.infinityexpansion.implementation.machines.ConversionMachine;
import io.github.mooy1.infinityexpansion.implementation.generators.EnergyGenerator;
import io.github.mooy1.infinityexpansion.implementation.machines.GearTransformer;
import io.github.mooy1.infinityexpansion.implementation.generators.InfinityReactor;
import io.github.mooy1.infinityexpansion.implementation.machines.ItemUpdater;
import io.github.mooy1.infinityexpansion.implementation.machines.MaterialGenerator;
import io.github.mooy1.infinityexpansion.implementation.machines.PoweredBedrock;
import io.github.mooy1.infinityexpansion.implementation.machines.Quarry;
import io.github.mooy1.infinityexpansion.implementation.machines.ResourceSynthesizer;
import io.github.mooy1.infinityexpansion.implementation.machines.SingularityConstructor;
import io.github.mooy1.infinityexpansion.implementation.machines.StoneworksFactory;
import io.github.mooy1.infinityexpansion.implementation.machines.TreeGrower;
import io.github.mooy1.infinityexpansion.implementation.machines.VirtualFarm;
import io.github.mooy1.infinityexpansion.implementation.machines.VoidHarvester;
import io.github.mooy1.infinityexpansion.implementation.mobdata.MobDataInfuser;
import io.github.mooy1.infinityexpansion.implementation.mobdata.MobSimulationChamber;
import io.github.mooy1.infinityexpansion.implementation.blocks.StorageUnit;
import io.github.mooy1.infinityexpansion.setup.SlimefunConstructors;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;

/**
 * List of items
 *
 * @author Mooy1
 */
public final class Items {
    
    //storage & transport
    
    public static final SlimefunItemStack STORAGE_FORGE = new SlimefunItemStack(
            "STORAGE_FORGE",
            Material.BEEHIVE,
            "&6仓储锻造",
            "&7升级存储单元层",
            "&7保留存储的项目"
    );

    //Machines
    
    public static final SlimefunItemStack STONEWORKS_FACTORY = new SlimefunItemStack(
            "STONEWORKS_FACTORY",
            Material.BLAST_FURNACE,
            "&8石材制造器",
            "&7生成鹅卵石并将其加工成各种材料",
            "",
            LorePreset.energyPerSecond(StoneworksFactory.ENERGY)
    );

    public static final SlimefunItemStack ITEM_UPDATER = new SlimefunItemStack(
            "ITEM_UPDATER",
            Material.QUARTZ_PILLAR,
            "&6物品更新器",
            "&7将要&6CRESET&7并更新其名称和知识",
            "&7slimefun物品（如果它们已过时或损坏）",
            "&收费和可升级物品将被重置!",
            "",
            LorePreset.energy(ItemUpdater.ENERGY) + "per item"
    );
    public static final SlimefunItemStack POWERED_BEDROCK = new SlimefunItemStack(
            "POWERED_BEDROCK",
            Material.NETHERITE_BLOCK,
            "&4动力基岩",
            "&7通电后，变成基岩",
            "&7断电或损坏后将还原",
            "",
            LorePreset.energyPerSecond(PoweredBedrock.ENERGY)
    );

    public static final SlimefunItemStack VOID_HARVESTER = new SlimefunItemStack(
            "VOID_HARVESTER",
            Material.OBSIDIAN,
            "&8虚空收割机",
            "&7一无所有慢慢收获&8虚空&7碎片..",
            "",
            LorePreset.speed(VoidHarvester.BASIC_SPEED),
            LorePreset.energyPerSecond(VoidHarvester.BASIC_ENERGY)
    );
    public static final SlimefunItemStack INFINITE_VOID_HARVESTER = new SlimefunItemStack(
            "INFINITE_VOID_HARVESTER",
            Material.CRYING_OBSIDIAN,
            "&b无尽&8虚空收割机",
            "&7收集 &8虚空&7碎片...",
            "",
            LorePreset.speed(VoidHarvester.INFINITY_SPEED),
            LorePreset.energyPerSecond(VoidHarvester.INFINITY_ENERGY)
    );

    public static final SlimefunItemStack SINGULARITY_CONSTRUCTOR = new SlimefunItemStack(
            "SINGULARITY_CONSTRUCTOR",
            Material.QUARTZ_BRICKS,
            "&f奇异构造机",
            "&7凝聚大量资源",
            "",
            LorePreset.speed(SingularityConstructor.BASIC_SPEED),
            LorePreset.energyPerSecond(SingularityConstructor.BASIC_ENERGY)
    );
    public static final SlimefunItemStack INFINITY_CONSTRUCTOR = new SlimefunItemStack(
            "INFINITY_CONSTRUCTOR",
            Material.CHISELED_QUARTZ_BLOCK,
            "&b无尽&f构造机",
            "&7快速的聚集大量能源",
            "",
            LorePreset.speed(SingularityConstructor.INFINITY_SPEED),
            LorePreset.energyPerSecond(SingularityConstructor.INFINITY_ENERGY)
    );

    public static final SlimefunItemStack RESOURCE_SYNTHESIZER = new SlimefunItemStack(
            "RESOURCE_SYNTHESIZER",
            Material.LODESTONE,
            "&6资源合成器",
            "&7非同寻常地创造资源",
            "",
            LorePreset.energy(ResourceSynthesizer.ENERGY) + "per use"
    );

    public static final SlimefunItemStack INFINITY_REACTOR = new SlimefunItemStack(
            "INFINITY_REACTOR",
            Material.BEACON,
            "&b有限反应堆",
            "&7通过压缩产生能量",
            "&7需要 &8虚空 &7和 &b无限 &7铸锭; 钢锭; 多晶硅块",
            "",
            LorePreset.energyBuffer(InfinityReactor.STORAGE),
            LorePreset.energyPerSecond(InfinityReactor.ENERGY)
    );

    public static final SlimefunItemStack ADVANCED_CHARGER = new SlimefunItemStack(
            "ADVANCED_CHARGER",
            Material.HONEYCOMB_BLOCK,
            "&c高级充电器",
            "&7迅速 收费 物品",
            "",
            LorePreset.speed(SlimefunConstructors.ADVANCED_CHARGER_SPEED),
            LorePreset.energyPerSecond(SlimefunConstructors.ADVANCED_CHARGER_ENERGY)
    );
    public static final SlimefunItemStack INFINITY_CHARGER = new SlimefunItemStack(
            "INFINITY_CHARGER",
            Material.SEA_LANTERN,
            "&b无限充电器",
            "&7即时收费项目",
            "",
            LorePreset.speed(SlimefunConstructors.INFINITY_CHARGER_SPEED),
            LorePreset.energy(SlimefunConstructors.INFINITY_CHARGER_ENERGY) + "per use"
    );
    public static final SlimefunItemStack ADVANCED_NETHER_STAR_REACTOR = new SlimefunItemStack(
            "ADVANCED_NETHER_STAR_REACTOR",
            HeadTexture.NETHER_STAR_REACTOR,
            "&c高级下届反应堆",
            "&f需要下界之星",
            "&b必须被水包围",
            "&b必须配备低温冷却液电池",
            "&4导致附近的实体枯萎",
            "",
            LorePreset.energyBuffer(SlimefunConstructors.STAR_BUFFER),
            LorePreset.energyPerSecond(SlimefunConstructors.STAR_ENERGY)
    );
    
    public static final SlimefunItemStack EXTREME_FREEZER = new SlimefunItemStack(
            "EXTREME_FREEZER",
            Material.LIGHT_BLUE_CONCRETE,
            "&b极端冷冻机",
            "&7将冰转化为冷却剂",
            "",
            LorePreset.speed(ConversionMachine.FREEZER_SPEED),
            LorePreset.energyPerSecond(ConversionMachine.FREEZER_ENERGY)
    );
    public static final SlimefunItemStack DUST_EXTRACTOR = new SlimefunItemStack(
            "DUST_EXTRACTOR",
            Material.FURNACE,
            "&8除尘器",
            "&7把鹅卵石变成灰尘",
            "",
            LorePreset.speed(ConversionMachine.DUST_SPEED),
            LorePreset.energyPerSecond(ConversionMachine.DUST_ENERGY)
    );

    public static final SlimefunItemStack URANIUM_EXTRACTOR = new SlimefunItemStack(
            "URANIUM_EXTRACTOR",
            Material.LIME_CONCRETE,
            "&a铀提取器",
            "&7把鹅卵石转化成铀",
            "",
            LorePreset.speed(ConversionMachine.URANIUM_SPEED),
            LorePreset.energyPerSecond(ConversionMachine.URANIUM_ENERGY)
    );


    public static final SlimefunItemStack BASIC_QUARRY = new SlimefunItemStack(
            "BASIC_QUARRY",
            Material.CHISELED_SANDSTONE,
            "&9基础采石场",
            "&7自动开采世界各地的矿石",
            "",
            LorePreset.speed(Quarry.BASIC_SPEED),
            LorePreset.energyPerSecond(Quarry.BASIC_ENERGY)
    );
    public static final SlimefunItemStack ADVANCED_QUARRY = new SlimefunItemStack(
            "ADVANCED_QUARRY",
            Material.CHISELED_RED_SANDSTONE,
            "&c高级采石场",
            "&7S能冶炼矿物，能开采地下矿石",
            "",
            LorePreset.speed(Quarry.ADVANCED_SPEED),
            LorePreset.energyPerSecond(Quarry.ADVANCED_ENERGY)
    );
    public static final SlimefunItemStack VOID_QUARRY = new SlimefunItemStack(
            "VOID_QUARRY",
            Material.CHISELED_NETHER_BRICKS,
            "&8空采石场",
            "&7可以偶尔开采筛分矿石或24克拉黄金",
            "",
            LorePreset.speed(Quarry.VOID_SPEED),
            LorePreset.energyPerSecond(Quarry.VOID_ENERGY)
    );
    public static final SlimefunItemStack INFINITY_QUARRY = new SlimefunItemStack(
            "INFINITY_QUARRY",
            Material.CHISELED_POLISHED_BLACKSTONE,
            "&b无限采石场",
            "&7可以开采和冶炼细粉锭",
            "",
            LorePreset.speed(Quarry.INFINITY_SPEED),
            LorePreset.energyPerSecond(Quarry.INFINITY_ENERGY)
    );
    public static final SlimefunItemStack ADVANCED_ENCHANTER = new SlimefunItemStack(
            "ADVANCED_ENCHANTER",
            Material.ENCHANTING_TABLE,
            "&c高级附魔师",
            "",
            LorePreset.speed(SlimefunConstructors.ADVANCED_EN_SPEED),
            LorePreset.energyPerSecond(SlimefunConstructors.ADVANCED_EN_ENERGY)
    );
    public static final SlimefunItemStack ADVANCED_DISENCHANTER = new SlimefunItemStack(
            "ADVANCED_DISENCHANTER",
            Material.ENCHANTING_TABLE,
            "&c高级祛魔者",
            "",
            LorePreset.speed(SlimefunConstructors.ADVANCED_DIS_SPEED),
            LorePreset.energyPerSecond(SlimefunConstructors.ADVANCED_DIS_ENERGY)
    );
    public static final SlimefunItemStack INFINITY_ENCHANTER = new SlimefunItemStack(
            "INFINITY_ENCHANTER",
            Material.ENCHANTING_TABLE,
            "&b无限附魔师",
            "",
            LorePreset.speed(SlimefunConstructors.INFINITY_EN_SPEED),
            LorePreset.energy(SlimefunConstructors.INFINITY_EN_ENERGY) + "per use"
    );
    public static final SlimefunItemStack INFINITY_DISENCHANTER = new SlimefunItemStack(
            "INFINITY_DISENCHANTER",
            Material.ENCHANTING_TABLE,
            "&b无限驱魔者",
            "",
            LorePreset.speed(SlimefunConstructors.INFINITY_DIS_SPEED),
            LorePreset.energy(SlimefunConstructors.INFINITY_DIS_ENERGY) + "per use"
    );
    public static final SlimefunItemStack INFINITY_WORKBENCH = new SlimefunItemStack(
            "INFINITY_FORGE",
            Material.RESPAWN_ANCHOR,
            "&6无限工作台",
            "&7用来制作无限的物品",
            "",
            LorePreset.energy(InfinityWorkbench.ENERGY) + "per item"
    );

    public static final SlimefunItemStack ADVANCED_ANVIL = new SlimefunItemStack(
            "ADVANCED_ANVIL",
            Material.SMITHING_TABLE,
            "&c高级砧",
            "&7组合工具和装备附魔，有时还会升级",
            "&b使用Slimefun项目",
            "",
            LorePreset.energy(AdvancedAnvil.ENERGY) + "per use"

    );
    public static final SlimefunItemStack DECOMPRESSOR = new SlimefunItemStack(
            "DECOMPRESSOR",
            Material.TARGET,
            "&7减压器",
            "&7将块减少到其基础材料",
            "",
            LorePreset.speed(ConversionMachine.DECOM_SPEED),
            LorePreset.energyPerSecond(ConversionMachine.DECOM_ENERGY)
    );

    public static final SlimefunItemStack INFINITY_CAPACITOR = new SlimefunItemStack(
            "INFINITY_CAPACITOR",
            HeadTexture.CAPACITOR_25,
            "&b无限电容",
            "&c&o无&a&o限&b&o电&e&o量",
            "&c&o1 懒得译了电容嘛懂得都懂",
            "",
            "&8\u21E8 &e\u26A1 &bInfinite &7J Capacity"
    );
    public static final SlimefunItemStack VOID_CAPACITOR = new SlimefunItemStack(
            "VOID_CAPACITOR",
            HeadTexture.CAPACITOR_25,
            "&8空电容器",
            "",
            "&8\u21E8 &e\u26A1 " + LorePreset.roundHundreds(SlimefunConstructors.VOID_CAPACITOR) + " &7J Capacity"
    );
    public static final SlimefunItemStack HYDRO_GENERATOR = new SlimefunItemStack(
            "HYDRO_GENERATOR",
            Material.PRISMARINE_WALL,
            "&9水轮发电机",
            "&7通过水的运动产生能量",
            "",
            LorePreset.energyBuffer(EnergyGenerator.WATER_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.WATER_RATE)
    );
    public static final SlimefunItemStack REINFORCED_HYDRO_GENERATOR = new SlimefunItemStack(
            "REINFORCED_HYDRO_GENERATOR",
            Material.END_STONE_BRICK_WALL,
            "&f强化&9水轮发电机",
            "&7产生大量的能量。",
            "&7从水的运动",
            "",
            LorePreset.energyBuffer(EnergyGenerator.WATER_STORAGE2),
            LorePreset.energyPerSecond(EnergyGenerator.WATER_RATE2)
    );
    public static final SlimefunItemStack GEOTHERMAL_GENERATOR = new SlimefunItemStack(
            "GEOTHERMAL_GENERATOR",
            Material.MAGMA_BLOCK,
            "&c地热发电机",
            "&7从世界的热量中产生能量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.GEO_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.GEO_RATE)
    );
    public static final SlimefunItemStack REINFORCED_GEOTHERMAL_GENERATOR = new SlimefunItemStack(
            "REINFORCED_GEOTHERMAL_GENERATOR",
            Material.SHROOMLIGHT,
            "&f强化&c地热发电机",
            "&7产生大量的能量。",
            "&7从世界的热量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.GEO_STORAGE2),
            LorePreset.energyPerSecond(EnergyGenerator.GEO_RATE2)
    );
    public static final SlimefunItemStack BASIC_PANEL = new SlimefunItemStack(
            "BASIC_PANEL",
            Material.BLUE_GLAZED_TERRACOTTA,
            "&9初级太阳能电池板",
            "&7从太阳产生能量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.BASIC_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.BASIC_RATE)
    );
    public static final SlimefunItemStack ADVANCED_PANEL = new SlimefunItemStack(
            "ADVANCED_PANEL",
            Material.RED_GLAZED_TERRACOTTA,
            "&c高级太阳能电池板",
            "&7从太阳产生能量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.ADVANCED_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.ADVANCED_RATE)
    );
    public static final SlimefunItemStack CELESTIAL_PANEL = new SlimefunItemStack(
            "CELESTIAL_PANEL",
            Material.YELLOW_GLAZED_TERRACOTTA,
            "&e太阳能电池板",
            "&7从太阳产生能量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.CELE_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.CELE_RATE)
    );
    public static final SlimefunItemStack VOID_PANEL = new SlimefunItemStack(
            "VOID_PANEL",
            Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
            "&8空隙板",
            "&7从黑暗中产生能量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.VOID_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.VOID_RATE)
    );
    public static final SlimefunItemStack INFINITE_PANEL = new SlimefunItemStack(
            "INFINITE_PANEL",
            Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
            "&b无限面板",
            "&7从宇宙中产生能量。",
            "",
            LorePreset.energyBuffer(EnergyGenerator.INFINITY_STORAGE),
            LorePreset.energyPerSecond(EnergyGenerator.INFINITY_RATE)
    );
    
    public static final SlimefunItemStack BASIC_TREE_GROWER = new SlimefunItemStack(
            "BASIC_TREE_GROWER",
            Material.STRIPPED_OAK_WOOD,
            "&9初级 &2种树机器人",
            "&7自动生长、收获和重新种植树木",
            "",
            LorePreset.speed(TreeGrower.SPEED1),
            LorePreset.energyPerSecond(TreeGrower.ENERGY1)
    );
    public static final SlimefunItemStack ADVANCED_TREE_GROWER = new SlimefunItemStack(
            "ADVANCED_TREE_GROWER",
            Material.STRIPPED_ACACIA_WOOD,
            "&c高级&2种树机器人",
            "&7自动生长、收获和重新种植树木",
            "",
            LorePreset.speed(TreeGrower.SPEED2),
            LorePreset.energyPerSecond(TreeGrower.ENERGY2)
    );
    public static final SlimefunItemStack INFINITY_TREE_GROWER = new SlimefunItemStack(
            "INFINITY_TREE_GROWER",
            Material.STRIPPED_WARPED_HYPHAE,
            "&b无尽&2种树机器人",
            "&7自动生长、收获和重新种植树木",
            "",
            LorePreset.speed(TreeGrower.SPEED3),
            LorePreset.energyPerSecond(TreeGrower.ENERGY3)
    );

    public static final SlimefunItemStack BASIC_VIRTUAL_FARM = new SlimefunItemStack(
            "BASIC_VIRTUAL_FARM",
            Material.GRASS_BLOCK,
            "&9初级&a虚拟农场",
            "&7自动生长、收获和再植作物自动生长、收获和再植作物",
            "",
            LorePreset.speed(VirtualFarm.SPEED1),
            LorePreset.energyPerSecond(VirtualFarm.ENERGY1)
    );
    public static final SlimefunItemStack ADVANCED_VIRTUAL_FARM = new SlimefunItemStack(
            "ADVANCED_VIRTUAL_FARM",
            Material.CRIMSON_NYLIUM,
            "&c高级&a虚拟农场",
            "&7自动生长、收获和重新种植作物",
            "",
            LorePreset.speed(VirtualFarm.SPEED2),
            LorePreset.energyPerSecond(VirtualFarm.ENERGY2)
    );
    public static final SlimefunItemStack INFINITY_VIRTUAL_FARM = new SlimefunItemStack(
            "INFINITY_VIRTUAL_FARM",
            Material.WARPED_NYLIUM,
            "&b无尽&a虚拟农场",
            "&7自动生长、收获和重新种植作物",
            "",
            LorePreset.speed(VirtualFarm.SPEED3),
            LorePreset.energyPerSecond(VirtualFarm.ENERGY3)
    );

    public static final SlimefunItemStack GEAR_TRANSFORMER = new SlimefunItemStack(
            "GEAR_TRANSFORMER",
            Material.EMERALD_BLOCK,
            "&7齿轮变压器",
            "&7Changes the material of tools and gear",
            "",
            LorePreset.energy(GearTransformer.ENERGY) + "Per Use"
    );

    //generators

    public static final SlimefunItemStack BASIC_COBBLE_GEN = new SlimefunItemStack(
            "BASIC_COBBLE_GEN",
            Material.LIGHT_GRAY_CONCRETE,
            "&9初级&8圆石发电机",
            "",
            LorePreset.speed(MaterialGenerator.COBBLE_SPEED),
            LorePreset.energyPerSecond(MaterialGenerator.COBBLE_ENERGY)
    );
    public static final SlimefunItemStack ADVANCED_COBBLE_GEN = new SlimefunItemStack(
            "ADVANCED_COBBLE_GEN",
            Material.GRAY_CONCRETE,
            "&c高级&8鹅卵石发电机",
            "",
            LorePreset.speed(MaterialGenerator.COBBLE2_SPEED),
            LorePreset.energyPerSecond(MaterialGenerator.COBBLE2_ENERGY)
    );
    public static final SlimefunItemStack BASIC_OBSIDIAN_GEN = new SlimefunItemStack(
            "BASIC_OBSIDIAN_GEN",
            Material.BLACK_CONCRETE,
            "&8黑曜石发电机",
            "",
            LorePreset.speed(MaterialGenerator.OBSIDIAN_SPEED),
            LorePreset.energyPerSecond(MaterialGenerator.OBSIDIAN_ENERGY)
    );

    //Deep storage units

    public static final SlimefunItemStack BASIC_STORAGE = new SlimefunItemStack(
            "BASIC_STORAGE",
            Material.OAK_WOOD,
            "&9初级&8存储单元",
            LorePreset.storesItem(StorageUnit.BASIC)
    );
    public static final SlimefunItemStack ADVANCED_STORAGE = new SlimefunItemStack(
            "ADVANCED_STORAGE",
            Material.DARK_OAK_WOOD,
            "&c高级&8存储单元",
            LorePreset.storesItem(StorageUnit.ADVANCED)
    );
    public static final SlimefunItemStack REINFORCED_STORAGE = new SlimefunItemStack(
            "REINFORCED_STORAGE",
            Material.ACACIA_WOOD,
            "&f强化&8存储单元",
            LorePreset.storesItem(StorageUnit.REINFORCED)
    );
    public static final SlimefunItemStack VOID_STORAGE = new SlimefunItemStack(
            "VOID_STORAGE",
            Material.CRIMSON_HYPHAE,
            "&8量子存储单元",
            LorePreset.storesItem(StorageUnit.VOID)
    );
    public static final SlimefunItemStack INFINITY_STORAGE = new SlimefunItemStack(
            "INFINITY_STORAGE",
            Material.WARPED_HYPHAE,
            "&b无限&8存储单元",
            "&6容量:&e无限"
            "&a神级储存单元！"
    );

    public static final SlimefunItemStack VEIN_MINER_RUNE = new SlimefunItemStack(
            "VEIN_MINER_RUNE",
            Material.DIAMOND,
            "&b矿脉符文",
            "&7升级工具以开采某些材料。"
    );
    
    public static final SlimefunItemStack ADVANCED_GEO_MINER = new SlimefunItemStack(
            "ADVANCED_GEO_MINER",
            HeadTexture.GEO_MINER,
            "&c高级&f地质采矿机",
            "&7速度更快的地质采矿机",
            "",
            LorePreset.speed(SlimefunConstructors.ADVANCED_GEO_SPEED),
            LorePreset.energyPerSecond(SlimefunConstructors.ADVANCED_GEO_ENERGY)
    );

    //strainers

    public static final SlimefunItemStack STRAINER_BASE = new SlimefunItemStack(
            "STRAINER_BASE",
            Material.SANDSTONE_WALL,
            "&7滤器底座"
    );
    public static final SlimefunItemStack BASIC_STRAINER = new SlimefunItemStack(
            "BASIC_STRAINER",
            Material.FISHING_ROD,
            "&9碱性滤器",
            "&7从流动的水中收集材料。",
            "",
            LoreBuilder.speed(Strainer.BASIC)
    );
    public static final SlimefunItemStack ADVANCED_STRAINER = new SlimefunItemStack(
            "ADVANCED_STRAINER",
            Material.FISHING_ROD,
            "&c高级过滤器",
            "&7从流动的水中收集材料。",
            "",
            LoreBuilder.speed(Strainer.ADVANCED)
    );
    public static final SlimefunItemStack REINFORCED_STRAINER = new SlimefunItemStack(
            "REINFORCED_STRAINER",
            Material.FISHING_ROD,
            "&b钢筋滤网",
            "&7从流动的水中收集材料。",
            "",
            LoreBuilder.speed(Strainer.REINFORCED)
    );

    //mob stuff

    public static final SlimefunItemStack DATA_INFUSER = new SlimefunItemStack(
            "DATA_INFUSER",
            Material.LODESTONE,
            "&8MOB数据注入程序",
            "&7注入空数据卡与暴徒项目。",
            "",
            LorePreset.energy(MobDataInfuser.ENERGY) + "per use"
    );
    public static final SlimefunItemStack MOB_SIMULATION_CHAMBER = new SlimefunItemStack(
            "MOB_SIMULATION_CHAMBER",
            Material.GILDED_BLACKSTONE,
            "&8MOB模拟室",
            "&7使用Mob数据卡激活",
            "",
            LorePreset.energyBuffer(MobSimulationChamber.BUFFER),
            LorePreset.energyPerSecond(MobSimulationChamber.ENERGY)
    );
    public static final SlimefunItemStack EMPTY_DATA_CARD = new SlimefunItemStack(
            "EMPTY_DATA_CARD",
            Material.CHAINMAIL_CHESTPLATE,
            "&8空数据卡",
            "&7注入暴民的物品来填充。"
    );

    
    //Info thing

    /**
     * Thanks to NCBPFluffyBear for the idea
     */
    public static final SlimefunItemStack ADDON_INFO = new SlimefunItemStack(
            InfinityExpansion.getInstance().getName().toUpperCase(Locale.ROOT) + "_ADDON_INFO",
            Material.NETHER_STAR,
            "&b附加信息",
            "&c汉化者:&amc"
            "&e翻译有什么问题请联系翻译者"
            "&f版本: &7" + InfinityExpansion.getInstance().getPluginVersion(),
            "",
            "&bDiscord: &b@&7Riley&8#5911",
            "&7discord.gg/slimefun",
            "",
            "&fGithub: &b@&8&7Mooy1",
            "&7" + InfinityExpansion.getInstance().getBugTrackerURL()
    );

    //Materials

    public static final SlimefunItemStack MAGSTEEL = new SlimefunItemStack(
            "MAGSTEEL",
            Material.BRICK,
            "&4磁钢"
    );
    public static final SlimefunItemStack MAGNONIUM = new SlimefunItemStack(
            "MAGNONIUM",
            Material.NETHER_BRICK,
            "&5强化奇点锭"
    );
    public static final SlimefunItemStack TITANIUM = new SlimefunItemStack(
            "TITANIUM",
            Material.IRON_INGOT,
            "&7钛金属"
    );
    public static final SlimefunItemStack MYTHRIL = new SlimefunItemStack(
            "MYTHRIL",
            Material.IRON_INGOT,
            "&b强化奇点锭"
    );
    public static final SlimefunItemStack ADAMANTITE = new SlimefunItemStack(
            "ADAMANTITE",
            Material.BRICK,
            "&d金刚石"
    );
    public static final SlimefunItemStack INFINITE_INGOT = new SlimefunItemStack(
            "INFINITE_INGOT",
            Material.IRON_INGOT,
            "&b无限大铸锭", // &dI&cn&6f&ei&an&bi&3t&9y &fIngot
            "&7&o宇宙的愤怒",
            "&7&o在你的手心里"
    );
    
    public static final SlimefunItemStack VOID_BIT = new SlimefunItemStack(
            "VOID_BIT",
            Material.IRON_NUGGET,
            "&8虚空粒子",
            "&7&感觉.很空虚。"
    );
    public static final SlimefunItemStack VOID_DUST = new SlimefunItemStack(
            "VOID_DUST",
            Material.GUNPOWDER,
            "&8空隙尘",
            "&7&o它开始成形了..."
    );
    public static final SlimefunItemStack VOID_INGOT = new SlimefunItemStack(
            "VOID_INGOT",
            Material.NETHERITE_INGOT,
            "&8空隙锭",
            "&7&o宇宙的空虚",
            "&7&o在你的手心里"
    );
    
    public static final SlimefunItemStack ENDER_ESSENCE = new SlimefunItemStack(
            "END_ESSENCE",
            Material.BLAZE_POWDER,
            "&5赤焰",
            "&8&o从尽头的深处."
    );

    public static final SlimefunItemStack MAGSTEEL_PLATE = new SlimefunItemStack(
            "MAGSTEEL_PLATE",
            Material.NETHERITE_SCRAP,
            "&4磁钢钢板",
            "&7机器部件"
    );
    public static final SlimefunItemStack MACHINE_PLATE = new SlimefunItemStack(
            "MACHINE_PLATE",
            Material.PAPER,
            "&f机板",
            "&7机器部件"
    );
    public static final SlimefunItemStack MACHINE_CIRCUIT = new SlimefunItemStack(
            "MACHINE_CIRCUIT",
            Material.GOLD_INGOT,
            "&6机器电路",
            "&7机器部件"
    );
    public static final SlimefunItemStack INFINITE_MACHINE_CIRCUIT = new SlimefunItemStack(
            "INFINITE_MACHINE_CIRCUIT",
            Material.DIAMOND,
            "&b无限&6机器电路",
            "&7机器部件"
    );
    public static final SlimefunItemStack MACHINE_CORE = new SlimefunItemStack(
            "MACHINE_CORE",
            Material.IRON_BLOCK,
            "&f机芯",
            "&7机器部件"
    );
    public static final SlimefunItemStack INFINITE_MACHINE_CORE = new SlimefunItemStack(
            "INFINITE_MACHINE_CORE",
            Material.DIAMOND_BLOCK,
            "&b无限机心",
            "&7机器部件"
    );

    //Compressed Cobblestones

    public static final SlimefunItemStack COMPRESSED_COBBLESTONE_1 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_1",
            Material.ANDESITE,
            "&7一层压缩圆石",
            "&89块圆石"
    );
    public static final SlimefunItemStack COMPRESSED_COBBLESTONE_2 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_2",
            Material.ANDESITE,
            "&7二层压缩圆石",
            "&881块圆石"
    );
    public static final SlimefunItemStack COMPRESSED_COBBLESTONE_3 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_3",
            Material.STONE,
            "&7三层压缩圆石",
            "&8243块圆石。"
    );
    public static final SlimefunItemStack COMPRESSED_COBBLESTONE_4 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_4",
            Material.STONE,
            "&7四层压缩圆石",
            "&86561块圆石"
    );
    public static final SlimefunItemStack COMPRESSED_COBBLESTONE_5 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_5",
            Material.POLISHED_ANDESITE,
            "&7五层压缩圆石",
            "&859049块圆石"
    );

    //singularities

    public static final SlimefunItemStack COPPER_SINGULARITY = new SlimefunItemStack(
            "COPPER_SINGULARITY",
            Material.BRICKS,
            "&6铜奇点"
    );

    public static final SlimefunItemStack ZINC_SINGULARITY = new SlimefunItemStack(
            "ZINC_SINGULARITY",
            Material.IRON_BLOCK,
            "&7锌奇点"
    );

    public static final SlimefunItemStack TIN_SINGULARITY = new SlimefunItemStack(
            "TIN_SINGULARITY",
            Material.IRON_BLOCK,
            "&7锡奇点"
    );

    public static final SlimefunItemStack ALUMINUM_SINGULARITY = new SlimefunItemStack(
            "ALUMINUM_SINGULARITY",
            Material.IRON_BLOCK,
            "&7铝奇点"
    );

    public static final SlimefunItemStack SILVER_SINGULARITY = new SlimefunItemStack(
            "SILVER_SINGULARITY",
            Material.IRON_BLOCK,
            "&7银奇点"
    );

    public static final SlimefunItemStack MAGNESIUM_SINGULARITY = new SlimefunItemStack(
            "MAGNESIUM_SINGULARITY",
            Material.NETHER_BRICKS,
            "&5镁奇点"
    );

    public static final SlimefunItemStack LEAD_SINGULARITY = new SlimefunItemStack(
            "LEAD_SINGULARITY",
            Material.IRON_BLOCK,
            "&8铅奇点"
    );


    public static final SlimefunItemStack GOLD_SINGULARITY = new SlimefunItemStack(
            "GOLD_SINGULARITY",
            Material.GOLD_BLOCK,
            "&6金块奇点"
    );

    public static final SlimefunItemStack IRON_SINGULARITY = new SlimefunItemStack(
            "IRON_SINGULARITY",
            Material.IRON_BLOCK,
            "&7铁块奇点"
    );

    public static final SlimefunItemStack DIAMOND_SINGULARITY = new SlimefunItemStack(
            "DIAMOND_SINGULARITY",
            Material.DIAMOND_BLOCK,
            "&b钻石块奇点"
    );

    public static final SlimefunItemStack EMERALD_SINGULARITY = new SlimefunItemStack(
            "EMERALD_SINGULARITY",
            Material.EMERALD_BLOCK,
            "&a绿宝石块奇点"
    );

    public static final SlimefunItemStack NETHERITE_SINGULARITY = new SlimefunItemStack(
            "NETHERITE_SINGULARITY",
            Material.NETHERITE_BLOCK,
            "&4下界合金块奇点"
    );

    public static final SlimefunItemStack COAL_SINGULARITY = new SlimefunItemStack(
            "COAL_SINGULARITY",
            Material.COAL_BLOCK,
            "&8煤碳块奇点"
    );

    public static final SlimefunItemStack REDSTONE_SINGULARITY = new SlimefunItemStack(
            "REDSTONE_SINGULARITY",
            Material.REDSTONE_BLOCK,
            "&c红石块奇点"
    );

    public static final SlimefunItemStack LAPIS_SINGULARITY = new SlimefunItemStack(
            "LAPIS_SINGULARITY",
            Material.LAPIS_BLOCK,
            "&9青金石块奇点"
    );

    public static final SlimefunItemStack QUARTZ_SINGULARITY = new SlimefunItemStack(
            "QUARTZ_SINGULARITY",
            Material.QUARTZ_BLOCK,
            "&f石英块奇点"
    );

    public static final SlimefunItemStack INFINITY_SINGULARITY = new SlimefunItemStack(
            "INFINITY_SINGULARITY",
            Material.SMOOTH_QUARTZ,
            "&b无限奇点块"
    );

    public static final SlimefunItemStack FORTUNE_SINGULARITY = new SlimefunItemStack(
            "FORTUNE_SINGULARITY",
            Material.NETHER_STAR,
            "&6福琼奇点"
    );
    public static final SlimefunItemStack EARTH_SINGULARITY = new SlimefunItemStack(
            "EARTH_SINGULARITY",
            Material.NETHER_STAR,
            "&a地球奇点"
    );
    public static final SlimefunItemStack METAL_SINGULARITY = new SlimefunItemStack(
            "METAL_SINGULARITY",
            Material.NETHER_STAR,
            "&8金属奇点"
    );
    public static final SlimefunItemStack MAGIC_SINGULARITY = new SlimefunItemStack(
            "MAGIC_SINGULARITY",
            Material.NETHER_STAR,
            "&d幻奇点"
    );

    //Gear

    public static final SlimefunItemStack ENDER_FLAME = new SlimefunItemStack(
            "ENDER_FLAME",
            Material.ENCHANTED_BOOK,
            "&c附魔火焰"
    );

    public static final SlimefunItemStack INFINITY_CROWN = new SlimefunItemStack(
            "INFINITY_CROWN",
            Material.NETHERITE_HELMET,
            "&b无限冠",
            "&7饱和 I",
            "&7夜视 I",
            "&7水呼吸 I"
    );
    public static final SlimefunItemStack INFINITY_CHESTPLATE = new SlimefunItemStack(
            "INFINITY_CHESTPLATE",
            Material.NETHERITE_CHESTPLATE,
            "&b无限胸板",
            "&7力量 II",
            "&7健康促进 III",
            "&7抵抗 II"
    );
    public static final SlimefunItemStack INFINITY_LEGGINGS = new SlimefunItemStack(
            "INFINITY_LEGGINGS",
            Material.NETHERITE_LEGGINGS,
            "&b无限贴腿裤",
            "&7匆忙 III",
            "&7导管功率 I",
            "&7再生 I"
    );
    public static final SlimefunItemStack INFINITY_BOOTS = new SlimefunItemStack(
            "INFINITY_BOOTS",
            Material.NETHERITE_BOOTS,
            "&b无限靴子",
            "&7速度 III",
            "&7海豚恩典 I",
            "&7耐火性 I"
    );
    public static final SlimefunItemStack INFINITY_BLADE = new SlimefunItemStack(
            "INFINITY_BLADE",
            Material.NETHERITE_SWORD,
            "&b宇宙之刃",
            "&b&o无中生有"
    );
    public static final SlimefunItemStack INFINITY_PICKAXE = new SlimefunItemStack(
            "INFINITY_PICKAXE",
            Material.NETHERITE_PICKAXE,
            "&9世界破坏者",
            "&3&o世界末日"
    );
    public static final SlimefunItemStack INFINITY_AXE = new SlimefunItemStack(
            "INFINITY_AXE",
            Material.NETHERITE_AXE,
            "&4自然的毁灭",
            "&c&o愤怒的化身"
    );
    public static final SlimefunItemStack INFINITY_SHOVEL = new SlimefunItemStack(
            "INFINITY_SHOVEL",
            Material.NETHERITE_SHOVEL,
            "&a山食者",
            "&2&o恐怖"
    );
    public static final SlimefunItemStack INFINITY_BOW = new SlimefunItemStack(
            "INFINITY_BOW",
            Material.BOW,
            "&6天空穿孔机",
            "&e&o天空的长弓"
    );
    public static final SlimefunItemStack INFINITY_MATRIX = new SlimefunItemStack(
            "INFINITY_MATRIX",
            Material.NETHER_STAR,
            "&f无穷矩阵",
            "&6给予无限的飞行",
            "&7右键单击以启用/禁用和声明",
            "&7蹲下并右键单击以删除所有权。"
    );
    public static final SlimefunItemStack INFINITY_SHIELD = new SlimefunItemStack(
            "INFINITY_SHIELD",
            Material.SHIELD,
            "&b虚空庇护",
            "&7&obong"
    );
    
    public static void setup(@Nonnull FileConfiguration config) { //add item meta
        Validate.notNull(config, "Config cannot be null!");

        ItemMeta meta = BASIC_STRAINER.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Strainer.KEY, PersistentDataType.INTEGER, Strainer.BASIC);
            BASIC_STRAINER.setItemMeta(meta);
        }
        meta = ADVANCED_STRAINER.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Strainer.KEY, PersistentDataType.INTEGER, Strainer.ADVANCED);
            ADVANCED_STRAINER.setItemMeta(meta);
        }
        meta = REINFORCED_STRAINER.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Strainer.KEY, PersistentDataType.INTEGER, Strainer.REINFORCED);
            REINFORCED_STRAINER.setItemMeta(meta);
        }
        
        SlimefunItemStack[] items = new SlimefunItemStack[] {
                INFINITY_CROWN, INFINITY_CHESTPLATE, INFINITY_LEGGINGS, INFINITY_BOOTS, INFINITY_BLADE,
                INFINITY_AXE, INFINITY_PICKAXE, INFINITY_SHOVEL, INFINITY_SHIELD, INFINITY_BOW
        };

        addUnbreakable(items);
        addEnchants(items, "infinity-enchant-levels", "INFINITY", config);
        
        EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) ENDER_FLAME.getItemMeta();
        Objects.requireNonNull(storageMeta).addStoredEnchant(Enchantment.FIRE_ASPECT, 10, true);
        ENDER_FLAME.setItemMeta(storageMeta);
        
    }
    
    private static void addEnchants(SlimefunItemStack[] items, String section, String gearType, FileConfiguration config) {
        ConfigurationSection typeSection = config.getConfigurationSection(section);
        
        if (typeSection == null) {
            PluginUtils.log(Level.SEVERE, "Config section " + section + " missing, Check the config and report this!");
            return;
        }
        
        for (SlimefunItemStack item : items) {
            String itemPath = item.getItemId().replace(gearType + "_", "").toLowerCase();
            ConfigurationSection itemSection = typeSection.getConfigurationSection(itemPath);
            if (itemSection == null) {
                PluginUtils.log(Level.SEVERE, "Config section " + itemPath + " missing, Check the config and report this!");
                continue;
            }
            
            for (String path : itemSection.getKeys(false)) {
                int level = config.getInt(section + "." + itemPath + "." + path);
                if (level > 0 && level <= Short.MAX_VALUE) {
                    Enchantment e = getEnchantFromString(path);
                    if (e == null) {
                        PluginUtils.log(Level.WARNING, "Failed to add enchantment " + path + ", your config may be messed up, report this!");
                        continue;
                    }
                    item.addUnsafeEnchantment(e, level);
                } else if (level != 0) {
                    config.set(section + "." + itemPath + "." + path, 1);
                    PluginUtils.log(Level.WARNING, "Enchantment level " + level + " for enchantment " + path + " is not allowed, resetting to 1, please check your config and update it with a correct value"); 
                }
            }
        }
        
        InfinityExpansion.getInstance().saveConfig();
    }
        
    private static void addUnbreakable(SlimefunItemStack[] items) {
        for (SlimefunItemStack item : items) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
        }
    }
    
    @Nullable
    private static Enchantment getEnchantFromString(String string) {
        switch (string) {
            case "sharpness": return Enchantment.DAMAGE_ALL;
            case "efficiency": return Enchantment.DIG_SPEED;
            case "protection": return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "fire-aspect": return Enchantment.FIRE_ASPECT;
            case "fortune": return Enchantment.LOOT_BONUS_BLOCKS;
            case "looting": return Enchantment.LOOT_BONUS_MOBS;
            case "silk-touch": return Enchantment.SILK_TOUCH;
            case "thorns": return Enchantment.THORNS;
            case "aqua-affinity": return Enchantment.WATER_WORKER;
            case "power": return Enchantment.ARROW_DAMAGE;
            case "flame": return Enchantment.ARROW_FIRE;
            case "infinity": return Enchantment.ARROW_INFINITE;
            case "punch": return Enchantment.ARROW_KNOCKBACK;
            case "feather-falling": return Enchantment.PROTECTION_FALL;
            default: return null;
        }
    }
}
