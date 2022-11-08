package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RiftSpell extends ProjectileSpell{
    public RiftSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(SBSpellRegistry.RIFT_OF_DARKNESS.getId());
        magic.setParticle(ParticleTypes.ENCHANT);
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 3.0F, 0.6F);
    }
}