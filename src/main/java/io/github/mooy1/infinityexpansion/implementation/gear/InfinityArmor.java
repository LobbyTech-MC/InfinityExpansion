package io.github.mooy1.infinityexpansion.implementation.gear;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinityexpansion.implementation.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.setup.categories.Categories;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * armor
 *
 * @author Mooy1
 */
public final class InfinityArmor extends SlimefunArmorPiece implements ProtectiveArmor, Soulbound, NotPlaceable { //add sf bow too
    
    private static final NamespacedKey key = PluginUtils.getKey("infinity_armor");
    
    public static void setup(InfinityExpansion plugin) {
        new InfinityArmor(CROWN, new PotionEffect[] {
                new PotionEffect(PotionEffectType.SATURATION, 1200, 0),
                new PotionEffect(PotionEffectType.NIGHT_VISION, 1200, 0),
                new PotionEffect(PotionEffectType.WATER_BREATHING, 1200, 0)
        }, new ItemStack[] {
                null, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, null,
                Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY,
                Items.INFINITY, Items.VOID_INGOT, Items.INFINITY, Items.INFINITY, Items.VOID_INGOT, Items.INFINITY,
                null, Items.INFINITY, null, null, Items.INFINITY, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null
        }).register(plugin);
        new InfinityArmor(CHESTPLATE, new PotionEffect[] {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 1),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 1),
                new PotionEffect(PotionEffectType.ABSORPTION, 1200, 2),
        }, new ItemStack[] {
                null, Items.INFINITY, null, null, Items.INFINITY, null,
                Items.INFINITY, Items.VOID_INGOT, Items.INFINITY, Items.INFINITY, Items.VOID_INGOT, Items.INFINITY,
                Items.VOID_INGOT, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.VOID_INGOT,
                Items.VOID_INGOT, Items.INFINITY, Items.VOID_INGOT, Items.VOID_INGOT, Items.INFINITY, Items.VOID_INGOT,
                null, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, null,
                null, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, null
        }).register(plugin);
        new InfinityArmor(LEGGINGS, new PotionEffect[] {
                new PotionEffect(PotionEffectType.CONDUIT_POWER, 1200, 0),
                new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 2),
                new PotionEffect(PotionEffectType.REGENERATION, 1200, 0),
        }, new ItemStack[] {
                null, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, null,
                Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY, Items.INFINITY,
                Items.VOID_INGOT, Items.INFINITY, null, null, Items.INFINITY, Items.VOID_INGOT,
                Items.VOID_INGOT, Items.INFINITY, null, null, Items.INFINITY, Items.VOID_INGOT,
                Items.VOID_INGOT, Items.INFINITY, null, null, Items.INFINITY, Items.VOID_INGOT,
                null, Items.INFINITY, null, null, Items.INFINITY, null
        }).register(plugin);
        new InfinityArmor(BOOTS, new PotionEffect[] {
                new PotionEffect(PotionEffectType.SPEED, 1200, 2),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1200, 0),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0)
        }, new ItemStack[] {
                null, null, null, null, null, null,
                Items.INFINITY, Items.INFINITY, null, null, Items.INFINITY, Items.INFINITY,
                Items.INFINITY, Items.INFINITY, null, null, Items.INFINITY, Items.INFINITY,
                Items.VOID_INGOT, Items.VOID_INGOT, null, null, Items.VOID_INGOT, Items.VOID_INGOT,
                Items.INFINITY, Items.INFINITY, null, null, Items.INFINITY, Items.INFINITY,
                Items.INFINITY, Items.INFINITY, null, null, Items.INFINITY, Items.INFINITY
        }).register(plugin);
    }
    
    public static final SlimefunItemStack CROWN = new SlimefunItemStack(
            "INFINITY_CROWN",
            Material.NETHERITE_HELMET,
            "&b无尽头盔",
            "&7饱和 I",
            "&7夜视 I",
            "&7水下呼吸 I",
            "&7鞘翅撞地免疫"
    );
    public static final SlimefunItemStack CHESTPLATE = new SlimefunItemStack(
            "INFINITY_CHESTPLATE",
            Material.NETHERITE_CHESTPLATE,
            "&b无尽胸甲",
            "&7力量 II",
            "&7生命提升 III",
            "&7抗性 II",
            "&7蜜蜂保护"
    );
    public static final SlimefunItemStack LEGGINGS = new SlimefunItemStack(
            "INFINITY_LEGGINGS",
            Material.NETHERITE_LEGGINGS,
            "&b无尽护腿",
            "&7急迫 III",
            "&7潮涌能量 I",
            "&7生命恢复 I",
            "&7免疫辐射"
    );
    public static final SlimefunItemStack BOOTS = new SlimefunItemStack(
            "INFINITY_BOOTS",
            Material.NETHERITE_BOOTS,
            "&b无尽靴子",
            "&7速度 III",
            "&7海豚恩惠 I",
            "&7火焰抗性 I"
    );
    
    private InfinityArmor(SlimefunItemStack item, PotionEffect[] effects, ItemStack[] recipe) {
        super(Categories.INFINITY_CHEAT , item, InfinityWorkbench.TYPE, recipe, effects);
    }

    @Nonnull
    @Override
    public ProtectionType[] getProtectionTypes() {
        return new ProtectionType[] {
                ProtectionType.BEES, ProtectionType.RADIATION, ProtectionType.FLYING_INTO_WALL
        };
    }

    @Override
    public boolean isFullSetRequired() {
        return false;
    }

    @Nonnull
    @Override
    public NamespacedKey getArmorSetId() {
        return key;
    }

}