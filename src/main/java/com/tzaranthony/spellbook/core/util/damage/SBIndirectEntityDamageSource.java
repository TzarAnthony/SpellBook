package com.tzaranthony.spellbook.core.util.damage;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class SBIndirectEntityDamageSource extends SBEntityDamageSource {
    private final Entity owner;

    public SBIndirectEntityDamageSource(String name, Entity entity, @Nullable Entity owner) {
        super(name, entity);
        this.owner = owner;
    }

    @Nullable
    public Entity getDirectEntity() {
        return this.entity;
    }

    @Nullable
    public Entity getEntity() {
        return this.owner;
    }

    public Component getLocalizedDeathMessage(LivingEntity entity) {
        Component itextcomponent = this.owner == null ? this.entity.getDisplayName() : this.owner.getDisplayName();
        ItemStack itemstack = this.owner instanceof LivingEntity ? ((LivingEntity)this.owner).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslatableComponent(s1, entity.getDisplayName(), itextcomponent, itemstack.getDisplayName()) : new TranslatableComponent(s, entity.getDisplayName(), itextcomponent);
    }
}