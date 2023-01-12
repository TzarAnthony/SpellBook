package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SelfImprovementSpell extends Spell{
    public MobEffect effect;
    public Supplier<MobEffect> effect1;
    public int duration;
    public int amplifier;

    public SelfImprovementSpell(int id, String name, SpellTier tier, @Nullable MobEffect effect, @Nullable Supplier<MobEffect> effect1, int duration, int amplifier) {
        super(id, name, tier);
        this.effect = effect;
        this.effect1 = effect1;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public boolean perform_spell(Level level, LivingEntity entity, InteractionHand hand, BlockPos pos) {
        if (this.effect != null) {
            entity.addEffect(new MobEffectInstance(this.effect, this.duration, this.amplifier));
            playCustomSound(entity);
            return true;
        } else if (this.effect1 != null) {
            entity.addEffect(new MobEffectInstance(this.effect1.get(), this.duration, this.amplifier));
            playCustomSound(entity);
            return true;
        }
        return false;
    }
}