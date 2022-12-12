package com.tzaranthony.spellbook.core.containers.slots;

import com.tzaranthony.spellbook.core.containers.menus.SBBagMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SBBagSlot extends Slot {
    private final SBBagMenu menu;

    public SBBagSlot(SBBagMenu menu, Container container, int id, int x, int y) {
        super(container, id, x, y);
        this.menu = menu;
    }

    public boolean mayPlace(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof BlockItem && item.canFitInsideContainerItems();
    }

    public int getMaxStackSize(ItemStack stack) {
        return this.getMaxStackSize();
    }
}