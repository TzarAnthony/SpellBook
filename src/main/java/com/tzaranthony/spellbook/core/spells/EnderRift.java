package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class EnderRift extends BlockCreateSpell{
    public EnderRift(int id, String name, SpellTier tier, RegistryObject<Block> block) {
        super(id, name, tier, block);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.ENCHANTED_HIT);
    }

    @Override
    public boolean perform_spell(Entity user, Entity target) {
        BlockPos pos = getOpenFace(target.getLevel(), target.getOnPos(), 2);
        return target.level.setBlock(pos, getBlock().defaultBlockState(), 4);
    }

    @Override
    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        pos = getOpenFace(level, pos, 2);
        return level.setBlock(pos, getBlock().defaultBlockState(), 4);
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 0.5F, 1.0F);
    }
}