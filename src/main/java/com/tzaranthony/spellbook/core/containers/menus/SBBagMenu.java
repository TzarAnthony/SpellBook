package com.tzaranthony.spellbook.core.containers.menus;

import com.tzaranthony.spellbook.core.containers.slots.SBBagSlot;
import com.tzaranthony.spellbook.core.items.otherEquipment.BuilderBag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SBBagMenu extends AbstractContainerMenu {
    protected final Container container;
    protected final ItemStack bagStack;
    protected final Level level;
    protected final Inventory inventory;
    protected int minInputId = -1;
    protected int maxInputId;

    protected SBBagMenu(@Nullable MenuType<?> type, int id, Inventory inventory, Container container, ContainerData data, final ItemStack bagStack) {
        super(type, id);
        this.container = container;
        this.bagStack = bagStack;
        this.inventory = inventory;
        this.level = inventory.player.level;
        container.startOpen(inventory.player);
    }

    public void addInputSlotArray(int startId, int startX, int startY, int cols, int rows, int xGap, int yGap) {
        if (this.minInputId == -1) {
            this.minInputId = startId;
        }
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                addInputSlot(startId, startX + j * (18 + xGap), startY + i * (18 + yGap));
                ++startId;
            }
        }
        this.maxInputId = startId - 1;
    }

    public void addInputSlot(int id, int x, int y) {
        this.addSlot(new SBBagSlot(this, this.container, id, x, y));
        if (this.minInputId == -1) {
            this.minInputId = id;
        }
        this.maxInputId = id;
    }

    public void addInventory() {
        this.addInventory(8, 84);
    }

    public void addInventory(int x, int y) {
        // add inventory
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(this.inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
        // add hotbar
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(this.inventory, k, x + k * 18, y + 58));
        }
    }

    public ItemStack quickMoveStack(@NotNull Player player, int id) {
        int invSt = this.maxInputId + 1;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(id);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (id > invSt) { // move from inventory to bag
                if (canBag(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, this.minInputId, this.maxInputId + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (id < 28 + invSt) { // move to hotbar if can't move to bag
                    if (!this.moveItemStackTo(itemstack1, 28 + invSt, 36 + invSt, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (id >= 28 + invSt && id < 36 + invSt && !this.moveItemStackTo(itemstack1, invSt, invSt + 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, invSt, 36 + invSt, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public boolean canBag(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof BlockItem && item.canFitInsideContainerItems();
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().contains(this.bagStack);
    }

    @Override
    public void removed(Player player) {
//        ((BuilderBag) this.bagStack.getItem()).saveItems(player, this.bagStack, this.container);
        ((BuilderBag) this.bagStack.getItem()).playCloseSound(player);
        super.removed(player);
    }
}