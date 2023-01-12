package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.ai.FlyingEntity;
import com.tzaranthony.spellbook.core.entities.ai.FlyingMoveRandomGoal;
import com.tzaranthony.spellbook.core.entities.ai.MagicAndMeleeAttackGoal;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;

public class HigherVampireBat extends HigherVampirePhase1 implements FlyingEntity {
    public static final int TICKS_PER_FLAP = Mth.ceil(2.4166098F);

    public HigherVampireBat(EntityType<? extends HigherVampireBat> highVamp, Level level) {
        super(highVamp, level);
        this.xpReward = 50;
        this.maxUpStep = 1.5F;
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    protected PathNavigation createNavigation(Level p_186262_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_186262_);
        flyingpathnavigation.setCanOpenDoors(true);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new VampireBatToHumanTransformGoal(this));
        this.goalSelector.addGoal(0, new HigherVampireBatAttackGoal(this));
        this.goalSelector.addGoal(4, new FlyingMoveRandomGoal(this));
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
                .add(Attributes.ATTACK_DAMAGE, 8.0F)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FLYING_SPEED, 0.8D)
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

    class VampireBatToHumanTransformGoal extends Goal {
        private final HigherVampireBat mob;
        private int transformerCooldown;

        public VampireBatToHumanTransformGoal(HigherVampireBat vampireBat) {
            this.mob = vampireBat;
            this.transformerCooldown = 400;
            this.setFlags(EnumSet.of(Flag.JUMP));
            vampireBat.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return !isAboveWater() && !this.mob.hasEffect(MobEffects.LEVITATION) && !this.mob.isInWater();
        }

        public boolean isAboveWater() {
            for (int y = 0; y <= 5; ++y) {
                if (this.mob.level.getBlockState(this.mob.blockPosition().below(y)).getBlock() == Blocks.WATER) {
                    return true;
                }
            }
            return false;
        }

        public void resetTransformerCooldown() {
            this.transformerCooldown = 400;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.transformerCooldown <= 0) {
                this.resetTransformerCooldown();
                this.mob.transform();
            }
            --this.transformerCooldown;
        }
    }

    class HigherVampireBatAttackGoal extends MagicAndMeleeAttackGoal {
        protected final HigherVampirePhase1 mob;
        float damage;

        public HigherVampireBatAttackGoal(HigherVampirePhase1 vampire) {
            super(vampire, (ProjectileSpell) SBSpellRegistry.SCREAM, 1.35D, true, 40, 4);
            this.mob = vampire;
            this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        }

        protected boolean checkRangeCriteria(double distance, double d0) {
            return distance > 36.0D && distance <= 1600 && super.checkRangeCriteria(distance, d0);
        }

        @Override
        protected void performMeleeAttack(LivingEntity attacked) {
            if (attacked instanceof AbstractGolem) {
                attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 2.0F * this.mob.getDayTimeDamageModifier());
            }
            if (attacked.getMobType() != MobType.UNDEAD) {
                attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F * this.mob.getDayTimeDamageModifier());
                attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
                this.mob.heal(this.damage * 0.75F * this.mob.getDayTimeDamageModifier());
                this.mob.playSound(SoundEvents.PHANTOM_BITE, 1.0F, 1.0F);
            } else {
                this.mob.doHurtTarget(attacked);
            }
        }
    }
}