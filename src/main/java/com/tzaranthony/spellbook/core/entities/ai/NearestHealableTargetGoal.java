package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.SBMonsterEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class NearestHealableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private static final int DEFAULT_COOLDOWN = 200;
    private int cooldown = 0;

    public NearestHealableTargetGoal(SBMonsterEntity monster, Class<T> target, boolean mustSee, @Nullable Predicate<LivingEntity> targeting) {
        super(monster, target, 500, mustSee, false, targeting);
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void decrementCooldown() {
        --this.cooldown;
    }

    public boolean canUse() {
        if (this.cooldown <= 0 && this.mob.getRandom().nextBoolean()) {
            this.findTarget();
            return this.target != null;
        } else {
            return false;
        }
    }

    public void start() {
        this.cooldown = reducedTickDelay(200);
        super.start();
    }
}