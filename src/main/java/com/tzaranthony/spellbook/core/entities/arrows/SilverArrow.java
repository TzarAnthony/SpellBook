package com.tzaranthony.spellbook.core.entities.arrows;

import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class SilverArrow extends AbstractArrow {
    public SilverArrow(EntityType<? extends SilverArrow> entityType, Level level) {
        super(entityType, level);
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(SBItems.SILVER_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof Mob mob) {
            if (mob.getType().is(SBEntityTags.SILVER_VULNERABLE)) {
                setBaseDamage(getBaseDamage() * 2.0D);
            }
        }
        super.onHitEntity(result);
    }
}