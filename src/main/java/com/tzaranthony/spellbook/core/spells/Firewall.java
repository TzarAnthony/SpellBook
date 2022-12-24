package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.other.AreaFireCloud;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Firewall extends ProjectileSpell {
    public Firewall(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.FLAME);
        magic.setLifetime(10 + magic.level.random.nextInt(7));
    }

    @Override
    public boolean perform_spell(Entity user, Entity target) {
        AreaFireCloud wall = new AreaFireCloud(SBEntities.FIRE_WALL.get(), target.getLevel());
        if (user instanceof LivingEntity) {
            wall.setOwner((LivingEntity) user);
        }
        BlockPos pos = target.blockPosition().above();
        wall.setPos(pos.getX(), pos.below(3).getY(), pos.getZ());
        wall.setYRot(user.getYRot());
        target.getLevel().addFreshEntity(wall);
        return true;
    }

    @Override
    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        AreaFireCloud wall = new AreaFireCloud(SBEntities.FIRE_WALL.get(), level);
        if (user instanceof LivingEntity) {
            wall.setOwner((LivingEntity) user);
        }
        wall.setPos(pos.above().getX(), pos.below(2).getY(), pos.above().getZ());
        wall.setYRot(user.getYRot());
        level.addFreshEntity(wall);
        return true;
    }

    public void finishSpell(Entity user, Level level, BlockPos pos) {
        AreaFireCloud wall = new AreaFireCloud(SBEntities.FIRE_WALL.get(), level);
        if (user instanceof LivingEntity) {
            wall.setOwner((LivingEntity) user);
        }
        wall.setPos(pos.getX(), pos.below(4).getY(), pos.getZ());
        wall.setYRot(user.getYRot());
        level.addFreshEntity(wall);
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 5.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
    }
}