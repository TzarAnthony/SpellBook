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

public abstract class SummoningSpell extends Spell {
    public SummoningSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    public abstract EntityType getEntity(Level level);

    @Override
    public boolean perform_spell(Level level, LivingEntity summoner, InteractionHand hand, BlockPos pos) {
        if (!level.isClientSide && summoner instanceof Player player) {
            EntityType entityType = getEntity(level);
            int countbonus = getSummonBonus(player);

            int timeBonus = 0;
            if (countbonus == 4) {
                timeBonus = 30;
            }
            for (int i = 0; i < 3 + countbonus; ++i) {
                Entity entity = spawnEntityAtRandomPos(player, (ServerLevel) level, entityType);
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

    public Entity spawnEntityAtRandomPos(Player player, ServerLevel level, EntityType entitytype) {
        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());
        int y = Mth.floor(player.getY());

        for (int j = 0; j < 15; ++j) {
            int x1 = x + Mth.nextInt(player.getRandom(), 3, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int y1 = y + Mth.nextInt(player.getRandom(), 3, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int z1 = z + Mth.nextInt(player.getRandom(), 3, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            if (x != x1 && z != z1) {
                BlockPos spawnPoint = new BlockPos(x1, y1, z1);
                Entity e = entitytype.create(level);
                //TODO: fix when spawn placements are created
//                SpawnPlacements.Type spawnplacements$type = SpawnPlacements.getPlacementType(entitytype);
                SpawnPlacements.Type spawnplacements$type = SpawnPlacements.Type.ON_GROUND;
                if (NaturalSpawner.isSpawnPositionOk(spawnplacements$type, level, spawnPoint, entitytype)
                        && SpawnPlacements.checkSpawnRules(entitytype, level, MobSpawnType.MOB_SUMMONED, spawnPoint, level.random)) {
                    e.setPos(x1, y1, z1);
                    if (level.isUnobstructed(e) && level.noCollision(e) && !level.containsAnyLiquid(e.getBoundingBox())) {
                        if (e instanceof Mob) {
                            ((Mob) e).finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPoint), MobSpawnType.MOB_SUMMONED, null, null);
                        }
                        level.addFreshEntityWithPassengers(e);
                        return e;
                    }
                }
            }
        }
        return null;
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