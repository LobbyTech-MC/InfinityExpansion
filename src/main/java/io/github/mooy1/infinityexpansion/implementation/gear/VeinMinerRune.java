package io.github.mooy1.infinityexpansion.implementation.gear;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.implementation.materials.Items;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.players.CoolDownMap;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.runes.SoulboundRune;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A VeinMiner rune, most code from {@link SoulboundRune}
 * 
 * @author Mooy1
 * 
 */
public final class VeinMinerRune extends SlimefunItem implements Listener, NotPlaceable {
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "VEIN_MINER_RUNE",
            Material.DIAMOND,
            "&b超级矿脉稿",
            "&7升级版的对指定物品有效的矿脉稿"
    );
    private static final Map<UUID, Long> CDS = new HashMap<>();
    private static final Set<String> ALLOWED = new HashSet<>(Arrays.asList(
            "_ORE", "_LOG", "_WOOD", "GILDED", "SOUL", "GRAVEL",
            "MAGMA", "OBSIDIAN", "DIORITE", "ANDESITE", "GRANITE", "_LEAVES",
            "GLASS", "DIRT", "GRASS", "DEBRIS", "GLOWSTONE"
    ));
    private static final double RANGE = 1.5;
    private static final int MAX = 64;
    private static final String LORE = ChatColor.AQUA + "超级矿脉挖矿";
    private static final NamespacedKey key = InfinityExpansion.inst().getKey("vein_miner");
    
    private final CoolDownMap cooldowns = new CoolDownMap(InfinityExpansion.inst());
    private final Set<Block> PROCESSING = new HashSet<>();
    
    public VeinMinerRune() {
        super(Categories.MAIN_MATERIALS, ITEM, RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
                Items.MAGSTEEL, SlimefunItems.PICKAXE_OF_VEIN_MINING, Items.MAGSTEEL,
                new ItemStack(Material.REDSTONE_ORE), SlimefunItems.BLANK_RUNE, new ItemStack(Material.LAPIS_ORE),
                Items.MAGSTEEL, SlimefunItems.MAGIC_LUMP_3, Items.MAGSTEEL,
        });
        InfinityExpansion.inst().registerListener(this);
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isItem(e.getItemDrop().getItemStack()) && e.getItemDrop().getItemStack().getAmount() == 1) {
            InfinityExpansion.inst().runSync(() -> activate(e.getPlayer(), e.getItemDrop()), 20L);
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

                InfinityExpansion.inst().runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (rune.isValid() && item.isValid() && rune.getItemStack().getAmount() == 1) {

                        l.getWorld().createExplosion(l, 0);
                        l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1);

                        item.remove();
                        rune.remove();

                        setVeinMiner(itemStack, true);
                        l.getWorld().dropItemNaturally(l, itemStack);

                        p.sendMessage(ChatColor.GREEN + "成功应用矿脉!");
                    } else {
                        p.sendMessage(ChatColor.RED + "矿脉模式加载失败!");
                    }
                }, 10L);
                
            } else {
                p.sendMessage(ChatColor.RED + "矿脉模式加载失败!");
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

    public static boolean isVeinMiner(@Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
    
    public static void setVeinMiner(@Nullable ItemStack item, boolean makeVeinMiner) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();

        boolean isVeinMiner = isVeinMiner(item);

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (makeVeinMiner && !isVeinMiner) {
            container.set(key, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
            LoreUtils.addLore(item, LORE);
        }

        if (!makeVeinMiner && isVeinMiner) {
            container.remove(key);
            List<String> lore = meta.getLore();
            if (lore != null) {
                lore.remove(LORE);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
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
            p.sendMessage(ChatColor.GOLD + "你太饿了，无法使用矿脉!");
            return;
        }
        
        String type = b.getType().toString();
        
        if (!isAllowed(type)) return;
        
        Location l = b.getLocation();

        if (BlockStorage.hasBlockInfo(l)) return;

        boolean cd = this.cooldowns.check(p.getUniqueId(), 1000);
        
        if (!cd) {
            p.sendMessage(ChatColor.GOLD + "请等待一秒再次使用");
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
        
        if (ThreadLocalRandom.current().nextBoolean()) {
            FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 1);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                p.setFoodLevel(event.getFoodLevel());
            }
        }
    }
    
    private static boolean isAllowed(String mat) {
        for (String test : ALLOWED) {
            if (test.startsWith("_")) {
                if (mat.endsWith(test)) return true;
            } else {
                if (mat.contains(test)) return true;
            }
        }
        return false;
    }
    
    private static void getVein(Set<Location> checked, Set<Block> found, Location l, Block b) {
        if (found.size() >= MAX) return;
        
        for (Location check : getAdjacentLocations(l)) {
            if (checked.add(check) && check.getBlock().getType() == b.getType() && !BlockStorage.hasBlockInfo(b)) {
                found.add(b);
                getVein(checked, found, check, check.getBlock());
            }
        }
    }
    
    private static List<Location> getAdjacentLocations(Location l) {
        List<Location> list = new ArrayList<>();
        list.add(l.clone().add(1, 0, 0));
        list.add(l.clone().add(-1, 0, 0));
        list.add(l.clone().add(0, 1, 0));
        list.add(l.clone().add(0, -1, 0));
        list.add(l.clone().add(0, 0, 1));
        list.add(l.clone().add(0, 0, -1));
        Collections.shuffle(list);
        return list;
    }
    
}
