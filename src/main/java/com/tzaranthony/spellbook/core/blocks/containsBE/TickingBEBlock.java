package com.tzaranthony.spellbook.core.blocks.containsBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class TickingBEBlock extends Block implements EntityBlock {
    protected TickingBEBlock(BlockBehaviour.Properties p_49224_) {
        super(p_49224_);
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int event, int event1) {
        super.triggerEvent(state, level, pos, event, event1);
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity == null ? false : blockentity.triggerEvent(event, event1);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }
}