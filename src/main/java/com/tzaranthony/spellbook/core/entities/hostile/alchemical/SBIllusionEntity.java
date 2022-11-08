package com.tzaranthony.spellbook.core.entities.hostile.alchemical;

import com.tzaranthony.spellbook.core.entities.hostile.SBMobType;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class SBIllusionEntity extends Monster {
    protected SBIllusionEntity(EntityType<? extends Monster> entity, Level level) {
        super(entity, level);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.isProjectile()
                || source == DamageSource.FALL || source == DamageSource.IN_WALL || source == DamageSource.DROWN
                || source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL || this.checkLEInvulnerableTo(source);
    }

    public boolean checkLEInvulnerableTo(DamageSource source) {
        if (source instanceof SBDamageSource) {
            return ((SBDamageSource) source).isPsychicDmg();
        }
        return false;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != this.getTarget() && source.getEntity() != null) {
            amount = amount * 0.0F;
        }

        if (!super.hurt(source, amount)) {
            return false;
        } else if (!(this.level instanceof ServerLevel)) {
            return false;
        } else {
            return true;
        }
    }

    public MobType getMobType() {
        return SBMobType.PSYCHIC;
    }
}