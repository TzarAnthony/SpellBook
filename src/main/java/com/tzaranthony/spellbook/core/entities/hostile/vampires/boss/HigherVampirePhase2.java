package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.ai.DoNothingGoal;
import com.tzaranthony.spellbook.core.entities.ai.WaitingEntity;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class HigherVampirePhase2 extends HigherVampire implements WaitingEntity {
    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(HigherVampirePhase2.class, EntityDataSerializers.INT);
    private int attackAnimationTick;
    public boolean inWall;
    public HigherVampirePhase2(EntityType<? extends HigherVampirePhase2> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DoNothingGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new VampireMonsterAttackGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBVampireEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ARMOR, 20.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 5.0D)
                .add(Attributes.JUMP_STRENGTH, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 90.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_INV, 0);
    }

    public void addAdditionalSaveData(CompoundTag p_31485_) {
        super.addAdditionalSaveData(p_31485_);
        p_31485_.putInt("Invul", this.getInvulnerableTicks());
    }

    public void readAdditionalSaveData(CompoundTag p_31474_) {
        super.readAdditionalSaveData(p_31474_);
        this.setInvulnerableTicks(p_31474_.getInt("Invul"));
    }

    public void tick() {
        if (this.isVehicle() && this.level.getGameTime() % 30L == 0L) {
            for (Entity entity : this.getPassengers()) {
                if (entity instanceof LivingEntity le) {
                    le.addEffect(new MobEffectInstance(new MobEffectInstance(SBEffects.BLEEDING.get(), 200, 1)));
                } else {
                    entity.hurt(SBDamageSource.bite(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
            }
        }
        super.tick();
    }

    public void aiStep() {
        if (this.getInvulnerableTicks() > 0) {
            int k1 = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0F - (float)k1 / 220.0F);
            if (k1 <= 0) {
                this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.RAVAGER_ROAR, SoundSource.HOSTILE, 1.0F, 1.0F);
                //TODO: create some kind of animation
            }

            this.setInvulnerableTicks(k1);
            if (this.tickCount % 10 == 0) {
                this.heal(10.0F);
            }
        } else {
            super.aiStep();
            if (this.attackAnimationTick > 0) {
                --this.attackAnimationTick;
            }
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            if (!this.level.isClientSide) {
                this.inWall = this.checkWalls(this.getBoundingBox());
            }

            if (this.getInvulnerableTicks() > 0) {
                for(int i1 = 0; i1 < 3; ++i1) {
                    this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), (double)0.7F, (double)0.7F, (double)0.9F);
                }
            }
        }

        if (this.isDeadOrDying()) {
            this.playSound(SoundEvents.RAVAGER_CELEBRATE, 0.5F, 1.0F);
            HigherVampirePhase3 phase3 = this.convertTo(SBEntities.HIGH_VAMP3.get(), false);
            phase3.setVariant(this.getVariant());
            phase3.setTarget(this.getTarget());
            phase3.updateMist();
            this.remove(RemovalReason.DISCARDED);
        }
    }

    //TODO: create flying option instead of levitation immunity?
    public boolean canBeAffected(MobEffectInstance p_31495_) {
        return p_31495_.getEffect() == MobEffects.LEVITATION ? false : super.canBeAffected(p_31495_);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getInvulnerableTicks() > 0 && source != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        this.attackAnimationTick = 10;
        return super.doHurtTarget(target);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackAnimationTick = 10;
        }  else {
            super.handleEntityEvent(id);
        }
    }

    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.RAVAGER_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_STUNNED;
    }

    protected void playStepSound(BlockPos p_33350_, BlockState p_33351_) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
    }

    private boolean checkWalls(AABB p_31140_) {
        int i = Mth.floor(p_31140_.minX);
        int j = Mth.floor(p_31140_.minY);
        int k = Mth.floor(p_31140_.minZ);
        int l = Mth.floor(p_31140_.maxX);
        int i1 = Mth.floor(p_31140_.maxY);
        int j1 = Mth.floor(p_31140_.maxZ);
        boolean inWall = false;
        boolean destroyed = false;

        for(int k1 = i; k1 <= l; ++k1) {
            for(int l1 = j; l1 <= i1; ++l1) {
                for(int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!blockstate.isAir() && blockstate.getMaterial() != Material.FIRE) {
                        if (net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.level, blockpos, this) && !blockstate.is(BlockTags.WITHER_IMMUNE)) {
                            destroyed = this.level.removeBlock(blockpos, false) || destroyed;
                        } else {
                            inWall = true;
                        }
                    }
                }
            }
        }

        if (destroyed) {
            BlockPos blockpos1 = new BlockPos(i + this.random.nextInt(l - i + 1), j + this.random.nextInt(i1 - j + 1), k + this.random.nextInt(j1 - k + 1));
            this.level.levelEvent(2008, blockpos1, 0);
        }
        return inWall;
    }

    public void makeInvulnerable() {
        this.setInvulnerableTicks(220);
        this.bossEvent.setProgress(0.0F);
        this.setHealth(this.getMaxHealth());
    }

    public void setInvulnerableTicks(int p_31511_) {
        this.entityData.set(DATA_ID_INV, p_31511_);
    }

    public int getInvulnerableTicks() {
        return this.entityData.get(DATA_ID_INV);
    }

    @Override
    public boolean shouldWait() {
        return HigherVampirePhase2.this.getInvulnerableTicks() > 0;
    }

    public double getPassengersRidingOffset() {
        return (double)this.getType().getDimensions().height * 0.75D;
    }

    class VampireMonsterAttackGoal extends Goal {
        protected final HigherVampirePhase2 mob;
        private double speedModifier = 1.05D;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private int sequencedAttacks = 0;
        private long lastCanUseCheck;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;

        public VampireMonsterAttackGoal(HigherVampirePhase2 mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            long i = this.mob.level.getGameTime();
            if (i - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = i;
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else {
                    if (canPenalize) {
                        if (--this.ticksUntilNextPathRecalculation <= 0) {
                            this.path = this.mob.getNavigation().createPath(livingentity, 0);
                            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                            return this.path != null;
                        } else {
                            return true;
                        }
                    }
                    this.path = this.mob.getNavigation().createPath(livingentity, 0);
                    if (this.path != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {
            LivingEntity livingentity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.setTarget((LivingEntity)null);
            }

            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                double d0 = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());

                if (d0 < 5 * 5) {
                    this.speedModifier = 1.0D;
                } else if (d0 > 15 * 15) {
                    this.speedModifier = 1.8D;
                } else {
                    this.speedModifier = 1.35D;
                    //TODO: add leap at target goal here??
                }

                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if (this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = target.getX();
                    this.pathedTargetY = target.getY();
                    this.pathedTargetZ = target.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (this.canPenalize) {
                        this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                        if (this.mob.getNavigation().getPath() != null) {
                            net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                            if (finalPathPoint != null && target.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                                failedPathFindingPenalty = 0;
                            else
                                failedPathFindingPenalty += 10;
                        } else {
                            failedPathFindingPenalty += 10;
                        }
                    }
                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(target, d0);
            }
        }

        protected void checkAndPerformAttack(LivingEntity target, double distance) {
            double d0 = this.getAttackReachSqr(target);
            if (distance <= d0 && this.ticksUntilNextAttack <= 0) {

                if (this.sequencedAttacks > 0 || this.mob.random.nextInt(10) != 0) {
                    this.mob.doHurtTarget(target);

                    if (this.sequencedAttacks >= 3) {
                        target.hurt(DamageSource.explosion(this.mob), 10.0F);

                        this.mob.swing(InteractionHand.MAIN_HAND);
                        this.mob.swing(InteractionHand.OFF_HAND);
                        this.resetAttackCooldown();
                        this.resetAttackSequence();
                        this.strongKnockback(target);
                    } else {
                        if (this.sequencedAttacks % 2 == 0) {
                            this.mob.swing(InteractionHand.MAIN_HAND);
                        } else {
                            this.mob.swing(InteractionHand.OFF_HAND);
                        }
                        ++this.sequencedAttacks;
                        this.ticksUntilNextAttack = this.adjustedTickDelay(10);
                    }
                } else {
                    target.startRiding(this.mob);
                    this.resetAttackCooldown();
                    this.resetAttackSequence();
                }
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(60);
        }

        protected void resetAttackSequence() {
            this.sequencedAttacks = 0;
        }

        protected double getAttackReachSqr(LivingEntity target) {
            return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
        }

        protected void strongKnockback(Entity target) {
            double d0 = target.getX() - this.mob.getX();
            double d1 = target.getZ() - this.mob.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
            target.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
        }
    }
}