package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.SBGhostCommander;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.core.spells.Spell;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class MagicMultiAttackGoal extends Goal {
    protected final Mob mob;
    protected final NonNullList<Spell> spells;
    protected final NonNullList<Integer> maxCooldowns;
    protected final NonNullList<Integer> currentCooldowns;
    protected final NonNullList<Integer> useProbs;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private final double attackRadiusSqr;
    @Nullable
    private LivingEntity target;

    public MagicMultiAttackGoal(Mob mob, NonNullList<Spell> spell, NonNullList<Integer> maxCooldowns, NonNullList<Integer> useProbs) {
        if (spell.size() != maxCooldowns.size() || spell.size() != useProbs.size()) {
            throw new IllegalArgumentException("MagicMultiAttackGoal requires Spell list, Cooldown list, and Probability list sizes to match");
        } else {
            this.mob = mob;
            this.spells = spell;
            this.maxCooldowns = maxCooldowns;
            this.currentCooldowns = NonNullList.withSize(maxCooldowns.size(), 0);
            this.useProbs = useProbs;
            this.speedModifier = 1.10D;
            this.attackIntervalMin = 40;
            this.attackRadiusSqr = 20.0D * 20.0D;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    }

    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canUse() || !this.mob.getNavigation().isDone();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }
            checkCooldownsAndAttack();
            this.attackTime = this.attackIntervalMin;
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / Math.sqrt(this.attackRadiusSqr), this.attackIntervalMin, this.attackIntervalMin));
        }
        }
    }

    private void checkCooldownsAndAttack() {
        if (this.mob.getRandom().nextInt(10) <= 0) {
            List<LivingEntity> gcList = this.mob.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.mob.blockPosition()).inflate(20));
            for (LivingEntity le : gcList) {
                if (this.mob.isAlliedTo(le) && (le.getHealth() <= (le.getMaxHealth() * 0.75D))) {
                    le.heal(10.0F);
                    le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200));
                }
            }
            this.mob.setTarget(null);
            this.mob.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
        } else {
            boolean performedSpell = false;
            int prob = this.mob.level.random.nextInt(100);
            for (int i = 0; i < this.currentCooldowns.size(); ++i) {
                int cooldown = this.currentCooldowns.get(i) - 1;
                Spell spell = this.spells.get(i);
                if (!performedSpell && cooldown <= 0 && (prob <= this.useProbs.get(i))) {
                    if (spell instanceof ProjectileSpell pSpell) {
                        performProjectileMagic(this.target, pSpell);
                    } else {
                        spell.perform_spell(this.mob.level, this.mob, InteractionHand.MAIN_HAND, this.mob.blockPosition());
                    }
                    performedSpell = true;
                    resetCooldown(i);
                }
                this.currentCooldowns.set(i, Math.max(cooldown, 0));
            }
        }
    }

    protected void performProjectileMagic(LivingEntity attacked, ProjectileSpell spell) {
        this.mob.getLookControl().setLookAt(attacked.position());
        MagicProjectile magic = new MagicProjectile(spell.getMagicProjectile(), this.mob.level);
        magic.setOwner(this.mob);
        magic.setPos(this.mob.getX(), this.mob.getEyeY() - (double)0.15F, this.mob.getZ());
        magic.setSpell(spell.getId());
        spell.addSpellDataToProjectile(magic);
        magic.setIgnoreType(SBGhostCommander.class);
        double d0 = attacked.getX() - this.mob.getX();
        double d1 = attacked.getEyeY() - magic.getY();
        double d2 = attacked.getZ() - this.mob.getZ();
        magic.shoot(d0, d1, d2, 1.6F, (float) (5 - this.mob.level.getDifficulty().getId() * 3));
        mob.level.addFreshEntity(magic);
    }

    protected void resetCooldown(int i) {
        this.currentCooldowns.set(i, this.maxCooldowns.get(i));
    }
}