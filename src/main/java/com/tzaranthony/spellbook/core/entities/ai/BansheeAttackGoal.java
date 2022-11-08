package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.SBGhostEntity;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BansheeAttackGoal extends MeleeAttackGoal {
    protected final SBGhostEntity mob;
    int sonicCooldown;

    public BansheeAttackGoal(SBGhostEntity ghost, double boost, boolean whenNotSeen) {
        super(ghost, boost, whenNotSeen);
        this.mob = ghost;
    }

    @Override
    public void start() {
        this.sonicCooldown = 0;
        super.start();
    }

    protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
        double d0 = this.getAttackReachSqr(attacked);
        if (this.getTicksUntilNextAttack() <= 0) {
            if (distance >= d0 * 2.0D && this.sonicCooldown <= 0) {
                this.performSonicAttack(attacked);
                this.resetSonicCooldown();
            } else if (distance <= d0) {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(attacked);

            }
            // reduce second timers
            this.resetAttackCooldown();
            this.sonicCooldown = Math.max(this.sonicCooldown - 1, 0);
        }
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
        magic.shoot(d0, d1, d2, 1.6F, (float) (10 - this.mob.level.getDifficulty().getId() * 3));
        mob.level.addFreshEntity(magic);
    }

    protected void resetSonicCooldown() {
        this.sonicCooldown = 10;
    }
}