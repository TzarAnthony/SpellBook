package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;

public class AnimalSummoning extends Spell {
    public AnimalSummoning(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos pos) {
        ItemStack stack = player.getItemInHand(hand);
        EntityType entitytype;

        int selector = player.getRandom().nextInt(5);
        if (selector == 1) {
            entitytype = EntityType.PIG;
        } else if (selector == 2) {
            entitytype = EntityType.COW;
        } else if (selector == 3) {
            entitytype = EntityType.SHEEP;
        } else if (selector == 4) {
            entitytype = EntityType.RABBIT;
        } else {
            entitytype = EntityType.CHICKEN;
        }

        if (!level.isClientSide) {
            for (int i = 0; i < 3; ++i) {
                Entity entity = entitytype.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
            }
            return true;
        } else {
            return false;
        }
    }

    public BlockPos getRandomEntitySpawnPos(Player player, Level level) {
        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());
        int y = Mth.floor(player.getY());

        for (int j = 0; j < 30; ++j) {
            int x1 = x + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int y1 = y + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int z1 = z + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            if (x != x1 && z != z1) {
                BlockPos spawnPoint = new BlockPos(x1, y1, z1);
                EntityType<?> entitytype = EntityType.ZOMBIE;
                SpawnPlacements.Type entityspawnplacementregistry$placementtype = SpawnPlacements.getPlacementType(entitytype);
                if (NaturalSpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, player.level, spawnPoint, entitytype)
                        && SpawnPlacements.checkSpawnRules(entitytype, (ServerLevelAccessor) player.level, MobSpawnType.REINFORCEMENT, spawnPoint, level.random)
                        && level.isUnobstructed(player) && level.noCollision(player) && !level.containsAnyLiquid(player.getBoundingBox())
                ) {
                    return spawnPoint;
                }
            }
        }
        return player.blockPosition();
    }
}