package com.tzaranthony.spellbook.core.entities.arrows;

import com.tzaranthony.spellbook.core.entities.other.ShatteringCrystal;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class CrystalArrow extends AbstractArrow {
    public CrystalArrow(EntityType<? extends CrystalArrow> entityType, Level level) {
        super(entityType, level);
    }

    public CrystalArrow(LivingEntity owner, Level level) {
        super(SBEntities.CRYSTAL_ARROW.get(), owner, level);
    }


    protected ItemStack getPickupItem() {
        return new ItemStack(SBItems.CRYSTALAN_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        setBaseDamage(getBaseDamage() * 4.0D);
        BlockPos pos = result.getEntity().blockPosition();
        createCrystals(pos.getX(), pos.getY(), pos.getZ());
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        createCrystals(this.getX(), this.getY(), this.getZ());
        super.onHitBlock(result);
    }

    protected void createCrystals(double x, double y, double z) {
        if (this.getOwner() instanceof LivingEntity) {
            for(int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {
                    double d2 = 1.25D * (double) (i + 1);
                    double k = (Math.PI * (j * 45.0D)) / 180.0D;
                    level.addFreshEntity(finalizeCrystal(this.level, x + Math.cos(k) * d2, y, z + Math.sin(k) * d2, (float) k, i, (LivingEntity) this.getOwner()));
                }
            }
        }
    }

    protected ShatteringCrystal finalizeCrystal(Level level, double x, double y, double z, float yr, int delay, LivingEntity owner) {
        ShatteringCrystal crystal = new ShatteringCrystal(SBEntities.SHATTER_CRYSTAL.get(), level);
        crystal.warmupDelayTicks = delay;
        crystal.setOwner(owner);
        crystal.setYRot(yr * (180F / (float)Math.PI));
        crystal.setPos(x, y - 0.5D, z);
        return crystal;
    }
}