package com.tzaranthony.spellbook.core.util.damage;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SBEntityDamageSource extends SBDamageSource {
    @Nullable
    protected final Entity entity;

    public SBEntityDamageSource(String name, @Nullable Entity entity) {
        super(name);
        this.entity = entity;
    }

    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    public Component getLocalizedDeathMessage(LivingEntity p_151519_1_) {
        ItemStack itemstack = this.entity instanceof LivingEntity ? ((LivingEntity)this.entity).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslatableComponent(s + ".item", p_151519_1_.getDisplayName(), this.entity.getDisplayName(), itemstack.getDisplayName()) : new TranslatableComponent(s, p_151519_1_.getDisplayName(), this.entity.getDisplayName());
    }

    public boolean scalesWithDifficulty() {
        return this.entity != null && this.entity instanceof LivingEntity && !(this.entity instanceof Player);
    }

    @Nullable
    public Vec3 getSourcePosition() {
        return this.entity != null ? this.entity.position() : null;
    }

    public String toString() {
        return "EntityDamageSource (" + this.entity + ")";
    }
}