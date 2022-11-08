package com.tzaranthony.spellbook.core.entities.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class FlyingGhostMovementHelper extends MoveControl {
    Mob entity;

    public FlyingGhostMovementHelper(Mob entity) {
        super(entity);
        this.entity = entity;
    }

    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            Vec3 vector3d = new Vec3(this.wantedX - this.entity.getX(), this.wantedY - this.entity.getY(), this.wantedZ - this.entity.getZ());
            double d0 = vector3d.length();
            if (d0 < this.entity.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().scale(0.5D));
            } else {
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                if (this.entity.getTarget() == null) {
                    Vec3 vector3d1 = this.entity.getDeltaMovement();
                    this.entity.setYRot(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI));
                    this.entity.yBodyRot = this.entity.getYRot();
                } else {
                    double d2 = this.entity.getTarget().getX() - this.entity.getX();
                    double d1 = this.entity.getTarget().getZ() - this.entity.getZ();
                    this.entity.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
                    this.entity.yBodyRot = this.entity.getYRot();
                }
            }
        }
    }
}