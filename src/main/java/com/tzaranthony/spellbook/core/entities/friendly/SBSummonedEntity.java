package com.tzaranthony.spellbook.core.entities.friendly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SBSummonedEntity extends TamableAnimal {
    public boolean hasLimitedLife;
    public int limitedLifeTicks;

    public SBSummonedEntity(EntityType<? extends TamableAnimal> entity, Level level) {
        super(entity, level);
        this.setTame(false);
        this.limitedLifeTicks = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 25.0F, 5.0F, false));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, SBSummonedEntity.class, Wolf.class)).setAlertOthers());
    }

    public void tick() {
        super.tick();
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.hurt(DamageSource.CRAMMING, 1.0F);
        }
    }

    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (p_70037_1_.contains("LifeTicks")) {
            this.setLimitedLife(p_70037_1_.getInt("LifeTicks"));
        }
    }

    public void addAdditionalSaveData(CompoundTag p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        if (this.hasLimitedLife) {
            p_213281_1_.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
            if (target instanceof Wolf) {
                Wolf wolfentity = (Wolf) target;
                return !wolfentity.isTame() || wolfentity.getOwner() != owner;
            } else if (target instanceof SBSummonedEntity) {
                SBSummonedEntity summoned = (SBSummonedEntity) target;
                return !summoned.isTame() || summoned.getOwner() != owner;
            } else if (target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target)) {
                return false;
            } else if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed()) {
                return false;
            } else {
                return !(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame();
            }
        } else {
            return false;
        }
    }

    public boolean isVulnerableTo(DamageSource source) {
        return false;
    }

    public boolean isResistantTo(DamageSource source) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isVulnerableTo(source)) {
            amount = amount * 2.0F;
        }
        if (this.isResistantTo(source)) {
            amount = amount * 0.75F;
        }
        return super.hurt(source, amount);
    }

    public void setLimitedLife(int p_190653_1_) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = p_190653_1_;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return null;
    }
}