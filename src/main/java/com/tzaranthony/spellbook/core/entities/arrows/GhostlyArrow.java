package com.tzaranthony.spellbook.core.entities.arrows;

import com.google.common.collect.Lists;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class GhostlyArrow extends AbstractArrow {
    private int duration = 200;
    @Nullable
    private List<BlockPos> passedThroughBlockPos;

    public GhostlyArrow(EntityType<? extends GhostlyArrow> entityType, Level level) {
        super(entityType, level);
    }

    public GhostlyArrow(LivingEntity owner, Level level, int pierceLevel) {
        super(SBEntities.GHOSTLY_ARROW.get(), owner, level);
        setPierceLevel((byte) pierceLevel);
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(SBItems.GHOSTLY_ARROW.get());
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);

        if (this.shakeTime > 0) {
            --this.shakeTime;
        }

        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW)) {
            this.clearFire();
        }

        this.inGroundTime = 0;
        Vec3 vec32 = this.position();
        Vec3 vec33 = vec32.add(vec3);
        HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec33 = hitresult.getLocation();
        }

        while(!this.isRemoved()) {
            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)hitresult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                    hitresult = null;
                    entityhitresult = null;
                }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
                this.hasImpulse = true;
            }

            if (entityhitresult == null || this.getPierceLevel() <= 0) {
                break;
            }

            hitresult = null;
        }

        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        if (this.isCritArrow()) {
            for(int i = 0; i < 4; ++i) {
                this.level.addParticle(ParticleTypes.CRIT, this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
            }
        }

        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();
        if (flag) {
            this.setYRot((float)(Mth.atan2(-d5, -d1) * (double)(180F / (float)Math.PI)));
        } else {
            this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
        }

        this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        float f = 0.99F;
        float f1 = 0.05F;
        if (this.isInWater()) {
            for(int j = 0; j < 4; ++j) {
                float f2 = 0.25F;
                this.level.addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25D, d2 - d6 * 0.25D, d3 - d1 * 0.25D, d5, d6, d1);
            }

            f = this.getWaterInertia();
        }

        this.setDeltaMovement(vec3.scale((double)f));
        if (!this.isNoGravity() && !flag) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - (double)0.05F, vec34.z);
        }

        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSource.indirectMagic(this, this);
        } else {
            damagesource = DamageSource.indirectMagic(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastHurtMob(entity);
            }
        }
        entity.hurt(damagesource, 4);
        super.onHitEntity(result);
    }

    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
        target.addEffect(mobeffectinstance, this.getEffectSource());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        result.getBlockPos();

        if (this.passedThroughBlockPos == null) {
            this.passedThroughBlockPos = Lists.newArrayListWithCapacity(5);
        }

        if (this.passedThroughBlockPos.size() >= 4) {
            this.resetPiercedBlocks();
            this.discard();
            return;
        }

        this.passedThroughBlockPos.add(result.getBlockPos());
    }

    private void resetPiercedBlocks() {
        if (this.passedThroughBlockPos != null) {
            this.passedThroughBlockPos.clear();
        }
    }

    @Override
    public void setPierceLevel(byte b) {
        super.setPierceLevel((byte) (((int) b) + 2));
    }
}