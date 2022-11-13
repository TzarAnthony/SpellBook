package com.tzaranthony.spellbook.core.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Scream extends ProjectileSpell {
    public Scream(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos blockPos) {
        MagicProjectile magic = new MagicProjectile(SBEntities.LARGE_MAGIC_PROJECTILE.get(), level);
        magic.initializeProjectile(player, player.getX(), player.getEyeY() - (double)0.15F, player.getZ());
        addSpellDataToProjectile(magic);

        Vec3 vec31 = player.getUpVector(1.0F);
        Quaternion quaternion = new Quaternion(new Vector3f(vec31), 0.0F, true);
        Vec3 vec3 = player.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        magic.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), 1.6F, 1.0F);

        level.addFreshEntity(magic);
        return true;
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(SBParticleTypes.SCREAM.get());
    }

    @Override
    public boolean perform_spell(Entity user, Entity target) {
        if (user instanceof LivingEntity && target instanceof LivingEntity) {
            ((LivingEntity) target).hurt(SBDamageSource.scream(((LivingEntity) user)), 10.0F);
            ((LivingEntity) target).addEffect(new MobEffectInstance(SBEffects.CONCUSSED.get(), 600, 0));
            ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 90, 0));
            ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.CONFUSION, 30, 0));
            if (target instanceof Player) {
                target.level.playSound((Player) target, target.getX(), target.getY(), target.getZ(), SoundEvents.CONDUIT_AMBIENT, SoundSource.NEUTRAL, 10.0F, 1.0F);
            }
            return true;
        }
        return false;
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.GHAST_HURT, SoundSource.PLAYERS, 5.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public boolean spellIgnoresBlocks() {
        return true;
    }
}