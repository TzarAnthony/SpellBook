package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampirePerson;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class VampireHumanToBatTransformGoal extends Goal {
    private final HigherVampirePerson mob;
    private int transformerCooldown;

    public VampireHumanToBatTransformGoal(HigherVampirePerson vampireHuman) {
        this.mob = vampireHuman;
        this.transformerCooldown = 400;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
        vampireHuman.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getTarget() instanceof Player
                && (this.mob.hasEffect(MobEffects.LEVITATION) || this.mob.isInWater() || this.mob.distanceToSqr(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ()) > 3600.0D);
    }

    public void resetTransformerCooldown() {
        this.transformerCooldown = 400;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.transformerCooldown <= 0) {
            this.resetTransformerCooldown();
            this.mob.transform();
        }
        --this.transformerCooldown;
    }
}