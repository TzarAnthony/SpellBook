package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.friendly.SBSummonedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.function.Supplier;

public class TamingSummoningSpell extends Spell {
    public  Supplier<? extends EntityType<? extends SBSummonedEntity>> entity;

    public TamingSummoningSpell(int id, String name, SpellTier tier, Supplier<? extends EntityType<? extends SBSummonedEntity>> entity) {
        super(id, name, tier);
        this.entity = entity;
    }

    @Override
    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos pos) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            for (int i = 0; i < 3; ++i) {
                SBSummonedEntity entity = (SBSummonedEntity) this.entity.get().spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                entity.tame(player);
                entity.setLimitedLife(40 * (30 + player.getRandom().nextInt(90)));
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