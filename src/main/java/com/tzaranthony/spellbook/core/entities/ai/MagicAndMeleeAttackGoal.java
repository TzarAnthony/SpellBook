package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class MagicAndMeleeAttackGoal extends MeleeAttackGoal {
    protected final Mob mob;
    protected final ProjectileSpell spell;
    protected final int maxCooldown;
    protected final int variability;
    int magicCooldown;

    public MagicAndMeleeAttackGoal(PathfinderMob mob, ProjectileSpell spell, double boost, boolean whenNotSeen) {
        this(mob, spell, boost, whenNotSeen, 10, 10);
    }

    public MagicAndMeleeAttackGoal(PathfinderMob mob, ProjectileSpell spell, double boost, boolean whenNotSeen, int maxCooldown, int variability) {
        super(mob, boost, whenNotSeen);
        this.spell = spell;
        this.mob = mob;
        this.maxCooldown = maxCooldown;
        this.variability = variability;
    }

    @Override
    public void start() {
        this.magicCooldown = 0;
        super.start();
    }

    protected void checkAndPerformAttack(LivingEntity attacked, double distance) {
        double d0 = this.getAttackReachSqr(attacked);
        if (this.getTicksUntilNextAttack() <= 0) {
            if (checkRangeCriteria(distance, d0)) {
                this.performMagicAttack(attacked);
                this.resetMagicCooldown();
            } else if (distance <= d0) {
                this.performMeleeAttack(attacked);
            }
            this.handleTimers();
        }
    }

    protected boolean checkRangeCriteria(double distance, double d0) {
        return distance >= d0 * 2.0D && this.magicCooldown <= 0;
    }

    protected void performMeleeAttack(LivingEntity attacked) {
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(attacked);
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
        magic.shoot(d0, d1, d2, 1.6F, (float) (this.variability - this.mob.level.getDifficulty().getId() * 3));
        mob.level.addFreshEntity(magic);
    }

    protected void handleTimers() {
        this.resetAttackCooldown();
        this.magicCooldown = Math.max(this.magicCooldown - 1, 0);
    }

    protected void resetMagicCooldown() {
        this.magicCooldown = maxCooldown;
    }
}