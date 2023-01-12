package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.ai.MagicAndMeleeAttackGoal;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;

public class HigherVampirePerson extends HigherVampirePhase1 {
    public HigherVampirePerson(EntityType<? extends HigherVampirePerson> highVamp, Level level) {
        super(highVamp, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new VampireHumanToBatTransformGoal(this));
        this.goalSelector.addGoal(2, new HigherVampirePersonAttackGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBVampireEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.JUMP_STRENGTH, 4.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 90.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(7));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    public void transform() {
        this.playSound(SoundEvents.HUSK_AMBIENT, 1.0F, 1.0F);
        int i = PotionUtils.getColor(Potions.POISON);
        double d0 = (double)(i >> 16 & 255) / 255.0D;
        double d1 = (double)(i >> 8 & 255) / 255.0D;
        double d2 = (double)(i >> 0 & 255) / 255.0D;
        for(int j = 0; j < 8; ++j) {
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
        }
        float health = this.getHealth();
        Collection<MobEffectInstance> effects = this.getActiveEffects();
        HigherVampireBat vampire = this.convertTo(SBEntities.HIGH_VAMP1_BAT.get(), false);
        vampire.setHealth(health);
        if (!effects.isEmpty()) {
            for(MobEffectInstance effect : effects) {
                vampire.addEffect(effect);
            }
        }
        vampire.setVariant(this.getVariant());
        vampire.setTarget(this.getTarget());
    }

    class VampireHumanToBatTransformGoal extends Goal {
        private final HigherVampirePerson mob;
        private int transformerCooldown;

        public VampireHumanToBatTransformGoal(HigherVampirePerson vampireHuman) {
            this.mob = vampireHuman;
            this.transformerCooldown = 100;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP));
            vampireHuman.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return this.mob.hasEffect(MobEffects.LEVITATION) || this.mob.isInWater();
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

    class HigherVampirePersonAttackGoal extends MagicAndMeleeAttackGoal {
        protected final HigherVampirePhase1 mob;
        float damage;
        int cloudCooldown;
        int biteCooldown;
        int freezeCooldown;

        public HigherVampirePersonAttackGoal(HigherVampirePhase1 vampire) {
            super(vampire, (ProjectileSpell) SBSpellRegistry.SCREAM, 1.35D, true, 40, 4);
            this.mob = vampire;
            this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        }

        @Override
        public void start() {
            this.cloudCooldown = 0;
            this.biteCooldown = 0;
            this.freezeCooldown = 0;
            super.start();
        }

        protected boolean checkRangeCriteria(double distance, double d0) {
            return this.mob.hasEffect(MobEffects.LEVITATION) || (distance > 36.0D && distance <= 1600 && super.checkRangeCriteria(distance, d0));
        }

        @Override
        protected void performMeleeAttack(LivingEntity attacked) {
            if (Math.round(Math.random() * 100) <= 8 && this.cloudCooldown <= 0) {
                this.performCloudAttack();
                this.resetCloudCooldown();
            } else {
                if (attacked instanceof AbstractGolem) {
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 2.0F * this.mob.getDayTimeDamageModifier());
                }
                if (this.biteCooldown <= 0 && attacked.getMobType() != MobType.UNDEAD) {
                    this.resetBiteCooldown();
                    BlockPos pos = this.mob.blockPosition().relative(this.mob.getDirection());
                    this.mob.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ());
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F * this.mob.getDayTimeDamageModifier());
                    attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
                    this.mob.heal(this.damage * 0.75F * this.mob.getDayTimeDamageModifier());
                    this.mob.playSound(SoundEvents.PHANTOM_BITE, 1.0F, 1.0F);
                } else if (this.freezeCooldown <= 0) {
                    this.resetFreezeCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    attacked.hurt(SBDamageSource.mobAttack(this.mob), this.damage * this.mob.getDayTimeDamageModifier());
                    attacked.addEffect(new MobEffectInstance(SBEffects.FREEZING.get(), 400, 1));
                    this.mob.playSound(SoundEvents.SNOW_GOLEM_HURT, 1.0F, 1.0F);
                } else {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.swing(InteractionHand.OFF_HAND);
                    this.mob.doHurtTarget(attacked);
                    if (Math.round(Math.random() * 10) % 3 == 0) {
                        attacked.addEffect(new MobEffectInstance(SBEffects.FRACTURED.get(), 100));
                    }
                }
            }
        }

        protected void performCloudAttack() {
            this.mob.setPose(Pose.CROUCHING);
            this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 99, false, false));

            if (mob.isCrouching()) {
                this.mob.playSound(SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, 0.5F, 1.0F);
                int color;
                MobEffectInstance effect;

                if (Math.round(Math.random() * 100) <= Math.min(60, 15 + (80 * (1 - (mob.getHealth() / mob.getMaxHealth()))))) {
                    color = 3484199;
                    effect = new MobEffectInstance(MobEffects.WITHER, 200, 1);
                } else {
                    color = 5149489;
                    effect = new MobEffectInstance(MobEffects.POISON, 200, 1);
                }

                AreaEffectCloud effectCloud = new AreaEffectCloud(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ());
                effectCloud.setOwner(mob);
                effectCloud.setRadius(3.0F);
                effectCloud.setRadiusOnUse(-0.1F);
                effectCloud.setWaitTime(5);
                effectCloud.setRadiusPerTick((5.0F - effectCloud.getRadius()) / (float) effectCloud.getDuration());
                effectCloud.addEffect(effect);
                effectCloud.setFixedColor(color);
                this.mob.level.addFreshEntity(effectCloud);
            }
        }

        @Override
        protected void handleTimers() {
            super.handleTimers();
            this.biteCooldown = Math.max(this.biteCooldown - 1, 0);
            this.freezeCooldown = Math.max(this.freezeCooldown - 1, 0);
            this.cloudCooldown = Math.max(this.cloudCooldown - 1, 0);
        }

        protected void resetBiteCooldown() {
            this.biteCooldown = 60;
        }

        protected void resetFreezeCooldown() {
            this.freezeCooldown = 90;
        }

        protected void resetCloudCooldown() {
            this.cloudCooldown = 25;
        }
    }
}