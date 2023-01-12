package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;

public class Ignite extends AreaBlockSpell {
    public Ignite(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.FLAME);
    }

    @Override
    public boolean perform_spell(Entity user, Entity tgt) {
        tgt.setSecondsOnFire(5);
        return placeBlocksInArea(tgt.getOnPos().north(2).east(2).above(2), tgt.getLevel(), 5, 5, 5, 3);
    }

    @Override
    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        return placeBlocksInArea(pos.north(2).east(2).above(2), level, 5, 5, 5, 3);
    }

    public void placeBlock(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
    }

    @Override
    public void playCustomSound(Entity user) {
        user.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F);
    }
}