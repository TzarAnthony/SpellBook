package com.tzaranthony.spellbook.core.entities.hostile.ghosts;

import com.tzaranthony.spellbook.core.entities.hostile.SBMonsterEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;

public class SBGhostEntity extends SBMonsterEntity {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SBGhostEntity.class, EntityDataSerializers.INT);

    public SBGhostEntity(EntityType<? extends SBGhostEntity> type, Level level) {
        super(type, level);
        this.xpReward = 8;
        this.maxUpStep = 2.0F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source == DamageSource.FALL || source == DamageSource.IN_WALL
                || source == DamageSource.CACTUS || source == DamageSource.LIGHTNING_BOLT || source == DamageSource.DROWN
                || source == DamageSource.STARVE || source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL
                || source == DamageSource.SWEET_BERRY_BUSH;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.ELDER_GUARDIAN_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ELDER_GUARDIAN_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.ELDER_GUARDIAN_DEATH;
    }

    // spawning
    public void setVariant(int variant) {
        this.getEntityData().set(VARIANT, variant);
    }

    public int getVariant() {
        return this.getEntityData().get(VARIANT).intValue();
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(nbt.getInt("Variant"));
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("Variant", this.getVariant());
        super.addAdditionalSaveData(nbt);
    }
}