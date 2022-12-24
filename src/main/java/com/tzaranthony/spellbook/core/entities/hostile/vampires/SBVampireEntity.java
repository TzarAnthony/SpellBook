package com.tzaranthony.spellbook.core.entities.hostile.vampires;

import com.tzaranthony.spellbook.core.entities.hostile.SBMonsterEntity;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

public class SBVampireEntity extends SBMonsterEntity implements Enemy {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SBVampireEntity.class, EntityDataSerializers.INT);

    public SBVampireEntity(EntityType<? extends SBVampireEntity> entityType, Level level) {
        super(entityType, level);
    }

    // damage settings
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

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.BLOCK;
    }

    public void makeStuckInBlock(BlockState blockState, Vec3 vector) {
        if (!blockState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(blockState, vector);
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

    // spawning
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setVariant(nbt.getInt("Variant"));
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("Variant", this.getVariant());
        super.addAdditionalSaveData(nbt);
    }

    public float getVoicePitch() {
        switch (this.getVariant()) {
            case 1:
            case 2:
                return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
            default:
                return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.6F;
        }
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
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

    public void setVariant(int variant) {
        this.getEntityData().set(VARIANT, variant);
    }

    public int getVariant() {
        return this.getEntityData().get(VARIANT).intValue();
    }

    @Override
    public boolean isPreventingPlayerRest(Player player) {
        if (this.getTarget() instanceof Player) {
            return ((Player) this.getTarget()) == player;
        } else {
            return false;
        }
    }

    // attacks
    class VampireMeleeAttack extends MeleeAttackGoal {
        protected final SBVampireEntity mob;
        float damage;
        int rank;
        int attackCounter;

        public VampireMeleeAttack(double boost, boolean whenNotSeen, int rank) {
            super(SBVampireEntity.this, boost, whenNotSeen);
            this.rank = rank;
            this.mob = SBVampireEntity.this;
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
                    this.damage = this.damage * 3;
                }

                if (this.attackCounter <= 0) {
                    this.resetAttackCounter();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F);
                    attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 250 * (1 + this.rank), this.rank));
                } else {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(attacked);
                    if (Math.round(Math.random() * 10) % 3 == 0) {
                        attacked.addEffect(new MobEffectInstance(SBEffects.FRACTURED.get(), 30 * (1 + this.rank)));
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