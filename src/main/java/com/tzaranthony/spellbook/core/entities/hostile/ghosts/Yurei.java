package com.tzaranthony.spellbook.core.entities.hostile.ghosts;

import com.tzaranthony.spellbook.core.entities.ai.MoveToEntityGoal;
import com.tzaranthony.spellbook.core.entities.ai.NecromancedEntity;
import com.tzaranthony.spellbook.core.entities.other.CursedPainting;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class Yurei extends SBGhostEntity {
    public Yurei(EntityType<? extends SBGhostEntity> shade, Level level) {
        super(shade, level);
        this.xpReward = 8;
        this.maxUpStep = 5;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new MoveToEntityGoal(this, CursedPainting.class, 1.0D));
        this.goalSelector.addGoal(4, new PossesGoal(1.25D, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBGhostEntity.class));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 60.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !(source.getEntity() instanceof AbstractSkeleton || source.getEntity() instanceof NecromancedEntity || source.isCreativePlayer() || source == DamageSource.OUT_OF_WORLD || source == DamageSource.CRAMMING);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(4));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    class PossesGoal extends MeleeAttackGoal {
        public PossesGoal(double boost, boolean whenNotSeen) {
            super(Yurei.this, boost, whenNotSeen);
        }

        protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
            double d0 = this.getAttackReachSqr(attacked);
            if (distance <= d0 && getTicksUntilNextAttack() <= 0) {
                if (attacked instanceof AbstractSkeleton || attacked instanceof NecromancedEntity) {
                    attacked.hurt(DamageSource.OUT_OF_WORLD, 1000.0F);
                    this.mob.hurt(DamageSource.OUT_OF_WORLD, 1000.0F);
                } else {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(attacked);
                }
            }
        }
    }
}