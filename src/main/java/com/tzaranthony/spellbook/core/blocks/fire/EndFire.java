package com.tzaranthony.spellbook.core.blocks.fire;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public class EndFire extends BaseFireBlock {
    public EndFire( MaterialColor color, int light) {
        super(SBBlockProperties.Fire(color, light), 3.0f);
    }

    public BlockState updateShape(BlockState state, Direction dir, BlockState state2, LevelAccessor accessor, BlockPos curPos, BlockPos placePos) {
        return this.canSurvive(state, accessor, curPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return canSurviveOnBlock(reader.getBlockState(pos.below()).getBlock());
    }

    public static boolean canSurviveOnBlock(Block block) {
        return block == SBBlocks.INFUSED_END_STONE.get();
    }

    protected boolean canBurn(BlockState state) {
        return true;
    }
}