package com.tzaranthony.spellbook.core.items.items;

import com.tzaranthony.spellbook.core.entities.other.PhoenixAshesEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PhoenixAshes extends Item {
    public PhoenixAshes(Item.Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.ashes")));
        super.appendHoverText(stack, level, tooltip, flag);
    }


    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(Level level, Entity entity, ItemStack stack) {
        final PhoenixAshesEntity ashesItemEntity = new PhoenixAshesEntity(entity, level, stack);
        return ashesItemEntity;
    }
}