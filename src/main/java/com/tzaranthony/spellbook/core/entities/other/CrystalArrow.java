package com.tzaranthony.spellbook.core.entities.other;

import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
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
            for(int l = 0; l < 9; ++l) {
                for (int i = 0; i < 9; ++i) {
                    double d2 = 1.25D * (double)(l + 1);
                    int j = 1 * l;
                    float k = i * 60;
                    //TODO: create custom crystal entity that damages then explodes into shards when disappearing
                    level.addFreshEntity(new EvokerFangs(this.level, x + (double) Mth.cos(k) * d2, y, z + (double)Mth.sin(k) * d2, k, j, (LivingEntity) this.getOwner()));
                }
            }
        }
    }
}