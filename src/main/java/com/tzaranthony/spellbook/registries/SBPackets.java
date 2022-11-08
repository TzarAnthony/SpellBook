package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.network.FluidS2CPacket;
import com.tzaranthony.spellbook.core.network.ItemS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class SBPackets {
    private static SimpleChannel net;
    private static int id = 0;
    private static int getId() {
        return id++;
    }

    public static void registerPackets() {
        net = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(SpellBook.MOD_ID, "network"), () -> "1.0", v -> true, v -> true
        );

        net.messageBuilder(FluidS2CPacket.class, getId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidS2CPacket::new)
                .encoder(FluidS2CPacket::write)
                .consumer(FluidS2CPacket::handle)
                .add();

        net.messageBuilder(ItemS2CPacket.class, getId(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemS2CPacket::new)
                .encoder(ItemS2CPacket::write)
                .consumer(ItemS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        net.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        net.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        net.send(PacketDistributor.ALL.noArg(), message);
    }
}