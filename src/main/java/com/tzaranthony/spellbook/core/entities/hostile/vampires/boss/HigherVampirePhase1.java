package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class HigherVampirePhase1 extends HigherVampire {
    public HigherVampirePhase1(EntityType<? extends HigherVampirePhase1> highVamp, Level level) {
        super(highVamp, level);
    }

    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if (this.isDeadOrDying()) {
            double x = (this.random.nextFloat() - 0.5F) * 3.0F;
            double y = (this.random.nextFloat() - 0.5F) * 4.0F;
            double z = (this.random.nextFloat() - 0.5F) * 3.0F;
            this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + x, this.getY() + 2.0D + y, this.getZ() + z, 0.0D, 0.0D, 0.0D);
            this.playSound(SoundEvents.ENDER_DRAGON_DEATH, 0.5F, 1.0F);
            HigherVampirePhase2 phase2 = this.convertTo(SBEntities.HIGH_VAMP2.get(), false);
            phase2.makeInvulnerable();
            phase2.setVariant(this.getVariant());
            phase2.setTarget(this.getTarget());
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.getDirectEntity() instanceof AbstractArrow) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }
}