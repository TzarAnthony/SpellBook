package com.tzaranthony.spellbook.core.entities.other;

import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class CrystalArrow extends AbstractArrow {
    public CrystalArrow(EntityType<? extends CrystalArrow> entityType, Level level) {
        super(entityType, level);
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(SBItems.CRYSTALAN_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        setBaseDamage(getBaseDamage() * 4.0D);
        BlockPos pos = result.getEntity().blockPosition();
        createShards(pos.getX(), pos.getY(), pos.getZ());
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        createShards(this.getX(), this.getY(), this.getZ());
        super.onHitBlock(result);
    }

    protected void createShards(double x, double y, double z) {
        if (this.getOwner() instanceof LivingEntity) {
            //TODO: create custom crystal entity that damages then explodes into shards when disappearing
            for(int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {
                    double d2 = 1.25D * (double) (i + 1);
//                    float k = 60.0F * j;
                    double k = (Math.PI * (j * 45.0D)) / 180.0D;
//                    float k = 45.0F * j;
                    level.addFreshEntity(new EvokerFangs(this.level, x + Math.cos(k) * d2, y, z + Math.sin(k) * d2, (float) k, i, (LivingEntity) this.getOwner()));
                }
            }
        }
    }
}