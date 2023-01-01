package com.tzaranthony.spellbook.core.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

public class NearestNightTimeTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public NearestNightTimeTargetGoal(Mob mob, Class<T> target, boolean mustSee) {
        super(mob, target, mustSee);
    }

    public boolean canUse() {
        if (this.mob.level.dimension() == Level.OVERWORLD && this.mob.level.isDay()) {
            return false;
        }
        return super.canUse();
    }
}