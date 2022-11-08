package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampirePhase1;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
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
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;

public class HigherVampirePersonAttackGoal extends MeleeAttackGoal {
    protected final HigherVampirePhase1 mob;
    float damage;
    int cloudCooldown;
    int sonicCooldown;
    int biteCooldown;
    int animationTimer;

    public HigherVampirePersonAttackGoal(HigherVampirePhase1 vampire, double boost, boolean whenNotSeen) {
        super(vampire, boost, whenNotSeen);
        this.mob = vampire;
        this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
    }

    @Override
    public void start() {
        this.biteCooldown = 0;
        this.sonicCooldown = 0;
        this.cloudCooldown = 0;
        this.animationTimer = 1;
        super.start();
    }

    protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
        double d0 = this.getAttackReachSqr(attacked);
        if (this.getTicksUntilNextAttack() <= 0) {
            if (distance <= d0 && !this.mob.isCrouching()) {
                if (Math.round(Math.random() * 100) <= 8 && this.cloudCooldown <= 0) {
                    this.performCloudAttack();
                    this.resetCloudCooldown();
                } else {
                    this.performMeleeAttack(attacked);
                }
            } else if (this.mob.hasEffect(MobEffects.LEVITATION) || (distance > 36.0D && distance <= 1600 && this.sonicCooldown <= 0)) {
                this.performSonicAttack(attacked);
                this.resetSonicCooldown();
            }
            // reduce second timers
            this.resetAttackCooldown();
            this.biteCooldown = Math.max(this.biteCooldown - 1, 0);
            this.sonicCooldown = Math.max(this.sonicCooldown - 1, 0);
            this.cloudCooldown = Math.max(this.cloudCooldown - 1, 0);
            if (mob.isCrouching()) {
                this.animationTimer = Math.max(this.animationTimer - 1, 0);
                if (this.animationTimer <= 0) {
                    mob.setPose(Pose.STANDING);
                    this.resetAnimationTimer();
                }
            }
        }
    }

    protected void performMeleeAttack(LivingEntity attacked) {
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

    protected void performSonicAttack(LivingEntity attacked) {
        this.mob.getLookControl().setLookAt(attacked.position());

        MagicProjectile magic = new MagicProjectile(SBEntities.LARGE_MAGIC_PROJECTILE.get(), this.mob.level);
        magic.setOwner(this.mob);
        magic.setPos(this.mob.getX(), this.mob.getEyeY() - (double)0.15F, this.mob.getZ());
        magic.setSpell(SBSpellRegistry.SCREAM.getId());
        magic.setParticle(SBParticleTypes.SCREAM.get());

        double d0 = attacked.getX() - this.mob.getX();
        double d1 = attacked.getEyeY() - magic.getY();
        double d2 = attacked.getZ() - this.mob.getZ();
        magic.shoot(d0, d1, d2, 1.6F, (float) (4 - this.mob.level.getDifficulty().getId()));
        mob.level.addFreshEntity(magic);
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

    protected void resetBiteCooldown() {
        this.biteCooldown = 60;
    }

    protected void resetSonicCooldown() {
        this.sonicCooldown = 40;
    }

    protected void resetCloudCooldown() {
        this.cloudCooldown = 25;
    }

    protected void resetAnimationTimer() {
        this.animationTimer = 3;
    }
}