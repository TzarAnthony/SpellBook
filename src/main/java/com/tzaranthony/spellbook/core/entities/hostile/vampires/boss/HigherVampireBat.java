package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.ai.*;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collection;

public class HigherVampireBat extends HigherVampirePhase1 implements FlyingEntity {
    public static final int TICKS_PER_FLAP = Mth.ceil(2.4166098F);

    public HigherVampireBat(EntityType<? extends HigherVampireBat> highVamp, Level level) {
        super(highVamp, level);
        this.xpReward = 50;
        this.maxUpStep = 1.5F;
        this.moveControl = new VexLikeMovementHelper(this);
    }

    @Override
    public void move(MoverType moverType, Vec3 vec3) {
        super.move(moverType, vec3);
        this.checkInsideBlocks();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new VampireBatToHumanTransformGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new HigherVampireBatAttackGoal(this));
        this.goalSelector.addGoal(4, new FlyingGhostMoveRandomGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBVampireEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.JUMP_STRENGTH, 4.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0F)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 90.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        boolean flag = target.hurt(SBDamageSource.bite(this), damage * 0.75F);
        if (flag) {
            this.heal(damage * 0.75F);

            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
                if (knockback > 0.0F) {
                    ((LivingEntity) target).knockback((double)(knockback * 0.5F), (double)Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
            this.setLastHurtMob(target);
        }
        return flag;
    }

    public boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    protected float getSoundVolume() {
        return 0.1F;
    }

    public float getVoicePitch() {
        return super.getVoicePitch() * 0.95F;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BAT_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(Entity entity) {
    }

    protected void pushEntities() {
    }

    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    public boolean causeFallDamage(float p_148702_, float p_148703_, DamageSource p_148704_) {
        return false;
    }

    protected void checkFallDamage(double p_27419_, boolean p_27420_, BlockState p_27421_, BlockPos p_27422_) {
    }

    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SBVampireEntity.VARIANT, 0);
    }

    @Override
    public void playFlyingAttackSound() {
        this.playSound(SoundEvents.BAT_LOOP, 1.0F, 1.0F);
    }

    public void transform() {
        this.playSound(this.getConversionSound(), 1.0F, 1.0F);
        int i = PotionUtils.getColor(Potions.POISON);
        double d0 = (double)(i >> 16 & 255) / 255.0D;
        double d1 = (double)(i >> 8 & 255) / 255.0D;
        double d2 = (double)(i >> 0 & 255) / 255.0D;
        for(int j = 0; j < 8; ++j) {
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
        }
        float health = this.getHealth();
        Collection<MobEffectInstance> effects = this.getActiveEffects();
        HigherVampirePerson vampire = this.convertTo(SBEntities.HIGH_VAMP1.get(), false);
        vampire.setHealth(health);
        if (!effects.isEmpty()) {
            for(MobEffectInstance effect : effects) {
                vampire.addEffect(effect);
            }
        }
        vampire.setVariant(this.getVariant());
        vampire.setTarget(this.getTarget());
    }

    protected float getStandingEyeHeight(Pose p_27440_, EntityDimensions p_27441_) {
        return p_27441_.height / 2.0F;
    }

    class HigherVampireBatAttackGoal extends FlyingMeleeAndMagicAttackGoal {
        float damage;

        public HigherVampireBatAttackGoal(HigherVampirePhase1 vampire) {
            super(vampire, (ProjectileSpell) SBSpellRegistry.SCREAM);
            this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
            if (this.ticksUntilNextAttack <= 0) {
                if (distance > 9.0D && this.magicCooldown <= 0) {
                    this.performMagicAttack(attacked);
                    this.resetMagicCooldown();
                } else if (this.mob.getBoundingBox().intersects(attacked.getBoundingBox())) {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    attacked.hurt(SBDamageSource.bite((HigherVampireBat) this.mob), this.damage * 0.75F);
                    if (attacked.getMobType() != MobType.UNDEAD) {
                        attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
                        this.mob.heal(this.damage * 0.75F);
                    }
                } else {
                    if (distance < 9.0D) {
                        Vec3 vec3 = attacked.getEyePosition();
                        this.mob.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                    }
                }
                // reduce second timers
                this.resetAttackCooldown();
                this.magicCooldown = Math.max(this.magicCooldown - 1, 0);
            }
        }
    }
}