package com.tzaranthony.spellbook.core.entities.other;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class CrystalShard extends AbstractArrow {
    public CrystalShard(EntityType<? extends CrystalShard> entityType, Level level) {
        super(entityType, level);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            for(int j = 0; j < 2; ++j) {
                this.level.addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.discard();
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}