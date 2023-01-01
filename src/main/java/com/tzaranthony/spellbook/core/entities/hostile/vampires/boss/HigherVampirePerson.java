package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.ai.MagicAndMeleeAttackGoal;
import com.tzaranthony.spellbook.core.entities.ai.VampireHumanToBatTransformGoal;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Collection;

public class HigherVampirePerson extends HigherVampirePhase1 {
    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public HigherVampirePerson(EntityType<? extends HigherVampirePerson> highVamp, Level level) {
        super(highVamp, level);
        this.xpReward = 50;
        this.maxUpStep = 1.5F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new VampireHumanToBatTransformGoal(this));
        this.goalSelector.addGoal(2, new HigherVampirePersonAttackGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBVampireEntity.class).setAlertOthers());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.JUMP_STRENGTH, 4.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 90.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SBVampireEntity.VARIANT, 0);
    }

    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Component tag) {
        super.setCustomName(tag);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(7));
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    public void transform() {
        this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.HUSK_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F);
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

    class HigherVampirePersonAttackGoal extends MagicAndMeleeAttackGoal {
        protected final HigherVampirePhase1 mob;
        float damage;
        int cloudCooldown;
        int biteCooldown;

        public HigherVampirePersonAttackGoal(HigherVampirePhase1 vampire) {
            super(vampire, (ProjectileSpell) SBSpellRegistry.SCREAM, 1.2D, true, 40, 4);
            this.mob = vampire;
            this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        }

        @Override
        public void start() {
            this.biteCooldown = 0;
            this.cloudCooldown = 0;
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
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 2.0F);
                }
                if (this.biteCooldown <= 0 && attacked.getMobType() != MobType.UNDEAD) {
                    this.resetBiteCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    attacked.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F);
                    attacked.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
                    this.mob.heal(this.damage * 0.75F);
                } else {
                    this.mob.swing(InteractionHand.MAIN_HAND);
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
                this.mob.level.playSound((Player) null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.AMBIENT, 0.5F, 1.0F);

                int color;
                MobEffectInstance effect = null;

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
            this.cloudCooldown = Math.max(this.cloudCooldown - 1, 0);
        }

        protected void resetBiteCooldown() {
            this.biteCooldown = 60;
        }

        protected void resetCloudCooldown() {
            this.cloudCooldown = 25;
        }
    }
}