package com.tzaranthony.spellbook.core.network;

import com.tzaranthony.spellbook.core.spells.Binding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SoulBindS2CPacket {
    private int eID;
    private UUID uuid;
    private boolean add;

    public SoulBindS2CPacket(int boundEID, UUID uuid, boolean add) {
        this.eID = boundEID;
        this.uuid = uuid;
        this.add = add;
    }

    public static SoulBindS2CPacket read(FriendlyByteBuf buf) {
        return new SoulBindS2CPacket(buf.readInt(), buf.readUUID(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.eID);
        buf.writeUUID(this.uuid);
        buf.writeBoolean(this.add);
    }

    public static void handle(SoulBindS2CPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                Entity entity = Minecraft.getInstance().level.getEntity(message.eID);
                if (entity instanceof Mob mob) {
                    if (message.add) {
                        Binding.bindEntity(entity.level.getPlayerByUUID(message.uuid), mob);
                    } else {
                        Binding.unbindEntity(mob);
                    }
                }
            }
        });
    }
}