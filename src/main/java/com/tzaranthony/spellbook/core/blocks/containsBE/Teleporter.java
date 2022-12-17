package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.core.blockEntities.AlterBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Teleporter extends TickingBEBlock {
    public Teleporter(Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlterBE(pos, state);
    }
}