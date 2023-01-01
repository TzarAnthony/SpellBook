package com.tzaranthony.spellbook.core.entities.arrows;

import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownTool extends AbstractArrow {
    //TODO: create new accessor for spinning the tool?
    private static final EntityDataAccessor<ItemStack> TOOL = SynchedEntityData.defineId(ThrownTool.class, EntityDataSerializers.ITEM_STACK);
    private ItemStack toolItem;
    private boolean dealtDamage;

    public ThrownTool(EntityType<? extends ThrownTool> type, Level level) {
        super(type, level);
        this.toolItem = new ItemStack(Items.IRON_SWORD);
    }

    public ThrownTool(Level level, LivingEntity owner, ItemStack stack) {
        super(SBEntities.THROWN_TOOL.get(), owner, level);
        assert stack.getItem() instanceof TieredItem;
        stack.setEntityRepresentation(this);
        this.toolItem = stack;
        this.entityData.set(TOOL, stack);
    }

    protected void defineSynchedData() {
        this.entityData.define(TOOL, ItemStack.EMPTY);
        super.defineSynchedData();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (accessor.equals(TOOL)) {
            ItemStack itemstack = this.getTool();
            if (!itemstack.isEmpty() && itemstack.getEntityRepresentation() != this) {
                itemstack.setEntityRepresentation(this);
            }
        }
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if ((this.dealtDamage || this.isNoPhysics()) && this.getOwner() != null) {
            if (!this.isAcceptableReturnOwner()) {
                if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }
        super.tick();
    }

    private boolean isAcceptableReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getPickupItem() {
        return this.toolItem.copy();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }

    protected void onHitEntity(EntityHitResult p_37573_) {
        Entity entity = p_37573_.getEntity();
        float f = 6.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) entity;
            f += EnchantmentHelper.getDamageBonus(this.toolItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSource.thrown(this, (entity1 == null ? this : entity1));
        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity) entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }
                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        this.playSound(SoundEvents.TRIDENT_HIT, f1, 1.0F);
    }

    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Tool", 10)) {
            this.toolItem = ItemStack.of(tag.getCompound("Tool"));
        }
        this.dealtDamage = tag.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", this.toolItem.save(new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    public ItemStack getTool() {
        return this.entityData.get(TOOL);
    }

    protected float getWaterInertia() {
        return 0.72F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }
}