package com.tzaranthony.spellbook.core.containers.slots;

import com.tzaranthony.spellbook.core.containers.menus.SBFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SBCatalystSlot extends SlotItemHandler {
    private final SBFurnaceMenu menu;

    public SBCatalystSlot(SBFurnaceMenu menu, IItemHandler itemHandler, int id, int x, int y) {
        super(itemHandler, id, x, y);
        this.menu = menu;
    }

    public boolean mayPlace(ItemStack p_39526_) {
        return this.menu.isCatalyst(p_39526_);
    }
}