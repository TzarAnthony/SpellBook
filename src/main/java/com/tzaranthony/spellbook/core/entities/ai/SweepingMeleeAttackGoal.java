package com.tzaranthony.spellbook.core.entities.ai;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;

public class SweepingMeleeAttackGoal extends MeleeAttackGoal {
    public SweepingMeleeAttackGoal(PathfinderMob mob, double speedMod, boolean needsToSee) {
        super(mob, speedMod, needsToSee);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distance) {
        double d0 = this.getAttackReachSqr(target);
        if (distance <= d0 && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);

            if (this.mob.getRandom().nextInt(10) <= 5) {
                performSweepAttack(target);
            }
            this.mob.doHurtTarget(target);
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity target) {
        return 5.0D * super.getAttackReachSqr(target);
    }

    protected void performSweepAttack(LivingEntity target) {
        float sweepDmg = 1.0F + (float)(EnchantmentHelper.getSweepingDamageRatio(this.mob) * this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE));
        for(LivingEntity pTgt : this.mob.level.getEntitiesOfClass(LivingEntity.class, getSweepHitBox(target))) {
            if (pTgt != this.mob && pTgt != target && !this.mob.isAlliedTo(pTgt) && (!(pTgt instanceof ArmorStand) || !((ArmorStand) pTgt).isMarker()) && this.mob.distanceToSqr(pTgt) < 9.0D) {
                pTgt.knockback(0.4F, Mth.sin(this.mob.getYRot() * ((float) Math.PI / 180.0F)), -Mth.cos(this.mob.getYRot() * ((float) Math.PI / 180.0F)));
                pTgt.hurt(DamageSource.mobAttack(this.mob), sweepDmg);
            }
        }
        this.mob.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
        sweepAttack();
    }

    protected AABB getSweepHitBox(LivingEntity target) {
        return target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D);
    }

    public void sweepAttack() {
        double d0 = -Mth.sin(this.mob.getYRot() * ((float) Math.PI / 180.0F));
        double d1 = Mth.cos(this.mob.getYRot() * ((float) Math.PI / 180.0F));
        if (this.mob.level instanceof ServerLevel sLevel) {
            sLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.mob.getX() + d0, this.mob.getY(0.5D), this.mob.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
        }
    }
}