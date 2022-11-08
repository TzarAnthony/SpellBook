package com.tzaranthony.spellbook.core.blocks.block;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.Random;

public class EnderCrystal extends AmethystClusterBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public EnderCrystal(int height, int length, SoundType sound, int light) {
        super(height, length, SBBlockProperties.Crystal(sound, light));
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        for(int i = 0; i < 3; ++i) {
            Direction direction = state.getValue(FACING);
            int j = rand.nextInt(2) * 2 - 1;
            int l = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;

            double d0 = (double) pos.getX() + rand.nextDouble();
            double d1 = (double) pos.getY() + rand.nextDouble();
            double d2 = (double) pos.getZ() + rand.nextDouble();
            double d3 = (double) (rand.nextFloat() * (float) j);
            double d4 = (double) (rand.nextFloat() * (float) l - 0.5);
            double d5 = (double) (rand.nextFloat() * (float) k);

            if (direction == Direction.NORTH) {
                d2 = (double) ((float) pos.getZ() + rand.nextFloat());
                d5 = ((double) rand.nextFloat() + 0.5D * 0.125D - 1);
            } else if (direction == Direction.SOUTH) {
                d2 = (double) ((float) pos.getZ() + rand.nextFloat());
                d5 = ((double) rand.nextFloat() - 0.5D * 0.125D + 0.5D);
            } else if (direction == Direction.EAST) {
                d0 = (double) ((float) pos.getX() + rand.nextFloat());
                d3 = ((double) rand.nextFloat() - 0.5D * 0.125D + 0.5D);
            } else if (direction == Direction.WEST) {
                d0 = (double) ((float) pos.getX() + rand.nextFloat());
                d3 = ((double) rand.nextFloat() + 0.5D * 0.125D - 1);
            } else if (direction == Direction.UP) {
                d1 = (double) ((float) pos.getY() + rand.nextFloat());
                d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
            } else if (direction == Direction.DOWN) {
                d1 = (double) ((float) pos.getY() + rand.nextFloat());
                d4 = ((double) rand.nextFloat() + 0.5D * 0.125D - 2);
            }

            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

//    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
//        for(int i = 0; i < 3; ++i) {
//            Direction direction = state.getValue(FACING);
//            int j = rand.nextInt(2) * 2 - 1;
//            int k = rand.nextInt(2) * 2 - 1;
//
//            double d0 = (double) pos.getX() + rand.nextDouble();
//            double d1 = (double) pos.getY() + rand.nextDouble();
//            double d2 = (double) pos.getZ() + rand.nextDouble();
//            double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
//            double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
//            double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
//
//            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
//                d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
//                d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
//            } else if (direction == Direction.EAST || direction == Direction.WEST) {
//                d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
//                d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
//            } else if (direction == Direction.UP || direction == Direction.DOWN) {
//                d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
//                d1 = (double) ((float) pos.getY() + rand.nextFloat());
//                d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
//                d3 = (double) (rand.nextFloat() * (float) j);
//                d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
//                d5 = (double) (rand.nextFloat() * (float) k);
//            }
//
//            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
//        }
//    }
}