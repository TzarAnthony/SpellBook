package com.tzaranthony.spellbook.core.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SoulBindS2CPacket {
    private int bEID;
    private String oBoolean;
    private String oUUID;
    private String oEID;

    public SoulBindS2CPacket(int boundEID, String oBoolean, String oUUID, String oEID) {
        this.bEID = boundEID;
        this.oBoolean = oBoolean;
        this.oUUID = oUUID;
        this.oEID = oEID;
    }

    public SoulBindS2CPacket(FriendlyByteBuf buf) {
        read(buf);
    }

    public void read(FriendlyByteBuf buf) {
        bEID = buf.readInt();
        oBoolean = buf.readUtf();
        oUUID = buf.readUtf();
        oEID = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(bEID);
        buf.writeUtf(oBoolean);
        buf.writeUtf(oUUID);
        buf.writeUtf(oEID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {

            } else {
                Entity entity = context.getSender().level.getEntity(this.bEID);
                if(entity instanceof Mob) {
                    entity.addTag(oBoolean);
                    entity.addTag(oUUID);
                    entity.addTag(oEID);
                }
            }
        });
        return true;
    }
}