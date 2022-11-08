package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.containers.handlers.FluidTankHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class SBCraftingBE extends BlockEntity {
    protected int progress;
    protected String PROG = "Progress";
    protected int maxTime;
    protected final String MAXTM = "TimeNeeded";
    protected final RecipeType recipeType;
    protected ItemStackHandler itemHandler;
    protected final String ITEMINV = "Items";
    protected FluidTankHandler fluidHandler;
    protected final String FLUIDINV = "Fluids";
    protected LazyOptional<?> itemCap = LazyOptional.empty();
    protected LazyOptional<?> fluidCap = LazyOptional.empty();

    public SBCraftingBE(BlockEntityType<?> type, BlockPos pos, BlockState state, RecipeType recipeType) {
        super(type, pos, state);
        this.recipeType = recipeType;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt(PROG);
        this.maxTime = tag.getInt(MAXTM);
        this.itemHandler.deserializeNBT(tag);
        if (itemHandler.getSlots() > 0) {
            itemHandler.deserializeNBT(tag.getCompound(ITEMINV));
        }
        if (fluidHandler.getTanks() > 0) {
            fluidHandler.deserializeNBT(tag.getCompound(FLUIDINV));
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(PROG, this.progress);
        tag.putInt(MAXTM, this.maxTime);
        if (itemHandler.getSlots() > 0) {
            tag.put(ITEMINV, itemHandler.serializeNBT());
        }
        if (fluidHandler.getTanks() > 0) {
            tag.put(FLUIDINV, fluidHandler.serializeNBT());
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        return compoundtag;
    }

    protected static boolean checkResult(ItemStack outputStack, ItemStack resultSlotStack, int resultSlotLimit) {
        if (outputStack.isEmpty()) {
            return false;
        } else {
            if (resultSlotStack.isEmpty()) {
                return true;
            } else if (!resultSlotStack.sameItem(outputStack)) {
                return false;
            } else if (resultSlotStack.getCount() + outputStack.getCount() <= resultSlotLimit
                    && resultSlotStack.getCount() + outputStack.getCount() <= resultSlotStack.getMaxStackSize()) {
                return true;
            } else {
                return resultSlotStack.getCount() + outputStack.getCount() <= outputStack.getMaxStackSize();
            }
        }
    }

    // capabilities
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.itemHandler.getSlots() > 0) {
            return (LazyOptional.of(() -> itemHandler)).cast();
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.fluidHandler.getTanks() > 0) {
            return (LazyOptional.of(() -> fluidHandler)).cast();
        }

        return super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemCap.invalidate();
        fluidCap.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemCap = LazyOptional.of(() -> itemHandler);
        fluidCap = LazyOptional.of(() -> fluidHandler);
    }

    public Container createContainer() {
        Container container = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }
        return container;
    }

    public void dropInventory() {
        Containers.dropContents(this.level, this.worldPosition, this.createContainer());
    }

    public void dropFluid() {
        int maxFluid = -1;
        FluidStack current = FluidStack.EMPTY;
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            if (maxFluid < fluidHandler.getFluidInTank(i).getAmount()) {
                current = fluidHandler.getFluidInTank(i);
                maxFluid = current.getAmount();
            }
        }

        if (!current.isEmpty()) {
            this.level.setBlock(this.worldPosition, current.getFluid().defaultFluidState().createLegacyBlock(), 11);
        }
    }
}