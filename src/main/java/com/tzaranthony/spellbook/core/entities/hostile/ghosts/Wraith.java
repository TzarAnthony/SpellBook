package com.tzaranthony.spellbook.core.entities.hostile.ghosts;

import com.tzaranthony.spellbook.core.entities.ai.*;
import com.tzaranthony.spellbook.core.entities.other.CursedPainting;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class Wraith extends SBGhostEntity implements FlyingEntity {
    public Wraith(EntityType<? extends SBGhostEntity> shade, Level level) {
        super(shade, level);
        this.moveControl = new VexLikeMovementHelper(this);
        this.maxUpStep = 15.0F;
    }

    @Override
    public void move(MoverType moverType, Vec3 vec3) {
        super.move(moverType, vec3);
        this.checkInsideBlocks();
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MoveToEntityGoal(this, CursedPainting.class, 1.0D));
        this.goalSelector.addGoal(1, new FlyingMeleeAndMagicAttackGoal(this, (ProjectileSpell) SBSpellRegistry.IGNITE, 6));
        this.goalSelector.addGoal(5, new FlyingGhostMoveRandomGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
//        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, CursedPainting.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.STRAY_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_HORSE_DEATH;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(4));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    @Override
    public void playFlyingAttackSound() {
        this.playSound(SoundEvents.BLAZE_BURN, 1.0F, 1.0F);
    }
}