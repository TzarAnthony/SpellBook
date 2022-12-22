package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.core.blockEntities.RiftGeneratorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RiftGenerator extends TickingBEBlock {
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;

    public RiftGenerator(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, Boolean.valueOf(false)).setValue(HAS_BOOK, Boolean.valueOf(false)));
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RiftGeneratorBE(pos, state);
    }

    //TODO: maybe add multiblock
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            ItemStack stack = player.getItemInHand(hand);
            if (blockentity instanceof RiftGeneratorBE riftGen) {
                boolean changedItem = riftGen.changeItem(player, stack);
                if (changedItem) {
                    handlePortals(level, pos, state, !changedItem, !riftGen.getItem().isEmpty(), riftGen);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public void neighborChanged(BlockState state, Level level, BlockPos thisPos, Block block, BlockPos otherPos, boolean changed) {
        if (!level.isClientSide()) {
            BlockEntity blockentity = level.getBlockEntity(thisPos);
            if (blockentity instanceof RiftGeneratorBE riftGen) {
                handlePortals(level, thisPos, state, false, !riftGen.getItem().isEmpty(), riftGen);
            }
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state1, boolean opt) {
        if (!state.is(state1.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof RiftGeneratorBE riftGen) {
                handlePortals(level, pos, state, false, false, riftGen);
                Containers.dropContents(level, pos, riftGen.getItems());
            }
            super.onRemove(state, level, pos, state1, opt);
        }
    }
    
    protected void handlePortals(Level level, BlockPos pos, BlockState state, boolean wasClicked, boolean hasBook, RiftGeneratorBE riftGen) {
        boolean alreadyHasPortal = state.getValue(ENABLED);
        boolean alreadyHasBook = state.getValue(HAS_BOOK);
        boolean hasRedstoneSignal = hasValidRedstoneSignal(level, pos);

        if (!hasBook && !alreadyHasBook) {
            return;
        } else if (!hasBook) {
            level.blockEvent(pos, this, 0, 0);
        } else if (!hasRedstoneSignal && (!wasClicked || alreadyHasPortal)) {
            level.blockEvent(pos, this, 1, 0);
        } else if (!alreadyHasPortal) {
            level.blockEvent(pos, this, 1, 1);
        }
    }
    
    protected boolean hasValidRedstoneSignal(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (dir != Direction.UP && level.hasNeighborSignal(pos.relative(dir))) {
                return true;
            }
        }
        return false;
    }

    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int data, int data1) {
        boolean book = data == 1;
        boolean active = data1 == 1;
        if (active == state.getValue(ENABLED)) {
            return false;
        }

        level.setBlock(pos, state.setValue(HAS_BOOK, Boolean.valueOf(book)).setValue(ENABLED, Boolean.valueOf(active)), 2);
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof RiftGeneratorBE riftGen) {
            riftGen.updatePortals(active);
            return true;
        }
        return false;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(ENABLED, HAS_BOOK);
    }
}