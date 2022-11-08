package com.tzaranthony.spellbook.core.containers.slots;

import com.tzaranthony.spellbook.core.containers.menus.SBSmeltingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SBFuelSlot  extends SlotItemHandler {
    private final SBSmeltingMenu menu;

    public SBFuelSlot(SBSmeltingMenu menu, IItemHandler itemHandler, int id, int x, int y) {
        super(itemHandler, id, x, y);
        this.menu = menu;
    }

    public boolean mayPlace(ItemStack p_39526_) {
        return this.menu.isFuel(p_39526_) || isBucket(p_39526_);
    }

    public int getMaxStackSize(ItemStack p_39528_) {
        return isBucket(p_39528_) ? 1 : super.getMaxStackSize(p_39528_);
    }

    public static boolean isBucket(ItemStack p_39530_) {
        return p_39530_.is(Items.BUCKET);
    }
}