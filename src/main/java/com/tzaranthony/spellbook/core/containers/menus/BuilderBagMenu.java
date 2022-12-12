package com.tzaranthony.spellbook.core.containers.menus;

import com.tzaranthony.spellbook.registries.SBMenus;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class BuilderBagMenu extends SBBagMenu {
    public BuilderBagMenu(int id, Inventory inventory, Container container, ContainerData data, ItemStack bagStack) {
        super(SBMenus.BUILDER_BAG.get(), id, inventory, container, data, bagStack);
        checkContainerSize(inventory, 3);
        checkContainerDataCount(data, 2);
        this.addInputSlotArray(0, 17, 17, 3, 1, 45, 0);
        this.addInventory(8, 84);
    }
}