package com.tzaranthony.spellbook.core.blocks.spellBlocks;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Snare extends SpellBlock {
    public Snare() {
        super(SBBlockProperties.MagicBlock().noCollission());
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3((double) 0.01F, 0.01F, (double) 0.01F));
    }
}