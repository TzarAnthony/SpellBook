package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class HigherVampirePhase1 extends SBVampireEntity {
    public HigherVampirePhase1(EntityType<? extends HigherVampirePhase1> highVamp, Level level) {
        super(highVamp, level);
        this.xpReward = 50;
        this.maxUpStep = 1.5F;
    }

    public boolean isResistantTo(DamageSource source) {
        return source.isMagic() || source == DamageSource.STARVE;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.alertMinions((LivingEntity) source.getEntity());
        return super.hurt(source, amount);
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    private void alertMinions(LivingEntity attacker) {
        double d0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB axisalignedbb = AABB.unitCubeFromLowerCorner(this.position()).inflate(d0, 10.0D, d0);
        this.level.getEntitiesOfClass(LesserVampire.class, axisalignedbb).stream()
                .filter((subjectC1) -> {
                    return subjectC1.getTarget() == null;
                }).forEach((subjectC3) -> {
                    subjectC3.setTarget(attacker);
                });
    }
}