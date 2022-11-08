package com.tzaranthony.spellbook.core.containers.handlers;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FluidTankHandler implements IFluidHandler {
    protected String TANKTAG = "FluidTanks";
    protected String SLOT = "Slot";
    protected NonNullList<FluidTank> tanks;

    public FluidTankHandler()
    {
        this(1, 1000);
    }

    public FluidTankHandler(List<Integer> capacities) {
        tanks = NonNullList.create();

        for (int i = 0; i < capacities.size(); ++i) {
            tanks.add(new FluidTank(capacities.get(i)));
        }
    }

    public FluidTankHandler(int size, int capacity) {
        tanks = NonNullList.create();
        for (int i = 0; i < size; ++i) {
            tanks.add(i, new FluidTank(capacity));
        }
    }

    public FluidTankHandler(NonNullList<FluidTank> tanks) {
        this.tanks = tanks;
    }

    public void setSize(int size) {
        tanks = NonNullList.create();
        for (int i = 0; i < size; ++i) {
            tanks.add(i, new FluidTank(1000));
        }
    }

    public CompoundTag serializeNBT() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < tanks.size(); i++) {
            CompoundTag fluidTankTag = new CompoundTag();
            tanks.get(i).writeToNBT(fluidTankTag);
            fluidTankTag.putInt(SLOT, i);
            nbtTagList.add(fluidTankTag);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put(TANKTAG, nbtTagList);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        ListTag tagList = nbt.getList(TANKTAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag fluidTankTag = tagList.getCompound(i);
            int slot = fluidTankTag.getInt(SLOT);
            if (slot >= 0 && slot < tanks.size()) {
                tanks.get(slot).readFromNBT(fluidTankTag);
            }
        }
    }

    public FluidTank getTank(int tankId) {
        return tanks.get(tankId);
    }

    @Override
    public int getTanks() {
        return tanks.size();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tanks.get(tank).getFluidInTank(tank);
    }

    public void setFluidInTank(int tank, FluidStack stack) {
        tanks.get(tank).setFluid(stack);
        onContentsChanged(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return tanks.get(tank).getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return tanks.get(tank).isFluidValid(tank, stack);
    }

    public int fill(int slot, FluidStack resource, FluidAction action) {
        int filled = 0;
        filled = tanks.get(slot).fill(resource, action);
        if (filled > 0) {
            onContentsChanged(slot);
        }
        return filled;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int filled = 0;
        for (int i = 0; i < tanks.size(); i++) {
            filled = tanks.get(i).fill(resource, action);
            if (filled > 0) {
                onContentsChanged(i);
                break;
            }
        }
        return filled;
    }

    @NotNull
    public FluidStack drain(int slot, FluidStack resource, FluidAction action) {
        FluidStack fluid = FluidStack.EMPTY;
        fluid = tanks.get(slot).drain(resource, action);
        onContentsChanged(slot);
        return fluid;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack fluid = FluidStack.EMPTY;
        for (int i = 0; i < tanks.size(); i++) {
            fluid = tanks.get(i).drain(resource, action);
            if (!fluid.isEmpty()) {
                onContentsChanged(i);
                break;
            }
        }
        return fluid;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack fluid = FluidStack.EMPTY;
        for (int i = 0; i < tanks.size(); i++) {
            fluid = tanks.get(i).drain(maxDrain, action);
            if (!fluid.isEmpty()) {
                onContentsChanged(i);
                break;
            }
        }
        return fluid;
    }

    protected void onContentsChanged(int slot) {

    }
}