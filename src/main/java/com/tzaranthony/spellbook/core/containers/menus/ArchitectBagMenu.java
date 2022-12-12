package com.tzaranthony.spellbook.core.containers.menus;

import com.tzaranthony.spellbook.registries.SBMenus;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class ArchitectBagMenu extends SBBagMenu {
    public ArchitectBagMenu(int id, Inventory inventory, Container container, ContainerData data, ItemStack bagStack) {
        super(SBMenus.ARCH_BAG.get(), id, inventory, container, data, bagStack);
        checkContainerSize(inventory, 6);
        checkContainerDataCount(data, 2);
        this.addInputSlotArray(0, 13, 11, 2, 3, 7, 4);
        this.addInventory(8, 84);
    }
}