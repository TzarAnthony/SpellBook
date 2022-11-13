package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LifeSteal extends ProjectileSpell {
    public LifeSteal(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.SOUL);
    }

    @Override
    public boolean perform_spell(Entity usr, Entity tgt) {
        if (usr instanceof LivingEntity && tgt instanceof LivingEntity) {
            LivingEntity target = ((LivingEntity) tgt);
            LivingEntity user = ((LivingEntity) usr);

            float thp = target.getHealth();
            float stealAmt = 2.0F;
            if (thp < stealAmt) {
                stealAmt = thp;
                target.kill();
            } else {
                target.hurt(SBDamageSource.lifesteal(user), stealAmt);
            }

            if (user.getHealth() + stealAmt <= user.getMaxHealth()) {
                user.heal(stealAmt);
            } else {
                user.setHealth(user.getMaxHealth());
            }

            return true;
        }
        return false;
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}