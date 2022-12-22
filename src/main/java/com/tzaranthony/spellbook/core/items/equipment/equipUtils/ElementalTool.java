package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ElementalTool {
    String ELEMENT = "ToolElement";

    //TODO: player magic consumption
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

    default void nextElement(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ELEMENT)) {
            tag.putInt(ELEMENT, ChanneledElement.NOTHING.getId());
        }

        int id = tag.getInt(ELEMENT);
        if (id == ChanneledElement.NOTHING.getId()) {
            setElement(stack, ChanneledElement.FIRE);
        } else if (id == ChanneledElement.FIRE.getId()) {
            setElement(stack, ChanneledElement.WATER);
        } else if (id == ChanneledElement.WATER.getId()) {
            setElement(stack, ChanneledElement.AIR);
        } else if (id == ChanneledElement.AIR.getId()) {
            setElement(stack, ChanneledElement.EARTH);
        } else if (id == ChanneledElement.EARTH.getId()) {
            setElement(stack, ChanneledElement.NOTHING);
        }
    }

    default void setElement(ItemStack stack, ChanneledElement element) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(ELEMENT, element.getId());
    }

    default ChanneledElement getElement(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ELEMENT)) {
            tag.putInt(ELEMENT, ChanneledElement.NOTHING.getId());
            return ChanneledElement.NOTHING;
        }

        int id = tag.getInt(ELEMENT);
        if (id == ChanneledElement.FIRE.getId()) {
            return ChanneledElement.FIRE;
        } else if (id == ChanneledElement.WATER.getId()) {
            return ChanneledElement.WATER;
        } else if (id == ChanneledElement.AIR.getId()) {
            return ChanneledElement.AIR;
        } else if (id == ChanneledElement.EARTH.getId()) {
            return ChanneledElement.EARTH;
        }
        return ChanneledElement.NOTHING;
    }
}