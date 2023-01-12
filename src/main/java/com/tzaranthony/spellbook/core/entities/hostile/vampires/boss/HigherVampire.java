package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public abstract class HigherVampire extends SBVampireEntity {
    protected final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public HigherVampire(EntityType<? extends HigherVampire> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isResistantTo(DamageSource source) {
        return source.isMagic() || source == DamageSource.STARVE;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * this.getDayTimeDamageModifier();
        float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) target).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }

        boolean flag = target.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity)target).knockback((double)(f1 * 0.5F), (double) Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            if (target instanceof Player) {
                Player player = (Player)target;
                if (player.isUsingItem() && player.getUseItem().is(Items.SHIELD)) {
                    if (this.random.nextFloat() < 0.4F) {
                        player.getCooldowns().addCooldown(Items.SHIELD, 100);
                        this.level.broadcastEntityEvent(player, (byte)30);
                    }
                }
            }

            this.doEnchantDamageEffects(this, target);
            this.setLastHurtMob(target);
        }

        return flag;
    }

    public float getDayTimeDamageModifier() {
        if (this.level.isDay()) {
            return 0.5F;
        } else if (this.level.isNight()) {
            return 1.5F;
        }
        return 1.0F;
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Component tag) {
        super.setCustomName(tag);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.alertMinions((LivingEntity) source.getEntity());
        return super.hurt(source, amount);
    }

    protected void alertMinions(LivingEntity attacker) {
        double d0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB axisalignedbb = AABB.unitCubeFromLowerCorner(this.position()).inflate(d0, 10.0D, d0);
        this.level.getEntitiesOfClass(LesserVampire.class, axisalignedbb).stream()
                .filter((subjectC1) -> {
                    return subjectC1.getTarget() == null;
                }).forEach((subjectC3) -> {
                    subjectC3.setTarget(attacker);
                });
    }
}