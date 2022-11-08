package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampireBat;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;

public class VampireBatToHumanTransformGoal extends Goal {
    private final HigherVampireBat mob;
    private int transformerCooldown;

    public VampireBatToHumanTransformGoal(HigherVampireBat vampireBat) {
        this.mob = vampireBat;
        this.transformerCooldown = 400;
        this.setFlags(EnumSet.of(Flag.JUMP));
        vampireBat.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getTarget() instanceof Player && !isAboveWater()
                && !(this.mob.isInWater() || this.mob.distanceToSqr(this.mob.getTarget().getX(), this.mob.getTarget().getY(), this.mob.getTarget().getZ()) > 3600.0D);
    }

    public boolean isAboveWater() {
        for (int y = 0; y <= 5; ++y) {
            if (this.mob.level.getBlockState(this.mob.blockPosition().below(y)).getBlock() == Blocks.WATER) {
                return true;
            }
        }
        return false;
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