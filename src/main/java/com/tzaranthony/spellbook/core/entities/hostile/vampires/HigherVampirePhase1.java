package com.tzaranthony.spellbook.core.entities.hostile.vampires;

import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HigherVampirePhase1 extends SBVampireEntity {
    public HigherVampirePhase1(EntityType<? extends HigherVampirePhase1> highVamp, Level level) {
        super(highVamp, level);
        this.xpReward = 50;
        this.maxUpStep = 1.5F;
    }

    @Override
    public boolean checkSBInvulnerableTo(DamageSource source) {
        if (source instanceof SBDamageSource) {
            return ((SBDamageSource) source).isWaterDmg() || ((SBDamageSource) source).isAirDmg() || ((SBDamageSource) source).isPsychicDmg();
        }
        return source.isProjectile();
    }

    public boolean isResistantTo(DamageSource source) {
        return source.isMagic() || source == DamageSource.STARVE;
    }
}