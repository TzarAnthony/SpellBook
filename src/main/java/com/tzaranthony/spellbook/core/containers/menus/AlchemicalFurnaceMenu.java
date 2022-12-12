package com.tzaranthony.spellbook.core.containers.menus;

import com.tzaranthony.spellbook.registries.SBMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class AlchemicalFurnaceMenu extends SBFurnaceMenu {
    public static int FuelSlotId = 1;
    public static int XPResultSlotId = 2;
    public static int ResultSlotId = 3;
    public static int FluidSlotId = 4;

    public AlchemicalFurnaceMenu(int id, Inventory inventory, BlockPos pos) {
        this(id, inventory, pos, new SimpleContainerData(4));
    }

    public AlchemicalFurnaceMenu(int id, Inventory inventory, BlockPos pos, ContainerData data) {
        super(SBMenus.ALCHEMICAL_FURNACE.get(), id, inventory, pos, data);

        checkContainerSize(inventory, 5);
        checkContainerDataCount(data, 4);

        this.addInputSlot(0, 62, 17);
        this.addFuelSlot(FuelSlotId, 62, 53);
        this.addXPResultSlot(XPResultSlotId, 122, 20);
        this.addResultSlot(ResultSlotId, 122, 52);
        this.addFluidSlot(FluidSlotId, 39, 17);
        this.addInventory(8, 84);

        this.addDataSlots(this.data);
    }
}