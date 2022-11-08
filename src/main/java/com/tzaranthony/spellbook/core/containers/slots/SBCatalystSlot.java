package com.tzaranthony.spellbook.core.containers.slots;

import com.tzaranthony.spellbook.core.containers.menus.SBSmeltingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SBCatalystSlot extends SlotItemHandler {
    private final SBSmeltingMenu menu;

    public SBCatalystSlot(SBSmeltingMenu menu, IItemHandler itemHandler, int id, int x, int y) {
        super(itemHandler, id, x, y);
        this.menu = menu;
    }

    public boolean mayPlace(ItemStack p_39526_) {
        return this.menu.isCatalyst(p_39526_);
    }
}