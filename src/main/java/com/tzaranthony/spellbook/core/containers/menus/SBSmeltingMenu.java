package com.tzaranthony.spellbook.core.containers.menus;

import com.tzaranthony.spellbook.core.blockEntities.SBCraftingWScreenBE;
import com.tzaranthony.spellbook.core.containers.slots.*;
import com.tzaranthony.spellbook.core.util.tags.SBItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SBSmeltingMenu extends AbstractContainerMenu {
    protected final SBCraftingWScreenBE blockEntity;
    protected final ItemStackHandler container;
    protected final ContainerData data;
    protected final Level level;
    protected final Inventory inventory;
    protected int minInputId = -1;
    protected int maxInputId;
    protected int minFluidId = -1;
    protected int maxFluidId;
    protected int fuelId;
    protected int catalystId;
    protected int resultXpId;
    protected int resultId;

    protected SBSmeltingMenu(@Nullable MenuType<?> type, int id, Inventory inventory, BlockPos pos, ContainerData data) {
        super(type, id);
        this.blockEntity = (SBCraftingWScreenBE) inventory.player.level.getBlockEntity(pos);
        this.container = blockEntity.getItemHandler();
        this.data = data;
        this.inventory = inventory;
        this.level = inventory.player.level;
    }

    public void addInputSlotArray(int startId, int startX, int startY, int cols, int rows) {
        if (this.minInputId == -1) {
            this.minInputId = startId;
        }
        for (int i = 0; i <= rows; ++i) {
            for (int j = 0; j <= cols; ++j) {
                addInputSlot(startId, startX + (18 * j), startY + (18 * i));
                ++startId;
            }
        }
        this.maxInputId = startId - 1;
    }

    public void addInputSlot(int id, int x, int y) {
        this.addSlot(new SlotItemHandler(this.container, id, x, y));
        if (this.minInputId == -1) {
            this.minInputId = id;
        }
        this.maxInputId = id;
    }

    public void addFluidSlot(int idI, int x, int y) {
        this.addSlot(new SBFluidSlot(this, this.container, idI, x, y));
        if (this.minFluidId == -1) {
            this.minFluidId = idI;
        }
        this.maxFluidId = idI;
    }

    public void addFuelSlot(int id, int x, int y) {
        this.addSlot(new SBFuelSlot(this, this.container, id, x, y));
        this.fuelId = id;
    }


    public void addCatalystSlot(int id, int x, int y) {
        this.addSlot(new SBCatalystSlot(this, this.container, id, x, y));
        this.catalystId = id;
    }

    public void addXPResultSlot(int id, int x, int y) {
        this.addSlot(new SBResultXPSlot(this.inventory.player, this.container, id, x, y));
        this.resultXpId = id;
    }

    public void addResultSlot(int id, int x, int y) {
        this.addSlot(new SBResultSlot(this.inventory.player, this.container, id, x, y));
        this.resultId = id;
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
        int invSt = Math.max(Math.max(this.catalystId, Math.max(this.resultId, this.resultXpId)), Math.max(Math.max(this.maxInputId, this.maxFluidId), this.fuelId)) + 1;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(id);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (id == this.resultId || id == this.resultXpId) {
                if (!this.moveItemStackTo(itemstack1, invSt, 36 + invSt, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (id > invSt) { // move from inventory
                if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, this.fuelId, this.fuelId + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isCatalyst(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, this.catalystId, this.catalystId + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFluid(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, this.minFluidId, this.maxFluidId + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.canProcess(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, this.minInputId, this.maxInputId + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (id < 28 + invSt) { // move to hotbar if can't move to machine
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

    public boolean canProcess(ItemStack stack) {
        return true;
    }

    public boolean isFluid(ItemStack stack) {
        return !stack.is(Items.BUCKET) && stack.getItem() instanceof BucketItem;
    }

    public boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
    }

    public boolean isCatalyst(ItemStack stack) {
        return stack.is(SBItemTags.CATALYSTS);
    }

    // misc
    public SBCraftingWScreenBE getBE() {
        return this.blockEntity;
    }

    public int getBurnProgress() {
        int current = this.data.get(2);
        int max = this.data.get(3);
        return max != 0 && current != 0 ? current * 18 / max : 0;
    }

    public int getLitProgress() {
        int burn = this.data.get(1);
        if (burn == 0) {
            burn = 200;
        }
        return this.data.get(0) * 13 / burn;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
//        blockEntity.sendGUINetworkData(this, player);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.blockEntity.stillValid(player);
    }
}