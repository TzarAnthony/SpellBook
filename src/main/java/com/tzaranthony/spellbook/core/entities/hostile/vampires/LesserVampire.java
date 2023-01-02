package com.tzaranthony.spellbook.core.entities.hostile.vampires;

import com.tzaranthony.spellbook.core.entities.ai.NearestNightTimeTargetGoal;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class LesserVampire extends SBVampireEntity {
    public LesserVampire(EntityType<? extends LesserVampire> highVamp, Level level) {
        super(highVamp, level);
        this.xpReward = 50;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RestrictSunGoal(this));
        this.goalSelector.addGoal(2, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new VampireMeleeAttack(1.05D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestNightTimeTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.JUMP_STRENGTH, 2.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
    }

    @Override
    public float getVoicePitch() {
        switch (this.getVariant()) {
            case 1:
            case 2:
                return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
            default:
                return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.6F;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(4));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    class VampireMeleeAttack extends MeleeAttackGoal {
        protected final SBVampireEntity mob;
        float damage;
        int attackCounter;

        public VampireMeleeAttack(double boost, boolean mustSee) {
            super(LesserVampire.this, boost, mustSee);
            this.mob = LesserVampire.this;
            this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        }

        @Override
        public void start() {
            this.attackCounter = 0;
            super.start();
        }

        protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
            double d0 = this.getAttackReachSqr(attacked);
            if (distance <= d0 && this.getTicksUntilNextAttack() <= 0) {
                if (attacked instanceof AbstractGolem) {
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 2.0F);
                }
                if (this.attackCounter <= 0 && attacked.getMobType() != MobType.UNDEAD) {
                    this.resetAttackCounter();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F);
                    attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 250));
                    this.mob.heal(this.damage * 0.75F);
                    this.mob.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
                } else {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(attacked);
                    if (Math.round(Math.random() * 10) % 3 == 0) {
                        attacked.addEffect(new MobEffectInstance(SBEffects.FRACTURED.get(), 30));
                    }
                }
            }
            if (this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();
                this.attackCounter = Math.max(this.attackCounter - 1, 0);
            }
        }

        protected void resetAttackCounter() {
            this.attackCounter = 60;
        }
    }
}