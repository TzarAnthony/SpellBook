package com.tzaranthony.spellbook.core.entities.hostile.vampires;

import com.tzaranthony.spellbook.core.entities.hostile.SBMonsterEntity;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;

public class SBVampireEntity extends SBMonsterEntity implements Enemy {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SBVampireEntity.class, EntityDataSerializers.INT);

    public SBVampireEntity(EntityType<? extends SBVampireEntity> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0F;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source)
                || source == DamageSource.FALL || source == DamageSource.IN_WALL || source == DamageSource.DROWN
                || source == DamageSource.FALLING_STALACTITE || source == DamageSource.STALAGMITE
                || source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL;
    }

    public boolean isVulnerableTo(DamageSource source) {
        return source.isFire() || source == SBDamageSource.BLEED;
    }

    public boolean isResistantTo(DamageSource source) {
        return false;
    }

    public boolean vulnerableToItems(Item checkedItem) {
        return (checkedItem == Items.STICK
                || checkedItem == Items.WOODEN_AXE
                || checkedItem == Items.WOODEN_HOE
                || checkedItem == Items.WOODEN_PICKAXE
                || checkedItem == Items.WOODEN_SHOVEL
                || checkedItem == Items.WOODEN_SWORD
        );
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
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    public SoundEvent getConversionSound() {
        switch (this.getVariant()) {
            case 1:
            case 2:
                return SoundEvents.EVOKER_CELEBRATE;
            default:
                return SoundEvents.WITCH_CELEBRATE;
        }
    }

    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.getEffect() == MobEffects.POISON) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, effect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        if (effect.getEffect() == MobEffects.WITHER) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, effect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(effect);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity) {
            if (vulnerableToItems(((LivingEntity) source.getEntity()).getMainHandItem().getItem())) {
                amount = amount * 4.0F;
            }
        }
        return super.hurt(source, amount);
    }

    // spawning
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SBVampireEntity.VARIANT, 0);
    }

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