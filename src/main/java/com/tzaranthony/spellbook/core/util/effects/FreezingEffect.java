package com.tzaranthony.spellbook.core.util.effects;

import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class FreezingEffect extends SBEffect{
    public MobEffect effect;

    public FreezingEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (this == SBEffects.FREEZING.get()) {
            affected.hurt(DamageSource.FREEZE, 4.0F * (float) (amplifier + 1));
            affected.addEffect(new MobEffectInstance(SBEffects.FREEZING_ANIMATION.get(), 70, 0, true, false));
        } else if (this == SBEffects.FREEZING_ANIMATION.get()) {
            affected.setIsInPowderSnow(true);
            affected.setTicksFrozen(affected.getTicksFrozen() + 4);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this == SBEffects.FREEZING.get()) {
            int i = 60 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } else if (this == SBEffects.FREEZING_ANIMATION.get()) {
            int i = 1 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}