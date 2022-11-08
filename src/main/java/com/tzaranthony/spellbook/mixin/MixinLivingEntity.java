package com.tzaranthony.spellbook.mixin;

import com.tzaranthony.spellbook.core.entities.friendly.SummonedVex;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Shadow
    public native ItemStack getItemInHand(InteractionHand hand_1);

    @Shadow
    public native void setHealth(float health);

    @Shadow
    public native boolean removeAllEffects();

    @Shadow
    public native boolean addEffect(MobEffectInstance effect);

    @Inject(at = @At(value = "HEAD"), method = "checkTotemDeathProtection", cancellable = true)
    private void checkAvengingTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source.isBypassInvul()) {
            cir.setReturnValue(false);
        } else {
            Entity entity = this;

            ItemStack itemstack = null;

            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack itemstack1 = this.getItemInHand(hand);
                if (itemstack1.getItem().equals(SBItems.TOTEM_OF_AVENGING)) {
                    itemstack = itemstack1.copy();
                    itemstack1.shrink(1);
                    break;
                }
            }

            if (itemstack != null) {
                if (entity instanceof ServerPlayer) {
                    ServerPlayer serverplayerentity = (ServerPlayer) entity;
                    serverplayerentity.awardStat(Stats.ITEM_USED.get(SBItems.TOTEM_OF_AVENGING.get()));
                    CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, itemstack);
                }

                this.setHealth(1.0F);
                this.removeAllEffects();
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 2));
                this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 2));
                this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                this.level.broadcastEntityEvent(this, (byte) 35);

                ServerLevel world = (ServerLevel) this.level;
                if (entity instanceof Player) {
                    for (int i = 0; i < 3; ++i) {
                        BlockPos blockpos = this.blockPosition().offset(-2 + this.random.nextInt(5), 1, -2 + this.random.nextInt(5));
                        EntityType entitytype = SBEntities.SUMMONED_VEX.get();
                        SummonedVex vex = (SummonedVex) entitytype.spawn(world, itemstack, (Player) entity, blockpos, MobSpawnType.MOB_SUMMONED, false, false);
                        vex.tame((Player) entity);
                        vex.setLimitedLife(40 * (30 + this.random.nextInt(90)));
                    }
                } else {
                    for (int i = 0; i < 3; ++i) {
                        BlockPos blockpos = this.blockPosition().offset(-2 + this.random.nextInt(5), 1, -2 + this.random.nextInt(5));
                        Vex vex = EntityType.VEX.create(this.level);
                        vex.moveTo(blockpos, 0.0F, 0.0F);
                        vex.finalizeSpawn(world, this.level.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                        vex.setOwner((Mob) entity);
                        vex.setBoundOrigin(blockpos);
                        vex.setLimitedLife(20 * (30 + this.random.nextInt(90)));
                        world.addFreshEntityWithPassengers(vex);
                    }
                }

                cir.setReturnValue(true);
            }
        }
    }
}