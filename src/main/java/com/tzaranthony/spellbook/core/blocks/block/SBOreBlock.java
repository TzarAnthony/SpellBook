package com.tzaranthony.spellbook.core.blocks.block;

import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class SBOreBlock extends Block {
    public SBOreBlock(Properties properties) {
        super(properties);
    }

    protected int getExpAmt(Random rand) {
        if (this == SBBlocks.CINNABAR_ORE.get()) {
            return Mth.nextInt(rand, 1, 6);
        } else {
            return 0;
        }
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? this.getExpAmt(RANDOM) : 0;
    }
}