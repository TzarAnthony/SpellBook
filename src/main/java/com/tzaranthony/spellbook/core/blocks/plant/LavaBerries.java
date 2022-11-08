package com.tzaranthony.spellbook.core.blocks.plant;

import com.tzaranthony.spellbook.core.util.tags.SBBlockTags;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class LavaBerries extends BushBlock implements IPlantable, SimpleWaterloggedBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape Young = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape Old = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    public static final BooleanProperty LAVALOGGED = BlockStateProperties.WATERLOGGED;

    public LavaBerries(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(LAVALOGGED, Boolean.valueOf(true)));
    }

    // for picking block
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        return new ItemStack(SBItems.INFERNUM_BERRIES.get());
    }

    // shape for age
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (state.getValue(AGE) == 0) {
            return Young;
        } else {
            return state.getValue(AGE) < 3 ? Old : super.getShape(state, getter, pos, context);
        }
    }

    // random ticks for growth 1/30 chance
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        int i = state.getValue(AGE);
        if (i < 3 && level.getRawBrightness(pos.above(), 0) >= 5
                && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((1 + (i / 3)) * 30) == 0)) {
            level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    // berry picking
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int i = state.getValue(AGE);
        if (i == 3) {
            popResource(level, pos, new ItemStack(SBItems.INFERNUM_BERRIES.get(), 1));
            level.playSound((Player) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(AGE, Integer.valueOf(1)), 2);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, handIn, hit);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LAVALOGGED);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        if (!getter.getFluidState(pos.above()).is(Fluids.LAVA)) {
            return state.is(Blocks.MAGMA_BLOCK);
        }
        return state.is(SBBlockTags.LAVA_BERRIES_BASE_BLOCKS);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        if (context.getLevel().getBlockState(blockpos.below()).is(Blocks.MAGMA_BLOCK)) {
            return this.defaultBlockState().setValue(LAVALOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.LAVA));
        }
        return fluidstate.is(FluidTags.LAVA) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(context) : null;
    }

    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
        BlockState blockstate = super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
        if (!blockstate.isAir() && state.getValue(LAVALOGGED)) {
            accessor.scheduleTick(currentPos, Fluids.LAVA, Fluids.LAVA.getTickDelay(accessor));
        }
        return blockstate;
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter p_56301_, BlockPos p_56302_, BlockState p_56303_, Fluid p_56304_) {
        return !p_56303_.getValue(BlockStateProperties.WATERLOGGED) && p_56304_ == Fluids.LAVA;
    }

    @Override
    public boolean placeLiquid(LevelAccessor p_56306_, BlockPos p_56307_, BlockState p_56308_, FluidState p_56309_) {
        if (!p_56308_.getValue(BlockStateProperties.WATERLOGGED) && p_56309_.getType() == Fluids.LAVA) {
            if (!p_56306_.isClientSide()) {
                p_56306_.setBlock(p_56307_, p_56308_.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
                p_56306_.scheduleTick(p_56307_, p_56309_.getType(), p_56309_.getType().getTickDelay(p_56306_));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor accessor, BlockPos pos, BlockState state) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            accessor.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)), 3);
            if (!accessor.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK) || !state.canSurvive(accessor, pos)) {
                accessor.destroyBlock(pos, true);
            }
            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.LAVA.getPickupSound();
    }
}