package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BlackHoleBE extends BlockEntity {
    private final double radius = 6.0D;
    private final double rSq = radius * radius;
    private final AABB box;
    private int lifespan = 5*20;

    public BlackHoleBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.BLACK_HOLE.get(), pos, state);
        this.box = new AABB(pos).inflate(radius);
    }

    public void setLifespan(int seconds) {
        this.lifespan = seconds * 20;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlackHoleBE bh) {
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, bh.box);
        if (!entities.isEmpty()) {
            for (Entity e : entities) {
                if ((e instanceof ItemEntity || e instanceof Mob) && e.position().distanceToSqr(bh.box.getCenter()) <= bh.rSq) {
                    e.setPos(bh.box.getCenter());
                }
            }
        }
        --bh.lifespan;
        if (bh.lifespan <= 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}