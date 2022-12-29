package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.SBGhostCommander;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.core.spells.Spell;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

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
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
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
            this.attackIntervalMax = 40;
            this.attackRadius = 20.0F;
            this.attackRadiusSqr = 20.0F * 20.0F;
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
        double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }

            float f = (float)Math.sqrt(d0) / this.attackRadius;
            checkCooldownsAndAttack();
            this.attackTime = Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax));
        }

    }

    private void checkCooldownsAndAttack() {
        if (this.target instanceof SBGhostCommander) {
            if ((this.target.getHealth() <= (this.target.getMaxHealth() * 0.75D))) {
//            if ((this.target.getHealth() / this.target.getMaxHealth()) <= (this.mob.getHealth() / this.mob.getMaxHealth())) {
//                this.target.heal(10.0F);
//                this.target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
//            } else {
//                this.mob.heal(10.0F);
//                this.mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
//            }
                this.target.heal(10.0F);
                this.target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
                this.mob.setTarget(null);

                this.mob.level.playSound((Player) null, this.mob.getX() , this.mob.getY() , this.mob.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
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