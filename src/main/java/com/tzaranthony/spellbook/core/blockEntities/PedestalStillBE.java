package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalStillBE extends PedestalBE {
    public PedestalStillBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.PEDESTAL_STILL.get(), pos, state);
    }
}