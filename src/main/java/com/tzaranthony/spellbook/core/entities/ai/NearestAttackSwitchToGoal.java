package com.tzaranthony.spellbook.core.entities.ai;

import com.tzaranthony.spellbook.core.entities.hostile.SBMonsterEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class NearestAttackSwitchToGoal <T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private boolean canAttack = true;

    public NearestAttackSwitchToGoal(SBMonsterEntity monster, Class<T> target, int cooldown, boolean mustSee, boolean canReach, @Nullable Predicate<LivingEntity> targeter) {
        super(monster, target, cooldown, mustSee, canReach, targeter);
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public boolean canUse() {
        return this.canAttack && super.canUse();
    }
}