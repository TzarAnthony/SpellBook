package com.tzaranthony.spellbook.core.items.equipment.arrows;

import com.tzaranthony.spellbook.core.entities.arrows.CrystalArrow;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalArrowItem extends ArrowItem {
    public CrystalArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity owner) {
        CrystalArrow arrow = new CrystalArrow(SBEntities.CRYSTAL_ARROW.get(), level);
        arrow.setPos(owner.getX(), owner.getEyeY() - (double)0.1F, owner.getZ());
        arrow.setOwner(owner);
        return arrow;
    }
}