package com.tzaranthony.spellbook.core.blocks.spellBlocks;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class Snare extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public Snare() {
        super(SBBlockProperties.MagicBlock().noCollission());
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
    }

    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3((double) 0.01F, 0.01F, (double) 0.01F));
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        int i = state.getValue(AGE);
        if (i < 2) {
            level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    public void setLifespan(int lifespan, BlockState state) {
        state.setValue(AGE, Integer.valueOf(lifespan));
    }
}