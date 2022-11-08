package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DimensionalBE extends TheEndPortalBlockEntity {
    public DimensionalBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.DIMENSIONAL_BE.get(), pos, state);
    }

    public boolean shouldRenderFace(Direction p_59959_) {
        return Block.shouldRenderFace(this.getBlockState(), this.level, this.getBlockPos(), p_59959_, this.getBlockPos().relative(p_59959_));
    }
}