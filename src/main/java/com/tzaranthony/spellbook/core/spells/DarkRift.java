package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class DarkRift extends BlockCreateSpell{
    public DarkRift(int id, String name, SpellTier tier, RegistryObject<Block> block) {
        super(id, name, tier, block);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.ENCHANT);
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}