package com.tzaranthony.spellbook.core.entities.other;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class ShatteringCrystal extends Entity {
    public int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 22;
    private boolean clientSideAttackStarted;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public ShatteringCrystal(EntityType<? extends ShatteringCrystal> entity, Level level) {
        super(entity, level);
    }

    protected void defineSynchedData() {
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.warmupDelayTicks = tag.getInt("Warmup");
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d1 = this.getY() + 0.05D + this.random.nextDouble();
                        double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.level.addParticle(ParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
                    this.dealDamageTo(livingentity);
                }
            }

            if (!this.sentSpikeEvent) {
                this.level.broadcastEntityEvent(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }

        if (this.lifeTicks == 10) {
            shatter();
            this.level.playSound(null, this.blockPosition(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    private void dealDamageTo(LivingEntity tgt) {
        LivingEntity livingentity = this.getOwner();
        if (tgt.isAlive() && !tgt.isInvulnerable() && tgt != livingentity) {
            if (livingentity == null) {
                tgt.hurt(DamageSource.MAGIC, 10.0F);
            } else {
                if (livingentity.isAlliedTo(tgt)) {
                    return;
                }
                tgt.hurt(DamageSource.indirectMagic(this, livingentity), 10.0F);
            }
        }
    }

    protected void shatter() {
        this.level.broadcastEntityEvent(this, (byte)17);
        this.gameEvent(GameEvent.EXPLODE, this.getOwner());
        this.dealExplosionDamage();
        for(int i = 0; i < 12; ++i) {
            double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * 4.5D;
            double d1 = this.getY() + (this.random.nextDouble() * 2.0D - 1.0D) * 4.5D;
            double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * 4.5D;
            double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
            double d4 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
            double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
            this.level.addParticle(ParticleTypes.ELECTRIC_SPARK, d0, d1 + 1.0D, d2, d3, d4, d5);
        }
        this.discard();
        //TODO: get this to work some day
//        Entity owned;
//        if (this.owner instanceof Player) {
//            owned = this.owner;
//        } else {
//            owned = this;
//        }
//
//        float offset = -10.0F;
//        for (int i = 0; i < 2; ++i) {
//            CrystalShard shard = new CrystalShard(SBEntities.CRYSTAL_SHARD.get(), this.level);
//            shard.setOwner(owned);
//            shard.setBaseDamage(4.0D);
//
//            Vec3 vec31 = this.getUpVector(1.0F);
//            Quaternion quaternion = new Quaternion(new Vector3f(vec31), offset, true);
//            Vec3 vec3 = this.getViewVector(1.0F);
//            Vector3f vector3f = new Vector3f(vec3);
//            vector3f.transform(quaternion);
//            shard.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), 1.6F, 1.0F);
//
//            this.level.addFreshEntity(shard);
//            offset += 10.0F;
//        }
    }

    private void dealExplosionDamage() {
        float f = 5.0F + (float)(5 * 2);

        if (f > 0.0F) {
            double d0 = 5.0D;
            Vec3 vec3 = this.position();

            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D))) {
                if (!(this.distanceToSqr(livingentity) > 25.0D)) {
                    boolean flag = false;

                    for(int i = 0; i < 2; ++i) {
                        Vec3 vec31 = new Vec3(livingentity.getX(), livingentity.getY(0.5D * (double)i), livingentity.getZ());
                        HitResult hitresult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        if (hitresult.getType() == HitResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        float f1 = f * (float)Math.sqrt((5.0D - (double)this.distanceTo(livingentity)) / 5.0D);
                        livingentity.hurt(DamageSource.explosion(this.getOwner()), f1);
                    }
                }
            }
        }
    }

    public void handleEntityEvent(byte p_36935_) {
        super.handleEntityEvent(p_36935_);
        if (p_36935_ == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.EVOKER_FANGS_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }

    public float getAnimationProgress(float p_36937_) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - p_36937_) / 20.0F;
        }
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}