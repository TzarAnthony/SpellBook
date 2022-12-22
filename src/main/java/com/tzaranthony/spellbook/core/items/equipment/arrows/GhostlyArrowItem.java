package com.tzaranthony.spellbook.core.items.equipment.arrows;

import com.tzaranthony.spellbook.core.entities.arrows.GhostlyArrow;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class GhostlyArrowItem extends ArrowItem {
    public GhostlyArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity owner) {
        GhostlyArrow arrow = new GhostlyArrow(SBEntities.GHOSTLY_ARROW.get(), level);
        arrow.setPos(owner.getX(), owner.getEyeY() - (double)0.1F, owner.getZ());
        arrow.setPierceLevel((byte) 0);
        arrow.setOwner(owner);
        return arrow;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.ghostly_arrow")));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}