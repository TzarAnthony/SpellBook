package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import com.tzaranthony.spellbook.core.util.events.SBToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface MiningModeTool {
    String MINING_MODE = "ToolMode";

    default void mineBlocks(ItemStack stack, ServerLevel level, ServerPlayer player, BlockPos pos) {
        int mode = getMiningMode(stack);
        switch (mode) {
            case 1:
                SBToolUtils.areaMine(level, player, pos, stack, 1, 1);
            case 2:
                SBToolUtils.areaMine(level, player, pos, stack, 2, 2);
            case 3:
                SBToolUtils.veinMine(level, player, pos, level.getBlockState(pos).getBlock(), 49);
        }
    }

    default List<Component> getHoverDetails(ItemStack stack, List<Component> tooltip) {
        int mode = getMiningMode(stack);
        switch (mode) {
            case 1:
                tooltip.add((new TranslatableComponent("tooltip.spellbook.area_mode")).append("3x3"));
            case 2:
                tooltip.add((new TranslatableComponent("tooltip.spellbook.area_mode")).append("5x5"));
            case 3:
                tooltip.add(new TranslatableComponent("tooltip.spellbook.vein_mode"));
        }
        return tooltip;
    }

    default void consumeMP(LivingEntity entity, ChanneledElement element) {
        int x = 100000;
        x -= element.getMPUseValue();
    }

    default boolean canUseMP(LivingEntity entity, ChanneledElement element) {
        if (100000 > element.getMPUseValue()) {
            return true;
        }
        return false;
    }

    default void playChangeSound(Level level, LivingEntity entity) {
        level.playSound((Player) null, entity.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.8F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    default void nextMiningMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MINING_MODE)) {
            tag.putInt(MINING_MODE, 0);
        }
        int id = tag.getInt(MINING_MODE);
        if (++id > 3) {
            id = 0;
        }
        setMiningMode(tag, id);
    }

    default void setMiningMode(CompoundTag tag, int id) {
        tag.putInt(MINING_MODE, id);
    }

    default int getMiningMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MINING_MODE)) {
            tag.putInt(MINING_MODE, 0);
            return 0;
        }
        return tag.getInt(MINING_MODE);
    }
}