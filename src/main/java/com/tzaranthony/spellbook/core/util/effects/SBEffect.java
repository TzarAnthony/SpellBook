package com.tzaranthony.spellbook.core.util.effects;

import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

import java.util.ArrayList;

public class SBEffect extends MobEffect {
    public MobEffect effect;

    public SBEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (this == SBEffects.INCINERATION.get()) {
            affected.hurt(SBDamageSource.INCINERATION, 4.0F * (float) (amplifier + 1));
            affected.setSecondsOnFire(8);
        } else if (this == SBEffects.BLEEDING.get()) {
            if (affected.getMobType() != MobType.UNDEAD) {
                affected.hurt(SBDamageSource.BLEED, 2.0F * (float) (amplifier + 1));
            }
        } else { // Ender Infection
            affected.hurt(DamageSource.FALL, 2.0F * (float) (amplifier + 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this == SBEffects.INCINERATION.get()) {
            int i = 80 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } else if (this == SBEffects.BLEEDING.get()) {
            int i = 20 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    @Override
    public java.util.List<net.minecraft.world.item.ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}