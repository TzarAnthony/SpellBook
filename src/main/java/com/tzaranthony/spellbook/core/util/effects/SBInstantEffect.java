package com.tzaranthony.spellbook.core.util.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class SBInstantEffect  extends MobEffect {
    public SBInstantEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean isInstantenous() {
        return true;
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (affected instanceof ServerPlayer) {
            ((ServerPlayer) affected).resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        }
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity object, @Nullable Entity owner, LivingEntity affected, int amplifier, double p_19466_) {
        this.applyEffectTick(affected, amplifier);
    }
}