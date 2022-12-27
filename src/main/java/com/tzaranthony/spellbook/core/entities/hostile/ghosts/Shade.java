package com.tzaranthony.spellbook.core.entities.hostile.ghosts;

import com.tzaranthony.spellbook.core.entities.other.CursedPainting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class Shade extends SBGhostEntity {
    public Shade(EntityType<? extends Shade> shade, Level level) {
        super(shade, level);
        this.xpReward = 8;
        this.maxUpStep = 15.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RestrictSunGoal(this));
        this.goalSelector.addGoal(2, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new PossesGoal(1.25D, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ShadeTargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, CursedPainting.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.isFire() || source.isProjectile();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(2));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    class PossesGoal extends MeleeAttackGoal {
        public PossesGoal(double boost, boolean whenNotSeen) {
            super(Shade.this, boost, whenNotSeen);
        }

        protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
            double d0 = this.getAttackReachSqr(attacked);
            if (distance <= d0) {
                this.mob.swing(InteractionHand.MAIN_HAND);

                if (attacked.level.getDifficulty() == Difficulty.HARD) {
                    attacked.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(attacked.getAttributeValue(Attributes.MAX_HEALTH) - 6.0D, 1.0D));
                } else {
                    attacked.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(attacked.getAttributeValue(Attributes.MAX_HEALTH) - 4.0D, 1.0D));
                }
                if (attacked.getHealth() > attacked.getMaxHealth()) {
                    attacked.setHealth(attacked.getMaxHealth());
                }
                this.mob.hurt(DamageSource.OUT_OF_WORLD, 100.0F);
            }
        }
    }

    static class ShadeTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public ShadeTargetGoal(Shade shade, Class<T> target) {
            super(shade, target, true);
        }

        public boolean canUse() {
            float f = this.mob.getBrightness();
            return f >= 0.5F ? false : super.canUse();
        }
    }
}