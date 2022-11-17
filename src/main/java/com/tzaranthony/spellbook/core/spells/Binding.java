package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Binding extends ProjectileSpell{
    public Binding(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }
    //TODO: create magic version of lead rendering: LeashFenceKnotEntity, LeashKnotRenderer, LeashKnotModel, MobRenderer, Mob

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.HEART);
    }

    @Override
    public boolean perform_spell(Entity usr, Entity tgt) {
        if (usr instanceof LivingEntity && tgt instanceof Mob) {
            Mob target = ((Mob) tgt);
            LivingEntity user = ((Player) usr);

            target.setLeashedTo(user, true);
            return true;
        }
        return false;
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.GLOW_INK_SAC_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}