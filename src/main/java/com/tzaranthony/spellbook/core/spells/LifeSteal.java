package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

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
        if (usr instanceof LivingEntity user && tgt instanceof LivingEntity target) {
            float thp = target.getHealth();
            float stealAmt = 4.0F;
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
    public void playCustomSound(Entity user) {
        user.playSound(SoundEvents.SOUL_ESCAPE, 30.0F, 0.6F + user.level.random.nextFloat() * 0.4F);
    }
}