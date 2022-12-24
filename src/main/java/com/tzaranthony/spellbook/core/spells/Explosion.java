package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.MagicExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
        MagicExplosion explosion = new MagicExplosion(user, null, tgt.getX(), tgt.getY(), tgt.getZ(), 10.0F, net.minecraft.world.level.Explosion.BlockInteraction.BREAK);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(user.level, explosion)) return true;
        explosion.explode();
        explosion.finalizeExplosion(true);

        if (user.level instanceof ServerLevel sLevel) {
            for(ServerPlayer serverplayer : sLevel.getPlayers(LivingEntity::isAlive)) {
                if (serverplayer.distanceToSqr(tgt.getX(), tgt.getY(), tgt.getZ()) < 4096.0D) {
                    serverplayer.connection.send(new ClientboundExplodePacket(tgt.getX(), tgt.getY(), tgt.getZ(), 10.0F, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer)));
                }
            }
        }
        return true;
    }

    @Override
    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        MagicExplosion explosion = new MagicExplosion(user, null, pos.getX(), pos.getY(), pos.getZ(), 10.0F, net.minecraft.world.level.Explosion.BlockInteraction.BREAK);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(user.level, explosion)) return true;
        explosion.explode();
        explosion.finalizeExplosion(true);

        if (user.level instanceof ServerLevel sLevel) {
            for(ServerPlayer serverplayer : sLevel.getPlayers(LivingEntity::isAlive)) {
                if (serverplayer.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 4096.0D) {
                    serverplayer.connection.send(new ClientboundExplodePacket(pos.getX(), pos.getY(), pos.getZ(), 10.0F, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer)));
                }
            }
        }
        return true;
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0F, 1.0F);
    }
}