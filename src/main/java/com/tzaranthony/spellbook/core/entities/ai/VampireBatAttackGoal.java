package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampireBat;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class VampireBatAttackGoal extends Goal {
    private final HigherVampireBat mob;
    int sonicCooldown;
    float damage;

    public VampireBatAttackGoal(HigherVampireBat vampireBat) {
        this.mob = vampireBat;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
        this.damage = (float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
    }

    public boolean canUse() {
        if (this.mob.getTarget() != null && !this.mob.getMoveControl().hasWanted() && this.mob.level.random.nextInt(7) == 0) {
            return this.mob.distanceToSqr(this.mob.getTarget()) > 4.0D;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.mob.getMoveControl().hasWanted()
                && this.mob.getTarget() != null
                && this.mob.getTarget().isAlive();
    }

    public void start() {
        this.sonicCooldown = 0;
        LivingEntity livingentity = this.mob.getTarget();
        Vec3 vector3d = livingentity.getEyePosition(1.0F);
        this.mob.getMoveControl().setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
        this.mob.playSound(SoundEvents.BAT_TAKEOFF, 1.0F, 1.0F);
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        double distance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        if (distance > 12.0D && distance <= 1600.0D && this.sonicCooldown <= 0) {
            this.performSonicAttack(target);
            this.resetSonicCooldown();
        } else if (this.mob.getBoundingBox().intersects(target.getBoundingBox())) {
            this.mob.doHurtTarget(target);
            target.hurt(SBDamageSource.bite(this.mob), this.damage * 0.75F);
            target.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
            this.mob.heal(this.damage * 0.75F);
        } else {
            double d0 = this.mob.distanceToSqr(target);
            if (d0 < 9.0D) {
                Vec3 vector3d = target.getEyePosition(1.0F);
                this.mob.getMoveControl().setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            }
        }
        this.sonicCooldown = Math.max(this.sonicCooldown - 1, 0);
    }

    protected void performSonicAttack(LivingEntity attacked) {
        this.mob.getLookControl().setLookAt(attacked.position());

        MagicProjectile magic = new MagicProjectile(SBEntities.LARGE_MAGIC_PROJECTILE.get(), this.mob.level);
        magic.setOwner(this.mob);
        magic.setPos(this.mob.getX(), this.mob.getEyeY() - (double)0.15F, this.mob.getZ());
        magic.setSpell(SBSpellRegistry.SCREAM.getId());
        magic.setParticle(SBParticleTypes.SCREAM.get());

        double d0 = attacked.getX() - this.mob.getX();
        double d1 = attacked.getEyeY() - magic.getY();
        double d2 = attacked.getZ() - this.mob.getZ();
        magic.shoot(d0, d1, d2, 1.6F, 1.0F);
        mob.level.addFreshEntity(magic);
    }

    protected void resetSonicCooldown() {
        this.sonicCooldown = 200;
    }
}