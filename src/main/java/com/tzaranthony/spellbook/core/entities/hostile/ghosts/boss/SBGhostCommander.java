package com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.SBGhostEntity;
import com.tzaranthony.spellbook.core.entities.neutral.GhostHorse;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class SBGhostCommander extends SBGhostEntity {
    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public SBGhostCommander(EntityType<? extends SBGhostEntity> type, Level level) {
        super(type, level);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source == SBDamageSource.BLEED  || source == DamageSource.STALAGMITE
                || source == DamageSource.FALLING_STALACTITE;
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
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof PathfinderMob) {
            PathfinderMob pathfindermob = (PathfinderMob)this.getVehicle();
            this.yBodyRot = pathfindermob.yBodyRot;
        }
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        if (vehicle instanceof GhostHorse) {
            return true;
        }
        return false;
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(2));
        spawnData = super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
//        GhostHorse horse = SBEntities.GHOST_HORSE.get().create(this.level);
//        horse.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
//        horse.finalizeSpawn(accessor, difficulty, reason, null, null);
//        this.startRiding(horse);
        return spawnData;
    }
}