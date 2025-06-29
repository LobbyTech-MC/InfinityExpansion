package io.github.mooy1.infinityexpansion.items.storage;

import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.DISPLAY_KEY;
import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.DISPLAY_SLOT;
import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.EMPTY_KEY;
import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.INPUT_SLOT;
import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.INTERACT_SLOT;
import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.OUTPUT_SLOT;
import static io.github.mooy1.infinityexpansion.items.storage.StorageUnit.STATUS_SLOT;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import lombok.Setter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;

/**
 * Represents a single storage unit with cached data
 *
 * @author Mooy1
 */
public final class StorageCache {

    /* Menu strings */
    private static final String EMPTY_DISPLAY_NAME = ChatColor.WHITE + "空";
    private static final String VOID_EXCESS_TRUE = ChatColors.color("&7满载时清空输入:&e 开");
    private static final String VOID_EXCESS_FALSE = ChatColors.color("&7满载时清空输入:&e 关");

    /* BlockStorage keys */
    private static final String STORED_AMOUNT = "stored"; // amount key in block data
    private static final String VOID_EXCESS = "void_excess"; // void excess true or null key

    /* Menu Items */
    private static final ItemStack EMPTY_ITEM = new CustomItemStack(Material.BARRIER, meta -> {
        meta.setDisplayName(ChatColor.WHITE + "空");
        meta.getPersistentDataContainer().set(EMPTY_KEY, PersistentDataType.BYTE, (byte) 1);
    });

    /* Space Pattern for Sign Display Names */
    private static final Pattern SPACE = Pattern.compile(" ");

    /* Instance Constants */
    private final StorageUnit storageUnit;
    private final BlockMenu menu;

    /* Instance Variables */
    private final String[] signDisplay = new String[2];
    private String displayName;
    private Material material;
    private ItemMeta meta;
    private boolean voidExcess;
    @Setter
    private int amount;

    public StorageCache(StorageUnit storageUnit, BlockMenu menu) {
        this.storageUnit = storageUnit;
        this.menu = menu;

        // load data
        reloadData();

        if (isEmpty()) {
            // empty
            setEmptyDisplayName();
            menu.replaceExistingItem(DISPLAY_SLOT, EMPTY_ITEM);
        }
        else {
            // something is stored
            ItemStack display = menu.getItemInSlot(DISPLAY_SLOT);
            if (display != null) {
                ItemMeta copy = display.getItemMeta();
                // fix if they somehow store the empty item
                if (copy.getPersistentDataContainer().has(EMPTY_KEY, PersistentDataType.BYTE)) {
                    // attempt to recover the correct item from output
                    ItemStack output = menu.getItemInSlot(OUTPUT_SLOT);
                    if (output != null) {
                        setStored(output);
                        menu.replaceExistingItem(OUTPUT_SLOT, null);
                    }
                    else {
                        // no output to recover
                        menu.replaceExistingItem(DISPLAY_SLOT, EMPTY_ITEM);
                        setEmptyDisplayName();
                        this.amount = 0;
                    }
                }
                else {
                    // load the item in menu
                    load(display, copy);
                }
            }
        }

        // void excess handler
        menu.addMenuClickHandler(STATUS_SLOT, (p, slot, item, action) -> {
            this.voidExcess = !this.voidExcess;
            if (this.voidExcess) {
                StorageCacheUtils.setData(this.menu.getLocation(), VOID_EXCESS, "true");
            } else {
                StorageCacheUtils.removeData(this.menu.getLocation(), VOID_EXCESS);
            }

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            lore.set(1, this.voidExcess ? VOID_EXCESS_TRUE : VOID_EXCESS_FALSE);
            meta.setLore(lore);
            item.setItemMeta(meta);
            return false;
        });

        // interact handler
        menu.addMenuClickHandler(INTERACT_SLOT, (p, slot, item, action) -> {
            if (this.amount == 1) {
                if (action.isShiftClicked() && !action.isRightClicked()) {
                    depositAll(p);
                }
                else {
                    withdrawLast(p);
                }
            }
            else if (!isEmpty()) {
                if (action.isRightClicked()) {
                    if (action.isShiftClicked()) {
                        withdraw(p, this.amount - 1);
                    }
                    else {
                        final int maxStackSize = this.material != null ? this.material.getMaxStackSize() : 1;
                        withdraw(p, Math.min(maxStackSize, this.amount - 1));
                    }
                }
                else {
                    if (action.isShiftClicked()) {
                        depositAll(p);
                    }
                    else {
                        withdraw(p, 1);
                    }
                }
            }
            return false;
        });

        // load status slot
        updateStatus();
    }

    private void setDisplayName(String name) {
        this.displayName = name;

        int len = name.length();
        if (len == 0) {
            this.signDisplay[0] = "";
            this.signDisplay[1] = "";
            return;
        }

        String color;
        if (len >= 2 && name.charAt(0) == ChatColor.COLOR_CHAR) {
            char second = name.charAt(1);
            if (len >= 14 && second == 'x') {
                color = name.substring(0, 14);
            }
            else {
                color = new String(new char[] {
                        ChatColor.COLOR_CHAR, second
                });
            }
        }
        else {
            color = null;
        }

        if (name.length() <= 15) {
            this.signDisplay[0] = color != null ? name : ChatColor.WHITE + name;
            this.signDisplay[1] = "";
            return;
        }

        String[] words = SPACE.split(name);
        int i = 1;
        StringBuilder firstLine = new StringBuilder();
        if (color == null) {
            firstLine.append(ChatColor.WHITE);
        }
        firstLine.append(words[0]);
        while (i < words.length && words[i].length() + firstLine.length() < 15) {
            firstLine.append(' ').append(words[i++]);
        }
        this.signDisplay[0] = firstLine.toString();

        if (i < words.length) {
            StringBuilder secondLine = new StringBuilder();
            String first = words[i++];
            if (first.length() <= 1 || first.charAt(0) != ChatColor.COLOR_CHAR) {
                if (color == null) {
                    secondLine.append(ChatColor.WHITE);
                }
                else {
                    secondLine.append(color);
                }
            }
            secondLine.append(first);
            while (i < words.length) {
                secondLine.append(' ').append(words[i++]);
            }
            this.signDisplay[1] = secondLine.toString();
        }
        else {
            this.signDisplay[1] = "";
        }
    }

    private void setEmptyDisplayName() {
        this.displayName = EMPTY_DISPLAY_NAME;
        this.signDisplay[0] = EMPTY_DISPLAY_NAME;
        this.signDisplay[1] = "";
    }

    void destroy(BlockBreakEvent e, List<ItemStack> drops) {

        // add output slot
        ItemStack output = this.menu.getItemInSlot(OUTPUT_SLOT);
        if (output != null && matches(output)) {
            int add = Math.min(this.storageUnit.max - this.amount, output.getAmount());
            if (add != 0) {
                this.amount += add;
                output.setAmount(output.getAmount() - add);
            }
        }

        ItemStack drop = this.storageUnit.getItem().clone();
        ItemStack displayItem = this.menu.getItemInSlot(DISPLAY_SLOT);
        if (displayItem == null || displayItem.getType().isAir()) {
            e.getPlayer().sendMessage(ChatColor.RED + "物品丢失，无法恢复");
        } else {
            drop.setItemMeta(StorageUnit.saveToStack(drop.getItemMeta(), this.menu.getItemInSlot(DISPLAY_SLOT), this.displayName, this.amount));
            e.getPlayer().sendMessage(ChatColor.GREEN + "物品仍保存在存储单元中");
        }
        drops.add(drop);
    }

    void reloadData() {
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(this.menu.getLocation());
        if (blockData == null || !blockData.isDataLoaded()) {
            return;
        }
        String amt = blockData.getData(STORED_AMOUNT);
        this.amount = amt == null ? 0 : Integer.parseInt(amt);
        this.voidExcess = "true".equals(blockData.getData(VOID_EXCESS));
    }

    void load(ItemStack stored, ItemMeta copy) {
        this.menu.replaceExistingItem(DISPLAY_SLOT, stored);

        // remove the display key from copy
        copy.getPersistentDataContainer().remove(DISPLAY_KEY);

        // check if the copy has anything besides the display key
        if (copy.equals(Bukkit.getItemFactory().getItemMeta(stored.getType()))) {
            this.meta = null;
        }
        else {
            this.meta = copy;
        }
        setDisplayName(ItemStackHelper.getDisplayName(stored));
        this.material = stored.getType();
    }

    void input() {
        ItemStack input = this.menu.getItemInSlot(INPUT_SLOT);
        if (input == null) {
            return;
        }
        if (isEmpty()) {
            // set the stored item to input
            this.amount = input.getAmount();
            setStored(input);
            this.menu.replaceExistingItem(INPUT_SLOT, null, false);
        }
        else if (matches(input)) {
            if (this.voidExcess) {
                // input and void excess
                if (this.amount < this.storageUnit.max) {
                    this.amount = Math.min(this.amount + input.getAmount(), this.storageUnit.max);
                }
                input.setAmount(0);
            }
            else if (this.amount < this.storageUnit.max) {
                // input as much as possible
                if (input.getAmount() + this.amount >= this.storageUnit.max) {
                    // last item
                    input.setAmount(input.getAmount() - (this.storageUnit.max - this.amount));
                    this.amount = this.storageUnit.max;
                }
                else {
                    this.amount += input.getAmount();
                    input.setAmount(0);
                }
            }
        }
    }

    private void output() {
        if (this.amount == 0 || this.material == null) {
            return;
        }
        ItemStack outputSlot = this.menu.getItemInSlot(OUTPUT_SLOT);
        if (outputSlot == null) {
            if (this.amount == 1) {
                this.menu.replaceExistingItem(OUTPUT_SLOT, createItem(1), false);
                setEmpty();
            }
            else {
                int amt = Math.min(this.material.getMaxStackSize(), this.amount - 1);
                this.menu.replaceExistingItem(OUTPUT_SLOT, createItem(amt), false);
                this.amount -= amt;
            }
        }
        else if (this.amount > 1) {
            int amt = Math.min(this.material.getMaxStackSize() - outputSlot.getAmount(), this.amount - 1);
            if (amt != 0 && matches(outputSlot)) {
                outputSlot.setAmount(outputSlot.getAmount() + amt);
                this.amount -= amt;
            }
        }
    }

    void tick(Block block) {
        // input output
        input();
        output();

        // store amount
        StorageCacheUtils.setData(this.menu.getLocation(), STORED_AMOUNT, String.valueOf(this.amount));

        // status
        if (this.menu.hasViewer()) {
            updateStatus();
        }

        // signs
        if (InfinityExpansion.slimefunTickCount() % 20 == 0) {
            Block check = block.getRelative(0, 1, 0);
            if (SlimefunTag.SIGNS.isTagged(check.getType())
                    || checkWallSign(check = block.getRelative(1, 0, 0), block)
                    || checkWallSign(check = block.getRelative(-1, 0, 0), block)
                    || checkWallSign(check = block.getRelative(0, 0, 1), block)
                    || checkWallSign(check = block.getRelative(0, 0, -1), block)
            ) {
                Sign sign = (Sign) check.getState();
                sign.setLine(0, this.signDisplay[0]);
                sign.setLine(1, this.signDisplay[1]);
                sign.setLine(2, ChatColor.GRAY + "------------");
                sign.setLine(3, ChatColor.YELLOW.toString() + this.amount);
                sign.update();
            }
        }
    }

    private void updateStatus() {
        this.menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, meta -> {
            meta.setDisplayName(ChatColor.AQUA + "状态");
            List<String> lore = new ArrayList<>();
            if (this.amount == 0) {
                lore.add(ChatColors.color("&6已储存：&e0 / " + MachineLore.format(this.storageUnit.max) + " &7(0%)"));
            }
            else {
                lore.add(ChatColors.color("&6已储存：&e" + MachineLore.format(this.amount)
                        + " / " + MachineLore.format(this.storageUnit.max)
                        + " &7(" + MachineLore.format((double) this.amount * 100.D / this.storageUnit.max) + "%)"
                ));
            }
            lore.add(this.voidExcess ? VOID_EXCESS_TRUE : VOID_EXCESS_FALSE);
            lore.add(ChatColor.GRAY + "(点击切换)");
            meta.setLore(lore);
        }), false);
    }

    private static boolean checkWallSign(Block sign, Block block) {
        return SlimefunTag.WALL_SIGNS.isTagged(sign.getType())
                && sign.getRelative(((WallSign) sign.getBlockData()).getFacing().getOppositeFace()).equals(block);
    }

    private void setStored(ItemStack input) {
        this.meta = input.hasItemMeta() ? input.getItemMeta() : null;
        setDisplayName(ItemStackHelper.getDisplayName(input));
        this.material = input.getType();

        // add the display key to the display input and set amount 1
        ItemMeta meta = input.getItemMeta();
        meta.getPersistentDataContainer().set(DISPLAY_KEY, PersistentDataType.BYTE, (byte) 1);
        input.setItemMeta(meta);
        input.setAmount(1);

        this.menu.replaceExistingItem(DISPLAY_SLOT, input);
    }

    private void setEmpty() {
        setEmptyDisplayName();
        this.meta = null;
        this.material = null;
        this.menu.replaceExistingItem(DISPLAY_SLOT, EMPTY_ITEM);
        this.amount = 0;
    }

    public boolean matches(ItemStack item) {
        return item.getType() == this.material
                && item.hasItemMeta() == (this.meta != null)
                && (this.meta == null || this.meta.equals(item.getItemMeta()));
    }

    private ItemStack createItem(int amount) {
        ItemStack item = new ItemStack(this.material, amount);
        if (this.meta != null) {
            item.setItemMeta(this.meta);
        }
        return item;
    }

    boolean isEmpty() {
        return this.amount == 0;
    }

    private void withdraw(Player p, int withdraw) {
        if (this.material.getMaxStackSize() == 64) {
            ItemStack remaining = p.getInventory().addItem(createItem(withdraw)).get(0);
            if (remaining != null) {
                if (remaining.getAmount() != withdraw) {
                    this.amount += remaining.getAmount() - withdraw;
                }
            }
            else {
                this.amount -= withdraw;
            }
            return;
        }

        Inventory inv = p.getInventory();
        int toWithdraw = withdraw;
        do {
            int amt = Math.min(this.material.getMaxStackSize(), toWithdraw);
            ItemStack remaining = inv.addItem(createItem(amt)).get(0);
            if (remaining != null) {
                toWithdraw -= amt - remaining.getAmount();
                break;
            }
            else {
                toWithdraw -= amt;
            }
        }
        while (toWithdraw > 0);
        if (toWithdraw != withdraw) {
            this.amount += toWithdraw - withdraw;
        }
    }

    private void withdrawLast(Player p) {
        if (p.getInventory().addItem(createItem(1)).get(0) == null) {
            setEmpty();
        }
    }

    private void depositAll(Player p) {
        depositAll(p.getInventory().getStorageContents());
    }

    public void depositAll(ItemStack[] itemStacks) {
        depositAll(itemStacks, false);
    }

    public void depositAll(ItemStack[] itemStacks, boolean observeVoiding) {
        if (this.amount < this.storageUnit.max) {
            for (ItemStack item : itemStacks) {
                if (item != null && matches(item)) {
                    if (item.getAmount() + this.amount >= this.storageUnit.max) {
                        // last item
                        item.setAmount(item.getAmount() - (this.storageUnit.max - this.amount));
                        this.amount = this.storageUnit.max;
                    }
                    else {
                        this.amount += item.getAmount();
                        item.setAmount(0);
                    }
                }
            }
        }
        if (observeVoiding && this.voidExcess) {
            for (ItemStack item : itemStacks) {
                if (item != null && matches(item)) {
                    item.setAmount(0);
                }
            }
        }
    }
    
    public int amount() {
    	return amount;
    }
    
    public Material material() {
    	return material;
    }
    
    public ItemMeta meta() {
    	return meta;
    }

}
