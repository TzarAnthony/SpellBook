package com.tzaranthony.spellbook.core.util.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeHooks;

public class ClumsyEffect extends SBEffect {
    public MobEffect effect;

    public ClumsyEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        int slot = affected.level.random.nextInt(6);

        // lol u tripped
        if ((slot % 2  > 0) &&  affected instanceof Player && ((Player) affected).isSprinting()) {
            affected.hurt(DamageSource.FALL, 1 + affected.getArmorValue()/5);
        }

        // remove armor
        if (!affected.level.isClientSide()) {
            switch (slot) {
                case 1:
                    dropItemInSlot(affected, EquipmentSlot.HEAD, amplifier);
                case 2:
                    dropItemInSlot(affected, EquipmentSlot.CHEST, amplifier);
                case 3:
                    dropItemInSlot(affected, EquipmentSlot.LEGS, amplifier);
                case 4:
                    dropItemInSlot(affected, EquipmentSlot.FEET, amplifier);
                case 5:
                    dropItemInSlot(affected, EquipmentSlot.MAINHAND, amplifier);
                default:
                    dropItemInSlot(affected, EquipmentSlot.OFFHAND, amplifier);
            }
        }
    }

    public boolean dropItemInSlot(LivingEntity affected, EquipmentSlot slot, int amplifier) {
        ItemStack stack = affected.getItemBySlot(slot);
        if (stack.isEmpty() || EnchantmentHelper.hasBindingCurse(stack)) {
            return false;
        }

        affected.setItemSlot(slot, ItemStack.EMPTY);
        if (affected instanceof Player) {
            stack.onDroppedByPlayer((ServerPlayer) affected);
            ItemEntity itemEntity = ForgeHooks.onPlayerTossEvent((ServerPlayer) affected, stack, true);
            itemEntity.setPickUpDelay(20 + (10 * amplifier));
            return true;
        }
        affected.spawnAtLocation(stack).setPickUpDelay(20 + (10 * amplifier));
        return true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 40 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}