package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class HigherVampire extends SBVampireEntity {
    protected final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public HigherVampire(EntityType<? extends HigherVampire> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 50;
    }

    public boolean isResistantTo(DamageSource source) {
        return source.isMagic() || source == DamageSource.STARVE;
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