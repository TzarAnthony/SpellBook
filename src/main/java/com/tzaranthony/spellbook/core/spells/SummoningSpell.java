package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.friendly.SBSummonedEntity;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.SBArmorMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;

public abstract class SummoningSpell extends Spell {
    public SummoningSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    public abstract EntityType getEntity(Level level);

    @Override
    public boolean perform_spell(Level level, LivingEntity summoner, InteractionHand hand, BlockPos pos) {
        if (!level.isClientSide && summoner instanceof Player player) {
            ItemStack stack = player.getItemInHand(hand);
            EntityType entityType = getEntity(level);
            int countbonus = getSummonBonus(player);

            int timeBonus = 0;
            if (countbonus == 4) {
                timeBonus = 30;
            }
            for (int i = 0; i < 3 + countbonus; ++i) {
                Entity entity = entityType.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level, entityType), MobSpawnType.MOB_SUMMONED, false, false);
                if (entity instanceof TamableAnimal) {
                    ((TamableAnimal) entity).tame(player);
                }
                if (entity instanceof SBSummonedEntity) {
                    ((SBSummonedEntity) entity).setLimitedLife(40 * (timeBonus + 30 + player.getRandom().nextInt(90)));;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public BlockPos getRandomEntitySpawnPos(Player player, Level level, EntityType entitytype) {
        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());
        int y = Mth.floor(player.getY());

        for (int j = 0; j < 30; ++j) {
            int x1 = x + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int y1 = y + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int z1 = z + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            if (x != x1 && z != z1) {
                BlockPos spawnPoint = new BlockPos(x1, y1, z1);
                SpawnPlacements.Type spawnplacements$type = SpawnPlacements.getPlacementType(entitytype);
                if (NaturalSpawner.isSpawnPositionOk(spawnplacements$type, player.level, spawnPoint, entitytype)
                        && SpawnPlacements.checkSpawnRules(entitytype, (ServerLevelAccessor) player.level, MobSpawnType.MOB_SUMMONED, spawnPoint, level.random)) {
                    return spawnPoint;
                }
            }
        }
        return player.blockPosition();
    }

    public int getSummonBonus(Player player) {
        int bonus = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == SBArmorMaterial.CURSED) {
                ++bonus;
            }
        }
        return bonus;
    }
}