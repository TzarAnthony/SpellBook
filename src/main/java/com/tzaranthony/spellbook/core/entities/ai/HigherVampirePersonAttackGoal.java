package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampirePhase1;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;

public class HigherVampirePersonAttackGoal extends MagicAndMeleeAttackGoal {
    protected final HigherVampirePhase1 mob;
    float damage;
    int cloudCooldown;
    int biteCooldown;

    public HigherVampirePersonAttackGoal(HigherVampirePhase1 vampire) {
        super(vampire, (ProjectileSpell) SBSpellRegistry.SCREAM, 1.2D, true, 40, 4);
        this.mob = vampire;
        this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
    }

    @Override
    public void start() {
        this.biteCooldown = 0;
        this.cloudCooldown = 0;
        super.start();
    }

    protected boolean checkRangeCriteria(double distance, double d0) {
        return this.mob.hasEffect(MobEffects.LEVITATION) || (distance > 36.0D && distance <= 1600 && super.checkRangeCriteria(distance, d0));
    }

    @Override
    protected void performMeleeAttack(LivingEntity attacked) {
        if (Math.round(Math.random() * 100) <= 8 && this.cloudCooldown <= 0) {
            this.performCloudAttack();
            this.resetCloudCooldown();
        } else {
            if (attacked instanceof AbstractGolem) {
                this.damage = this.damage * 3;
            }

            if (this.biteCooldown <= 0 && attacked.getMobType() != MobType.UNDEAD) {
                this.resetBiteCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F);
                attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
                this.mob.heal(this.damage * 0.75F);
            } else {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(attacked);
                if (Math.round(Math.random() * 10) % 3 == 0) {
                    attacked.addEffect(new MobEffectInstance(SBEffects.FRACTURED.get(), 100));
                }
            }
        }
    }

    protected void performCloudAttack() {
        this.mob.setPose(Pose.CROUCHING);
        this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 99, false, false));

        if (mob.isCrouching()) {
            this.mob.level.playSound((Player) null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.AMBIENT, 0.5F, 1.0F);

            int color;
            MobEffectInstance effect = null;

            if (Math.round(Math.random() * 100) <= Math.min(60, 15 + (80 * (1 - (mob.getHealth() / mob.getMaxHealth()))))) {
                color = 3484199;
                effect = new MobEffectInstance(MobEffects.WITHER, 200, 1);
            } else {
                color = 5149489;
                effect = new MobEffectInstance(MobEffects.POISON, 200, 1);
            }

            AreaEffectCloud effectCloud = new AreaEffectCloud(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ());
            effectCloud.setOwner(mob);
            effectCloud.setRadius(3.0F);
            effectCloud.setRadiusOnUse(-0.1F);
            effectCloud.setWaitTime(5);
            effectCloud.setRadiusPerTick((5.0F - effectCloud.getRadius()) / (float) effectCloud.getDuration());
            effectCloud.addEffect(effect);
            effectCloud.setFixedColor(color);
            this.mob.level.addFreshEntity(effectCloud);
        }
    }

    @Override
    protected void handleTimers() {
        super.handleTimers();
        this.biteCooldown = Math.max(this.biteCooldown - 1, 0);
        this.cloudCooldown = Math.max(this.cloudCooldown - 1, 0);
    }

    protected void resetBiteCooldown() {
        this.biteCooldown = 60;
    }

    protected void resetCloudCooldown() {
        this.cloudCooldown = 25;
    }
}