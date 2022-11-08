package com.tzaranthony.spellbook.core.entities.hostile;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class SBMonsterEntity extends Monster {
    public int spellCastingTickCount;

    protected SBMonsterEntity(EntityType<? extends SBMonsterEntity> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isVulnerableTo(DamageSource source) {
        return false;
    }

    public boolean isResistantTo(DamageSource source) {
        return false;
    }


    @Override
    public void baseTick() {
        if (this.isPassenger()) {
            this.stopRiding();
        }
        super.baseTick();
    }

    public boolean isCastingSpell() {
        return false;
    }

    public void setIsCastingSpell() {
        return;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isVulnerableTo(source)) {
            amount = amount * 2.0F;
        }
        if (this.isResistantTo(source)) {
            amount = amount * 0.6F;
        }

        return super.hurt(source, amount);
    }
}