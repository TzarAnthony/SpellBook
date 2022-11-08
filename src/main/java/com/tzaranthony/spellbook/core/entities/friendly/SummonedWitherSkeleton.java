package com.tzaranthony.spellbook.core.entities.friendly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;

public class SummonedWitherSkeleton extends SBAbstractFriendlySkeleton {
    public SummonedWitherSkeleton(EntityType<? extends SummonedWitherSkeleton> sfWitherSkelly, Level level) {
        super(sfWitherSkelly, level);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractPiglin.class, true));
        super.registerGoals();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }

    protected void populateDefaultEquipmentEnchantments(DifficultyInstance p_180483_1_) {
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag nbt) {
        SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, entityData, nbt);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.reassessWeaponGoal();
        return data;
    }

    protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
        return 2.1F;
    }

    public boolean doHurtTarget(Entity p_70652_1_) {
        if (!super.doHurtTarget(p_70652_1_)) {
            return false;
        } else {
            if (p_70652_1_ instanceof LivingEntity) {
                ((LivingEntity) p_70652_1_).addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
            }

            return true;
        }
    }

    protected AbstractArrow getArrow(ItemStack p_213624_1_, float p_213624_2_) {
        AbstractArrow abstractarrowentity = super.getArrow(p_213624_1_, p_213624_2_);
        abstractarrowentity.setSecondsOnFire(100);
        return abstractarrowentity;
    }

    public boolean canBeAffected(MobEffectInstance p_70687_1_) {
        return p_70687_1_.getEffect() == MobEffects.WITHER ? false : super.canBeAffected(p_70687_1_);
    }
}