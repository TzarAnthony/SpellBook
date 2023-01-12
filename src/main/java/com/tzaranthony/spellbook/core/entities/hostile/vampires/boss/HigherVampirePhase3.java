package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.google.common.collect.ImmutableList;
import com.tzaranthony.spellbook.core.entities.ai.VexLikeMoveControl;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import com.tzaranthony.spellbook.core.spells.SummoningSpell;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HigherVampirePhase3 extends HigherVampire {
    private static final Map<String, MistTypes> mistTypeMap = new HashMap<>() {{
        put(MistTypes.BLOOD.getName(), MistTypes.BLOOD);
        put(MistTypes.CRYO.getName(), MistTypes.CRYO);
        put(MistTypes.SLEEP.getName(), MistTypes.SLEEP);
        put(MistTypes.WITHER.getName(), MistTypes.WITHER);
    }};
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(HigherVampirePhase3.class, EntityDataSerializers.INT);
    private MistTypes mist = MistTypes.NONE;
    private int reapplicationDelay = 20;

    public HigherVampirePhase3(EntityType<? extends HigherVampirePhase3> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new VexLikeMoveControl(this);
        this.xpReward = 50;
    }

    @Override
    public void move(MoverType moverType, Vec3 vec3) {
        super.move(moverType, vec3);
        this.checkInsideBlocks();
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ChaseTargetAttackGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBVampireEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 90.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_COLOR, 0);
    }

    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        if (this.tickCount % 300 == 0) {
            this.updateMist();
        }

        this.setYRot(Mth.wrapDegrees(this.getYRot()));
        if (this.level.isClientSide) {
            float radius = this.getBbWidth() / 2.0F;
            int i = Mth.ceil((float)Math.PI * radius * radius);

            for(int j = 0; j < i; ++j) {
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = Mth.sqrt(this.random.nextFloat()) * radius;
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
                double d2 = this.getY();
                double d4 = this.getZ() + (double)(Mth.sin(f2) * f3);
                double d5 = ((float)(this.getColor() >> 16 & 255) / 255.0F);
                double d6 = ((float)(this.getColor() >> 8 & 255) / 255.0F);
                double d7 = ((float)(this.getColor() & 255) / 255.0F);
                this.level.addAlwaysVisibleParticle(ParticleTypes.ENTITY_EFFECT, d0, d2, d4, d5, d6, d7);
            }
        } else {
            List<LivingEntity> victims = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
            if (!victims.isEmpty()) {
                for(LivingEntity victim : victims) {
                    if (!(victim instanceof SBVampireEntity)) {
                        int i = EnchantmentHelper.getRespiration(victim);
                        int airSupply = victim.getAirSupply();
                        victim.setAirSupply(i > 0 && this.random.nextInt(i + 1) > 0 ? airSupply : airSupply - 2);

                        for(MobEffectInstance effectInstance : this.mist.getCurrentEffect()) {
                            if (effectInstance.getEffect().isInstantenous()) {
                                effectInstance.getEffect().applyInstantenousEffect(this, this, victim, effectInstance.getAmplifier(), 0.5D);
                            } else {
                                victim.addEffect(new MobEffectInstance(effectInstance), this);
                            }
                        }
                    }
                }
            }

            if (this.tickCount % 200 == 0) {
                int reinforcementCount = 2 + random.nextInt(3);
                for (int i = 0; i < reinforcementCount; ++i) {
                    SummoningSpell.spawnEntityAtRandomPos(this, (ServerLevel) level, SBEntities.LOW_VAMP.get());
                }
                this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
            }
        }
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.reapplicationDelay = tag.getInt("ReapplicationDelay");
        this.mist = this.mistTypeMap.get(tag.getString("Mist"));
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("ReapplicationDelay", this.reapplicationDelay);
        tag.putString("Mist", this.mist.getName());
        super.addAdditionalSaveData(tag);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.mist == MistTypes.WITHER && source.getEntity() instanceof Player player && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SMITE, player.getMainHandItem()) > 0) {
            amount = amount * this.mist.getCounterBonus();
        } else if (this.mist.getCounterDamageType() == source) {
            amount = amount * this.mist.getCounterBonus();
        } else if (source.isMagic()) {
            amount = amount * this.mist.getMagicResistance();
        } else {
            amount = amount * this.mist.getPhysicalResistance();
        }
        return super.hurt(source, amount);
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WITHER_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        spawnData = super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
        this.updateMist();
        return spawnData;
    }

    public void updateMist() {
        int i = this.level.random.nextInt(4);
        switch (i) {
            case 0:
                this.mist = MistTypes.BLOOD;
                break;
            case 1:
                this.mist = MistTypes.CRYO;
                break;
            case 2:
                this.mist = MistTypes.SLEEP;
                break;
            default:
                this.mist = MistTypes.WITHER;
                break;
        }
        this.updateColor();
    }

    private void updateColor() {
        this.getEntityData().set(DATA_COLOR, PotionUtils.getColor(this.mist.getCurrentEffect()));
    }

    public int getColor() {
        return this.getEntityData().get(DATA_COLOR);
    }

    class ChaseTargetAttackGoal extends Goal {
        protected final HigherVampirePhase3 mob;
        
        public ChaseTargetAttackGoal(HigherVampirePhase3 vampire) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.mob = vampire;
        }

        public boolean canUse() {
            if (this.mob.getTarget() != null && !this.mob.getMoveControl().hasWanted() && this.mob.random.nextInt(reducedTickDelay(7)) == 0) {
                return this.mob.getTarget().distanceToSqr(this.mob.getBoundingBox().getCenter()) > 1.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.mob.getMoveControl().hasWanted() && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.position();
                this.mob.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                double d0 = target.distanceToSqr(this.mob.getBoundingBox().getCenter());
                if (d0 < 1.0D) {
                    Vec3 vec3 = target.getEyePosition();
                    this.mob.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                }
            }
        }
    }

    // Enum for different mist types
    private enum MistTypes {
        NONE("None", new MobEffectInstance(MobEffects.GLOWING, 100), DamageSource.GENERIC, 0.0F, 0.0F, 0.0F),
        BLOOD("Blood",  new MobEffectInstance(SBEffects.BLEEDING.get(), 100, 1), DamageSource.FREEZE, 1.75F, 0.6F, 0.85F),
        CRYO("Cryo",  new MobEffectInstance(SBEffects.FREEZING.get(), 100, 1), SBDamageSource.INCINERATION, 2.25F, 0.9F, 0.5F),
        SLEEP("Sleep",  new MobEffectInstance(SBEffects.SLEEP.get(), 100, 1), SBDamageSource.BLEED, 1.5F, 0.5F, 0.9F),
        WITHER("Wither",  new MobEffectInstance(MobEffects.WITHER, 100, 2), DamageSource.GENERIC, 4.0F, 0.85F, 0.6F);

        private final String name;
        private final MobEffectInstance currentEffect;
        private final DamageSource counterDamageType;
        private final float counterBonusMultiplier;
        private final float physicalResistanceMultiplier;
        private final float wrongMagicResistanceMultiplier;

        MistTypes(String name, MobEffectInstance currentEffect, DamageSource counterDamageType, float counterBonus, float physicalResistance, float magicResistance) {
            this.name = name;
            this.currentEffect = currentEffect;
            this.counterDamageType = counterDamageType;
            this.counterBonusMultiplier = counterBonus;
            this.physicalResistanceMultiplier = physicalResistance;
            this.wrongMagicResistanceMultiplier = magicResistance;
        }

        public String getName() {
            return this.name;
        }

        public ImmutableList<MobEffectInstance> getCurrentEffect() {
            return ImmutableList.of(this.currentEffect);
        }

        public DamageSource getCounterDamageType() {
            return this.counterDamageType;
        }

        public float getCounterBonus() {
            return this.counterBonusMultiplier;
        }

        public float getPhysicalResistance() {
            return this.physicalResistanceMultiplier;
        }

        public float getMagicResistance() {
            return this.wrongMagicResistanceMultiplier;
        }
    }
}