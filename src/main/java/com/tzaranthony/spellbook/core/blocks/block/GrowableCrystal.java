package com.tzaranthony.spellbook.core.blocks.block;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrowableCrystal extends AmethystClusterBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public GrowableCrystal(int height, int length, SoundType sound, int light) {
        super(height, length, SBBlockProperties.Crystal(sound, light));
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        BlockState blockstate = level.getBlockState(pos);
        if (rand.nextInt(5) == 0 && canClusterGrowAtState(level, pos)) {
            Block block = null;
            if (blockstate.is(SBBlocks.CRYSTALAN_BUD_SMALL.get())) {
                block = SBBlocks.CRYSTALAN_BUD_MEDIUM.get();
            } else if (blockstate.is(SBBlocks.CRYSTALAN_BUD_MEDIUM.get())) {
                block = SBBlocks.CRYSTALAN_BUD_LARGE.get();
            } else if (blockstate.is(SBBlocks.CRYSTALAN_BUD_LARGE.get())) {
                block = SBBlocks.CRYSTALAN_CLUSTER.get();
            }

            Direction direction = state.getValue(GrowableCrystal.FACING);

            if (block != null) {
                BlockState blockstate1 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                level.setBlockAndUpdate(pos, blockstate1);
            }

            if (block == SBBlocks.CRYSTALAN_CLUSTER.get()) {
                finalizeClusterGrowth(level, pos);
            }
        }
    }

    public static void finalizeClusterGrowth(ServerLevel level, BlockPos pos) {
        BlockState crystalState = level.getBlockState(pos);
        Direction direction = crystalState.getValue(FACING);
        Block block = SBBlocks.CRYSTALAN_CLUSTER.get();

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            BlockState blockstate2 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.above(), blockstate2);
            BlockState blockstate3 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.east(), blockstate3);
            BlockState blockstate4 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.below(), blockstate4);
            BlockState blockstate5 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.west(), blockstate5);

        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            BlockState blockstate2 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.north(), blockstate2);
            BlockState blockstate3 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.above(), blockstate3);
            BlockState blockstate4 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.south(), blockstate4);
            BlockState blockstate5 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.below(), blockstate5);

        } else if (direction == Direction.UP || direction == Direction.DOWN) {
            BlockState blockstate2 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.north(), blockstate2);
            BlockState blockstate3 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.east(), blockstate3);
            BlockState blockstate4 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.south(), blockstate4);
            BlockState blockstate5 = block.defaultBlockState().setValue(GrowableCrystal.FACING, direction).setValue(GrowableCrystal.WATERLOGGED, false);
            level.setBlockAndUpdate(pos.west(), blockstate5);
        }
    }
    
    public static boolean canClusterGrowAtState(ServerLevel level, BlockPos pos) {
        BlockState crystalState = level.getBlockState(pos);
        Direction direction = crystalState.getValue(FACING);

        List<Integer> list = new ArrayList<Integer>(4);

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.above()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.east()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.below()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.west()).getBlock()));
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.north()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.above()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.south()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.below()).getBlock()));
        } else if (direction == Direction.UP || direction == Direction.DOWN) {
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.north()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.east()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.south()).getBlock()));
            list.add(isCrystalGrowingBlock(level.getBlockState(pos.west()).getBlock()));
        }

        return crystalState.getValue(GrowableCrystal.WATERLOGGED) == true && list.contains(1) && list.contains(2) && list.contains(3) && list.contains(4);
    }

    public static int isCrystalGrowingBlock(Block block) {
        if (block == Blocks.DIAMOND_BLOCK) {
            return 1;
        } else if (block == Blocks.EMERALD_BLOCK) {
            return 2;
        } else if (block == Blocks.AMETHYST_BLOCK) {
            return 3;
        } else if (block == Blocks.QUARTZ_BLOCK) {
            return 4;
        }
        return 0;
    }
}