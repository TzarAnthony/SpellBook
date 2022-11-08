package com.tzaranthony.spellbook.core.containers.menus;

import com.tzaranthony.spellbook.registries.SBMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class AlchemicalFoundryMenu extends SBSmeltingMenu {
    public static int CatalystSlotId = 6;
    public static int FuelSlotId = 7;
    public static int XPResultSlotId = 8;
    public static int ResultSlotId = 9;
    public static int FluidSlotId1 = 10;
    public static int FluidSlotId2 = 11;

    public AlchemicalFoundryMenu(int id, Inventory inventory, BlockPos pos) {
        this(id, inventory, pos, new SimpleContainerData(4));
    }

    public AlchemicalFoundryMenu(int id, Inventory inventory, BlockPos pos, ContainerData data) {
        super(SBMenus.ALCHEMICAL_FOUNDRY.get(), id, inventory, pos, data);

        checkContainerSize(inventory, 12);
        checkContainerDataCount(data, 4);

        this.addInputSlotArray(0, 71, 24, 2, 1);
        this.addCatalystSlot(CatalystSlotId, 76, 78);
        this.addFuelSlot(FuelSlotId, 102, 78);
        this.addXPResultSlot(XPResultSlotId, 158, 41);
        this.addResultSlot(ResultSlotId, 158, 73);
        this.addFluidSlot(FluidSlotId1, 10, 8);
        this.addFluidSlot(FluidSlotId2, 44, 8);
        this.addInventory(17, 108);
        this.addDataSlots(this.data);
    }
}