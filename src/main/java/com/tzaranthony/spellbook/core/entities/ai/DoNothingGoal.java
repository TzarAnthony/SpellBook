package com.tzaranthony.spellbook.core.entities.ai;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DoNothingGoal extends Goal {
    private WaitingEntity waiter;

    public DoNothingGoal(WaitingEntity waiter) {
        this.waiter = waiter;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return waiter.shouldWait();
    }
}