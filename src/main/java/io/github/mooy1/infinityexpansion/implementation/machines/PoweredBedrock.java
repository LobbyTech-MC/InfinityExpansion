package io.github.mooy1.infinityexpansion.implementation.machines;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.implementation.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractTicker;
import io.github.mooy1.infinitylib.slimefun.presets.LorePreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A block that becomes bedrock when powered, for decoration of course
 *
 * @author Mooy1
 */
public final class PoweredBedrock extends AbstractTicker implements EnergyNetComponent {

    public static final int ENERGY = 10_000;
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "POWERED_BEDROCK",
            Material.NETHERITE_BLOCK,
            "&4充能基岩",
            "&7当充能时, 变成基岩",
            "&7一旦不充能就变回去",
            "",
            LorePreset.energyPerSecond(PoweredBedrock.ENERGY)
    );
    
    public PoweredBedrock() {
        super (Categories.INFINITY_CHEAT, ITEM, InfinityWorkbench.TYPE, new ItemStack[] {
                Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5,
                Items.COBBLE_5, Items.MACHINE_PLATE, Items.VOID_INGOT, Items.VOID_INGOT, Items.MACHINE_PLATE, Items.COBBLE_5,
                Items.COBBLE_5, Items.VOID_INGOT, SlimefunItems.ENERGIZED_CAPACITOR, SlimefunItems.ENERGIZED_CAPACITOR, Items.VOID_INGOT, Items.COBBLE_5,
                Items.COBBLE_5, Items.VOID_INGOT, Items.INFINITE_CORE, Items.INFINITE_CIRCUIT, Items.VOID_INGOT, Items.COBBLE_5,
                Items.COBBLE_5, Items.MACHINE_PLATE, Items.VOID_INGOT, Items.VOID_INGOT, Items.MACHINE_PLATE, Items.COBBLE_5,
                Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5, Items.COBBLE_5
        });
    }
    
    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return ENERGY * 2;
    }

    @Override
    protected void tick(@Nonnull Block block, @Nonnull Config config) {
        if ((InfinityExpansion.inst().getGlobalTick() & 3) == 0  || block.getType() == Material.AIR) {
            return;
        }
        Location l = block.getLocation();
        if (getCharge(l) < ENERGY) {
            if (block.getType() != Material.NETHERITE_BLOCK) {
                block.setType(Material.NETHERITE_BLOCK);
            }
        } else {
            if (block.getType() != Material.BEDROCK) {
                block.setType(Material.BEDROCK);
            }
        }
        removeCharge(l, ENERGY);
    }

    @Override
    protected boolean synchronised() {
        return true;
    }

}
