package com.tzaranthony.spellbook.core.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FlyingGhostMoveRandomGoal extends Goal {
    Mob entity;

    public FlyingGhostMoveRandomGoal(Mob entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return !this.entity.getMoveControl().hasWanted() && this.entity.getRandom().nextInt(7) == 0;
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void tick() {
        BlockPos blockpos = this.entity.blockPosition();
        for (int i = 0; i < 3; ++i) {
            BlockPos blockpos1 = blockpos.offset(this.entity.getRandom().nextInt(15) - 7, this.entity.getRandom().nextInt(11) - 5, this.entity.getRandom().nextInt(15) - 7);
            if (this.entity.level.isEmptyBlock(blockpos1)) {
                this.entity.getMoveControl().setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                if (this.entity.getTarget() == null) {
                    this.entity.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                }
                break;
            }
        }
    }
}