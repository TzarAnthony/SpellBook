package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlyingMeleeAndMagicAttackGoal extends Goal {
    protected final Mob mob;
    protected final ProjectileSpell spell;
    protected final int maxMagicCooldown;
    private int ticksUntilNextAttack;
    private int magicCooldown;

    public FlyingMeleeAndMagicAttackGoal(Mob mob, ProjectileSpell spell) {
        this(mob, spell, 10);
    }

    public FlyingMeleeAndMagicAttackGoal(Mob mob, ProjectileSpell spell, int maxMagicCooldown) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.maxMagicCooldown = maxMagicCooldown;
        this.spell = spell;
        this.mob = mob;
    }

    public boolean canUse() {
        if (this.mob.getTarget() != null && !this.mob.getMoveControl().hasWanted() && this.mob.level.random.nextInt(reducedTickDelay(7)) == 0) {
            return this.mob.distanceToSqr(this.mob.getTarget()) > 4.0D;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.mob.getMoveControl().hasWanted() && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    public void start() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            Vec3 vec3 = livingentity.getEyePosition();
            this.mob.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
        }
        if (this.mob instanceof FlyingEntity) {
            ((FlyingEntity) this.mob).playFlyingAttackSound();
        }
        this.ticksUntilNextAttack = 0;
        this.magicCooldown = 0;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity attacked = this.mob.getTarget();
        if (attacked != null) {
            this.mob.getLookControl().setLookAt(attacked, 30.0F, 30.0F);
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(attacked, this.mob.distanceToSqr(attacked));
        }
    }


    protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
        if (this.ticksUntilNextAttack <= 0) {
            if (distance > 9.0D && this.magicCooldown <= 0) {
                this.performMagicAttack(attacked);
                this.resetMagicCooldown();
            } else if (this.mob.getBoundingBox().intersects(attacked.getBoundingBox())) {
                this.mob.doHurtTarget(attacked);
            } else {
                if (distance < 9.0D) {
                    Vec3 vec3 = attacked.getEyePosition();
                    this.mob.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                }
            }
            // reduce second timers
            this.resetAttackCooldown();
            this.magicCooldown = Math.max(this.magicCooldown - 1, 0);
        }
    }

    protected void performMagicAttack(LivingEntity attacked) {
        this.mob.getLookControl().setLookAt(attacked.position());
        MagicProjectile magic = new MagicProjectile(this.spell.getMagicProjectile(), this.mob.level);
        magic.setOwner(this.mob);
        magic.setPos(this.mob.getX(), this.mob.getEyeY() - (double)0.15F, this.mob.getZ());
        magic.setSpell(this.spell.getId());
        this.spell.addSpellDataToProjectile(magic);
        double d0 = attacked.getX() - this.mob.getX();
        double d1 = attacked.getEyeY() - magic.getY();
        double d2 = attacked.getZ() - this.mob.getZ();
        magic.shoot(d0, d1, d2, 1.6F, (float) (10 - this.mob.level.getDifficulty().getId() * 3));
        mob.level.addFreshEntity(magic);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected void resetMagicCooldown() {
        this.magicCooldown = this.maxMagicCooldown;
    }
}