package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.MagicExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Explosion extends ProjectileSpell {
    public Explosion(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.FIREWORK);
    }

    @Override
    public boolean perform_spell(Entity user, Entity tgt) {
        if (user instanceof LivingEntity) {
//            explode((LivingEntity) user, tgt.level, tgt.blockPosition());
            MagicExplosion explosion = new MagicExplosion(user.level, (LivingEntity) user, null, tgt.getX(), tgt.getY(), tgt.getZ(), 6.0F);
            explosion.explode();
            explosion.finalizeExplosion(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        if (user instanceof LivingEntity) {
            MagicExplosion explosion = new MagicExplosion(user.level, (LivingEntity) user, null, pos.getX(), pos.getY(), pos.getZ(), 6.0F);
            explosion.explode();
            explosion.finalizeExplosion(true);
            return true;
        }
        return false;
    }

//    public void explode(LivingEntity user, Level level, BlockPos pos) {
//        Vec3 vec3 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
//
//        for(LivingEntity tgt : level.getEntitiesOfClass(LivingEntity.class,
//                new AABB((double) (pos.getX() - 3), (double) (pos.getY() - 3), (double) (pos.getZ() - 3), (double) (pos.getX() + 3), (double) (pos.getY() + 3), (double) (pos.getZ() + 3)))) {
//
//            if (!(pos.distToCenterSqr(tgt.getX(), tgt.getY(), tgt.getZ()) > 25.0D)) {
//                boolean flag = false;
//
//                for(int i = 0; i < 2; ++i) {
//                    Vec3 vec31 = new Vec3(tgt.getX(), tgt.getY(0.5D * (double) i), tgt.getZ());
//                }
//                float f1 = 6 * (float)Math.sqrt((5.0D - (double) pos.distToCenterSqr(tgt.getX(), tgt.getY(), tgt.getZ())) / 5.0D);
//                tgt.hurt(DamageSource.explosion(user), f1);
//            }
//        }
//    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0F, 1.0F);
    }
}