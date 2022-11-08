package com.tzaranthony.spellbook.core.util.effects;

import com.tzaranthony.spellbook.core.entities.hostile.alchemical.SkeletonIllusion;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.ZombieIllusion;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;

public class MercuryEffect extends SBEffect {
    public MobEffect effect;

    public MercuryEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity affected, int amplifier) {
        if (!affected.level.isClientSide) {
            if (amplifier > 4 && Math.round(Math.random() * 10) >= 7) {
                int spawnCount = Math.round(Mth.nextInt(affected.getRandom(), 1, amplifier - 4));
                for (int i = 0; i < spawnCount; ++i) {
                    Level level = affected.level;

                    int x = Mth.floor(affected.getX());
                    int z = Mth.floor(affected.getZ());
                    int y = Mth.floor(affected.getY());

                    for (int j = 0; j < 30; ++j) {
                        int x1 = x + Mth.nextInt(affected.getRandom(), 7, 30) * Mth.nextInt(affected.getRandom(), -1, 1);
                        int y1 = y + Mth.nextInt(affected.getRandom(), 7, 20) * Mth.nextInt(affected.getRandom(), -1, 1);
                        int z1 = z + Mth.nextInt(affected.getRandom(), 7, 30) * Mth.nextInt(affected.getRandom(), -1, 1);
                        if (x != x1 && z != z1) {
                            BlockPos spawnPoint = new BlockPos(x1, y1, z1);
                            EntityType<?> entitytype = EntityType.ZOMBIE;
                            SpawnPlacements.Type entityspawnplacementregistry$placementtype = SpawnPlacements.getPlacementType(entitytype);
                            if (NaturalSpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, affected.level, spawnPoint, entitytype)
                                    && SpawnPlacements.checkSpawnRules(entitytype, (ServerLevelAccessor) affected.level, MobSpawnType.REINFORCEMENT, spawnPoint, level.random)
                                    && level.isUnobstructed(affected) && level.noCollision(affected) && !level.containsAnyLiquid(affected.getBoundingBox())
                            ) {
                                if (affected != null) {
                                    if (Math.round(Math.random() * 100) < (5 * Math.floor((amplifier + 1) / 2))) {
                                        if (Math.round(Math.random() * 10) >= 3) {
//                                            LiberExponentia.LOGGER.info("Spawned Zombie");
                                            Zombie zombie = (Zombie) EntityType.ZOMBIE.spawn((ServerLevel) affected.level, (ItemStack) null, (Player) affected, spawnPoint, MobSpawnType.SPAWN_EGG, false, false);
                                            zombie.setTarget(affected);
                                            zombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000));
                                            zombie.finalizeSpawn(affected.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(spawnPoint), MobSpawnType.SPAWN_EGG, (SpawnGroupData) null, (CompoundTag) null);
                                        } else {
//                                            LiberExponentia.LOGGER.info("Spawned Skeleton");
                                            Skeleton skeleton = (Skeleton) EntityType.SKELETON.spawn((ServerLevel) affected.level, (ItemStack) null, (Player) affected, spawnPoint, MobSpawnType.SPAWN_EGG, false, false);
                                            skeleton.setTarget(affected);
                                            skeleton.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000));
                                            skeleton.finalizeSpawn(affected.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(spawnPoint), MobSpawnType.SPAWN_EGG, (SpawnGroupData) null, (CompoundTag) null);
                                        }
                                    } else {
                                        if (Math.round(Math.random() * 10) >= 3) {
//                                            LiberExponentia.LOGGER.info("Spawned Pig");
                                            ZombieIllusion fakeZombie = (ZombieIllusion) SBEntities.FAKE_ZOMBIE.get().spawn((ServerLevel) affected.level, (ItemStack) null, (Player) affected, spawnPoint, MobSpawnType.SPAWN_EGG, false, false);
                                            fakeZombie.setTarget(affected);
                                            fakeZombie.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000));
                                            fakeZombie.finalizeSpawn(affected.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(spawnPoint), MobSpawnType.SPAWN_EGG, (SpawnGroupData) null, (CompoundTag) null);
                                        } else {
//                                            LiberExponentia.LOGGER.info("Spawned Sheep");
                                            SkeletonIllusion skelly = (SkeletonIllusion) SBEntities.FAKE_SKELLY.get().spawn((ServerLevel) affected.level, (ItemStack) null, (Player) affected, spawnPoint, MobSpawnType.SPAWN_EGG, false, false);
                                            skelly.setTarget(affected);
                                            skelly.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000));
                                            skelly.finalizeSpawn(affected.getServer().getLevel(level.dimension()), level.getCurrentDifficultyAt(spawnPoint), MobSpawnType.SPAWN_EGG, (SpawnGroupData) null, (CompoundTag) null);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 80 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}