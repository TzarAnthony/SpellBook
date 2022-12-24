package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Optional;

//TODO: fix campfire
public class SBCampfire extends CampfireBlock {
    public SBCampfire(int dmg, Properties properties) {
        super(false, dmg, properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CampfireBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof CampfireBlockEntity campfireblockentity) {
            ItemStack itemstack = player.getItemInHand(hand);
            Optional<CampfireCookingRecipe> optional = campfireblockentity.getCookableRecipe(itemstack);
            if (optional.isPresent()) {
                if (!level.isClientSide && campfireblockentity.placeFood(player.getAbilities().instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state1, boolean removed) {
        if (!state.is(state1.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof CampfireBlockEntity) {
                Containers.dropContents(level, pos, ((CampfireBlockEntity)blockentity).getItems());
            }

            super.onRemove(state, level, pos, state1, removed);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        if (p_152755_.isClientSide) {
            return p_152756_.getValue(LIT) ? createTickerHelper(p_152757_, SBBlockEntities.END_CAMPFIRE.get(), CampfireBlockEntity::particleTick) : null;
        } else {
            return p_152756_.getValue(LIT) ? createTickerHelper(p_152757_, SBBlockEntities.END_CAMPFIRE.get(), CampfireBlockEntity::cookTick) : createTickerHelper(p_152757_, SBBlockEntities.END_CAMPFIRE.get(), CampfireBlockEntity::cooldownTick);
        }
    }
}