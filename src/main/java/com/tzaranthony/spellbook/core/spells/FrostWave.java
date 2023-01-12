package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FrostWave extends ProjectileSpell {
    public FrostWave(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, LivingEntity entity, InteractionHand hand, BlockPos blockPos) {
        boolean x = super.perform_spell(level, entity, hand, blockPos);
        if (x) {
            boolean y = super.perform_spell(level, entity, hand, blockPos, -12.0F);
            boolean z = super.perform_spell(level, entity, hand, blockPos, 12.0F);
            return x || y || z;
        }
        return x;
    }

    @Override
    public EntityType<MagicProjectile> getMagicProjectile() {
        return SBEntities.FROST_WAVE_PROJECTILE.get();
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.SNOWFLAKE);
    }

    @Override
    public boolean perform_spell(Entity user, Entity target) {
        if (user instanceof LivingEntity && target instanceof LivingEntity) {
            ((LivingEntity) target).hurt(DamageSource.indirectMagic(user, user), 4.0F);
            ((LivingEntity) target).addEffect(new MobEffectInstance(SBEffects.FREEZING.get(), 200, 0));
            return true;
        }
        return false;
    }

    @Override
    public void playCustomSound(Entity user) {
        user.playSound(SoundEvents.POWDER_SNOW_FALL, 10.0F, 1.0F);
    }
}