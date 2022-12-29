package com.tzaranthony.spellbook.core.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class MoveToEntityGoal extends MoveToBlockGoal {
    protected final Class<? extends Entity> targetType;
    protected Entity etarget;
    private final int searchRange;
    private final int verticalSearchRange;
    private int ticksSinceReachedGoal;

    public MoveToEntityGoal(PathfinderMob mob, Class<? extends Entity> entity, double speed) {
        super(mob, speed, 24, 6);
        this.targetType = entity;
        this.searchRange = 24;
        this.verticalSearchRange = 6;
    }


    public boolean canContinueToUse() {
        return this.mob.getTarget() != null ? false : super.canContinueToUse();
    }

//    public boolean canUse() {
//        if (this.nextStartTick > 0) {
//            --this.nextStartTick;
//            return false;
//        } else if (this.tryFindBlock()) {
//            this.nextStartTick = reducedTickDelay(20);
//            return true;
//        } else {
//            this.nextStartTick = this.nextStartTick(this.mob);
//            return false;
//        }
//    }
//
//    private boolean tryFindBlock() {
//        return this.mob.getTarget() == null && this.isValidTarget(this.mob.level, this.blockPos) ? true : this.findNearestBlock();
//    }

    @Override
    protected boolean findNearestBlock() {
        if (this.mob.getTarget() == null) {
            return false;
        } else if (this.blockPos != null && this.etarget != null && !this.etarget.isRemoved()) {
            return isValidTarget(this.mob.level, this.blockPos);
        } else {
            this.etarget = this.getNearestEntity(this.mob.level.getEntitiesOfClass(this.targetType, this.getTargetSearchArea()), this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            this.blockPos = this.etarget.blockPosition();
            return true;
        }
    }

    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        ChunkAccess chunkaccess = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        return chunkaccess == null ? false : true;
    }

    protected AABB getTargetSearchArea() {
        return this.mob.getBoundingBox().inflate(this.searchRange, this.verticalSearchRange, this.searchRange);
    }

    protected <T extends Entity> T getNearestEntity(List<? extends T> entities, double x, double y, double z) {
        double d0 = -1.0D;
        T t = null;

        for(T t1 : entities) {
            double d1 = t1.distanceToSqr(x, y, z);
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }
}