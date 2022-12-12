package com.tzaranthony.spellbook.core.entities.neutral;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nullable;

public class GhostHorse extends AbstractHorse {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(GhostHorse.class, EntityDataSerializers.INT);

    public GhostHorse(EntityType<? extends GhostHorse> entity, Level level) {
        super(entity, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F);
    }

    protected void randomizeAttributes() {
        this.getAttribute(Attributes.JUMP_STRENGTH)
                .setBaseValue(this.generateRandomJumpStrength());
    }

    protected void addBehaviourGoals() {
    }

    public boolean canStandOnFluid(FluidState state) {
        return state.is(FluidTags.LAVA) || state.is(FluidTags.WATER);
    }

    @Override
    public boolean causeFallDamage(float p_149499_, float p_149500_, DamageSource p_149501_) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        super.getHurtSound(source);
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        BlockState blockstate = this.level.getBlockState(pos.above());
        SoundType soundtype = state.getSoundType(level, pos, this);
        if (blockstate.is(Blocks.SNOW)) {
            soundtype = blockstate.getSoundType(level, pos, this);
        }

        if (this.isVehicle() && this.canGallop) {
            ++this.gallopSoundCounter;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                if (!state.getMaterial().isLiquid()) {
                    this.playSound(SoundEvents.HORSE_GALLOP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                } else {
                    this.playSound(getLiquidStep(state, true), soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
                this.playGallopSound(soundtype);
            } else if (this.gallopSoundCounter <= 5) {
                if (!state.getMaterial().isLiquid()) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                } else {
                    this.playSound(getLiquidStep(state, false), soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            }
        } else if (soundtype == SoundType.WOOD) {
            this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        } else if (!state.getMaterial().isLiquid()) {
            this.playSound(getLiquidStep(state, false), soundtype.getVolume() * 0.15F, soundtype.getPitch());
        } else {
            this.playSound(SoundEvents.HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    protected SoundEvent getLiquidStep(BlockState state, boolean galloping) {
        if (state.is(Blocks.LAVA)) {
            return SoundEvents.STRIDER_STEP_LAVA;
        } else if (galloping) {
            return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
        }
        return SoundEvents.SKELETON_HORSE_STEP_WATER;
    }

    protected SoundEvent getSwimSound() {
        if (this.onGround) {
            if (!this.isVehicle()) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }

            ++this.gallopSoundCounter;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
            }

            if (this.gallopSoundCounter <= 5) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
        }
        return SoundEvents.SKELETON_HORSE_SWIM;
    }

    protected void playSwimSound(float p_30911_) {
        if (this.onGround) {
            super.playSwimSound(0.3F);
        } else {
            super.playSwimSound(Math.min(0.1F, p_30911_ * 25.0F));
        }
    }

    protected void playJumpSound() {
        if (this.level.getBlockState(this.getOnPos()).getMaterial().isLiquid()) {
            this.playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
        } else {
            super.playJumpSound();
        }
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.1875D;
    }

    public boolean rideableUnderWater() {
        return true;
    }

    protected float getWaterSlowDown() {
        return 0.96F;
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!this.isTamed()) {
            return InteractionResult.PASS;
        } else if (this.isBaby()) {
            return super.mobInteract(player, hand);
        } else if (player.isSecondaryUseActive()) {
            this.openInventory(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (this.isVehicle()) {
            return super.mobInteract(player, hand);
        } else {
            if (!itemstack.isEmpty()) {
                if (itemstack.is(Items.SADDLE) && !this.isSaddled()) {
                    this.openInventory(player);
                    return InteractionResult.sidedSuccess(this.level.isClientSide);
                }

                InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
                if (interactionresult.consumesAction()) {
                    return interactionresult;
                }
            }

            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
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

    public boolean isOnFire() {
        return false;
    }

    // Spawning
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public boolean isTamed() {
        return true;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(3));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }
}