package com.tzaranthony.spellbook.core.containers.slots;

import com.tzaranthony.spellbook.core.containers.menus.SBFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SBFluidSlot extends SlotItemHandler {
    private final SBFurnaceMenu menu;

    public SBFluidSlot(SBFurnaceMenu menu, IItemHandler itemHandler, int idI, int x, int y) {
        super(itemHandler, idI, x, y);
        this.menu = menu;
    }

    public boolean mayPlace(ItemStack stack) {
        return this.menu.isFluid(stack) || isBucket(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }
}