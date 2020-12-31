package io.github.mooy1.infinityexpansion.implementation.gear;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.lists.Categories;
import io.github.mooy1.infinityexpansion.lists.Items;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.general.LocationUtils;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.math.RandomUtils;
import io.github.mooy1.infinitylib.player.LeaveListener;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.SoulboundRune;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A VeinMiner rune, most code from {@link SoulboundRune}
 * 
 * @author Mooy1
 * 
 */
public class VeinMinerRune extends SlimefunItem implements Listener, NotPlaceable {

    private static final double RANGE = 1.5;
    private static final int MAX = 64;
    private static final long CD = 1000;
    private static NamespacedKey key = null;
    private static final Map<UUID, Long> CDS = new HashMap<>();
    private static final String[] LORE = {"", ChatColor.AQUA + "Veinminer - Crouch to use"};
    private static final Set<String> ALLOWED = new HashSet<>(Arrays.asList(
            "_ORE", "_LOG", "_WOOD", "GILDED", "SOUL", "GRAVEL",
            "MAGMA", "OBSIDIAN", "DIORITE", "ANDESITE", "GRANITE", "_LEAVES",
            "GLASS", "DIRT", "GRASS", "DEBRIS", "GLOWSTONE"
    ));
    private static final Set<Block> PROCESSING = new HashSet<>();
    
    public VeinMinerRune(InfinityExpansion plugin) {
        super(Categories.MAIN_MATERIALS, Items.VEIN_MINER_RUNE, RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
                Items.MAGSTEEL, SlimefunItems.PICKAXE_OF_VEIN_MINING, Items.MAGSTEEL,
                new ItemStack(Material.REDSTONE_ORE), SlimefunItems.BLANK_RUNE, new ItemStack(Material.LAPIS_ORE),
                Items.MAGSTEEL, SlimefunItems.MAGIC_LUMP_3, Items.MAGSTEEL,
        });
        new LeaveListener(CDS);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        key = new NamespacedKey(plugin, "vein_miner");
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isItem(e.getItemDrop().getItemStack()) && e.getItemDrop().getItemStack().getAmount() == 1) {
            PluginUtils.runSync(() -> activate(e.getPlayer(), e.getItemDrop()), 20L);
        }
    }

    private void activate(Player p, Item rune) {
        // Being sure the entity is still valid and not picked up or whatsoever.
        if (!rune.isValid()) {
            return;
        }

        Location l = rune.getLocation();
        Collection<Entity> entities = Objects.requireNonNull(l.getWorld()).getNearbyEntities(l, RANGE, RANGE, RANGE, this::findCompatibleItem);
        Optional<Entity> optional = entities.stream().findFirst();

        if (optional.isPresent()) {
            
            Item item = (Item) optional.get();
            ItemStack itemStack = item.getItemStack();

            if (itemStack.getAmount() == 1) {
                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                PluginUtils.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (rune.isValid() && item.isValid() && rune.getItemStack().getAmount() == 1) {

                        l.getWorld().createExplosion(l, 0);
                        l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1);

                        item.remove();
                        rune.remove();

                        setVeinMiner(itemStack, true);
                        l.getWorld().dropItemNaturally(l, itemStack);

                        MessageUtils.message(p, ChatColor.GREEN + "向工具添加了矿工!");
                    } else {
                        MessageUtils.message(p, ChatColor.RED + "添加矿工失败!");
                    }
                }, 10L);
                
            } else {
                MessageUtils.message(p, ChatColor.RED + "添加矿工失败!");
            }
        }
    }

    private boolean findCompatibleItem(Entity entity) {
        if (entity instanceof Item) {
            Item item = (Item) entity;
            ItemStack stack = item.getItemStack();

            return stack.getAmount() == 1 && stack.getItemMeta() instanceof Damageable && !isVeinMiner(stack) && !isItem(stack) ;
        }

        return false;
    }

    public static boolean isVeinMiner(@Nonnull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();

            return container.has(key, PersistentDataType.BYTE);
        }

        return false;
    }
    
    public static void setVeinMiner(@Nullable ItemStack item, boolean makeVeinMiner) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        
        if (meta == null) return;

        boolean isVeinMiner = isVeinMiner(item);

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (makeVeinMiner && !isVeinMiner) {
            container.set(key, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
            LoreUtils.addLore(item, LORE);
        }

        if (!makeVeinMiner && isVeinMiner) {
            container.remove(key);
            item.setItemMeta(meta);
            LoreUtils.removeLore(item, -1, LORE[1], 2);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        
        if (PROCESSING.contains(b)) return;

        Player p = e.getPlayer();
        
        if (!p.isSneaking()) return;
        
        ItemStack item = p.getInventory().getItemInMainHand();
        
        if (!isVeinMiner(item)) {
            return;
        }
            
        if (p.getFoodLevel() == 0) {
            MessageUtils.messageWithCD(p, 500, ChatColor.GOLD + "你太累了，无法挖矿!");
            return;
        }
        
        String type = b.getType().toString();
        
        if (!isAllowed(type)) return;
        
        Location l = b.getLocation();

        if (BlockStorage.hasBlockInfo(l)) return;

        Long prev = CDS.get(p.getUniqueId());
        if (prev != null && System.currentTimeMillis() - prev < CD) {
            MessageUtils.messageWithCD(p, 500, ChatColor.GOLD + "等待 " + ChatColor.YELLOW + (CD - (System.currentTimeMillis() - prev)) + ChatColor.GOLD + " ms before using again!");
            return;
        }
        CDS.put(p.getUniqueId(), System.currentTimeMillis());
        
        Set<Block> found = new HashSet<>();
        Set<Location> checked = new HashSet<>();
        checked.add(l);
        getVein(checked, found, l, b);

        World w = b.getWorld();
        
        for (Block mine : found) {
            PROCESSING.add(mine);
            BlockBreakEvent event = new BlockBreakEvent(mine, p);
            Bukkit.getServer().getPluginManager().callEvent(event);
            PROCESSING.remove(mine);
            if (!event.isCancelled()) {
                for (ItemStack drop : mine.getDrops(item)) {
                    w.dropItemNaturally(l, drop);
                }
                mine.setType(Material.AIR);
            }
        }
        
        if (type.endsWith("ORE")) {
            w.spawn(b.getLocation(), ExperienceOrb.class).setExperience(found.size() * 2);
        }
        
        if (RandomUtils.chanceIn(2)) {
            FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 1);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                p.setFoodLevel(event.getFoodLevel());
            }
        }
    }
    
    private boolean isAllowed(String mat) {
        for (String test : ALLOWED) {
            if (test.startsWith("_")) {
                if (mat.endsWith(test)) return true;
            } else {
                if (mat.contains(test)) return true;
            }
        }
        return false;
    }
    
    private void getVein(Set<Location> checked, Set<Block> found, Location l, Block b) {
        if (found.size() >= MAX) return;
        
        found.add(b);
        
        for (Location check : LocationUtils.getAdjacentLocations(l, true)) {
            if (checked.contains(check) || BlockStorage.hasBlockInfo(check)) continue;

            checked.add(check);

            if (check.getBlock().getType() == b.getType()) {
                getVein(checked, found, check, check.getBlock());
            }
        }
    }
    
}
