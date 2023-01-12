package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Snare extends AreaBlockSpell{
    public Snare(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.SQUID_INK);
    }

    @Override
    public boolean perform_spell(Entity user, Entity tgt) {
        snareEntities(tgt.getLevel(), tgt.blockPosition());
        return placeBlocksInArea(tgt.blockPosition().north(2).east(2).above(2), tgt.getLevel(), 5, 5, 5, 8);
    }

    @Override
    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        snareEntities(level, pos);
        return placeBlocksInArea(pos.north(2).east(2).above(2), level, 5, 5, 5, 8);
    }

    public void snareEntities(Level level, BlockPos pos) {
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                new AABB((double) (pos.getX() - 2), (double) (pos.getY() - 2), (double) (pos.getZ() - 2), (double) (pos.getX() + 2), (double) (pos.getY() + 2), (double) (pos.getZ() + 2)));

        if (!entities.isEmpty()) {
            for (LivingEntity mob : entities) {
                if (mob.isAlive() && !mob.isRemoved()) {
                    placeBlock(level, mob.blockPosition());
                }
            }
        }
    }

    @Override
    public void placeBlock(Level level, BlockPos pos) {
        level.setBlock(pos, SBBlocks.SNARE.get().defaultBlockState(), 2);
    }

    @Override
    public void playCustomSound(Entity user) {
        user.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, 1.0F, 1.0F);
    }
}