package com.tzaranthony.spellbook.core.network;

import com.tzaranthony.spellbook.core.blockEntities.SBCraftingWScreenBE;
import com.tzaranthony.spellbook.core.containers.handlers.FluidTankHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class FluidS2CPacket {
    private FluidTankHandler fluidTanks;
    private List<Integer> capacities;
    private BlockPos pos;

    public FluidS2CPacket(FluidTankHandler fluidTanks, BlockPos pos) {
        this.fluidTanks = fluidTanks;
        this.capacities = this.getTankCapacities(this.fluidTanks);
        this.pos = pos;
    }

    public FluidS2CPacket(FriendlyByteBuf buf) {
        read(buf);
    }

    public List<Integer> getTankCapacities(FluidTankHandler fluidTanks) {
        List<Integer> amounts = new ArrayList<Integer>();
        for (int i = 0; i < fluidTanks.getTanks(); ++i) {
            amounts.add(fluidTanks.getTankCapacity(i));
        }
        return amounts;
    }

    public void read(FriendlyByteBuf buf) {
        capacities = buf.readCollection(ArrayList::new, FriendlyByteBuf::readInt);
        List<FluidStack> fluids = buf.readCollection(ArrayList::new, FriendlyByteBuf::readFluidStack);
        fluidTanks = new FluidTankHandler(capacities);
        for (int i = 0; i < fluids.size(); i++) {
            fluidTanks.setFluidInTank(i, fluids.get(i));
        }
        pos = buf.readBlockPos();
    }

    public void write(FriendlyByteBuf buf) {
        Collection<Integer> amounts = new ArrayList<>();
        Collection<FluidStack> fluids = new ArrayList<>();
        for(int i = 0; i < fluidTanks.getTanks(); i++) {
            amounts.add(fluidTanks.getTankCapacity(i));
            fluids.add(fluidTanks.getFluidInTank(i));
        }
        buf.writeCollection(amounts, FriendlyByteBuf::writeInt);
        buf.writeCollection(fluids, FriendlyByteBuf::writeFluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof SBCraftingWScreenBE blockEntity) {
                blockEntity.setFluidHandler(this.fluidTanks);
            }
        });
        return true;
    }
}