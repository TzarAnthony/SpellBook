package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.tzaranthony.spellbook.core.items.equipment.equipUtils.MiningModeTool;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.SBToolMaterial;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.UOC;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalShovel extends ShovelItem implements MiningModeTool {
    public CrystalShovel(SBToolMaterial tier, Properties properties) {
        super(tier, 1.5F, -3.0F, properties);
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, user, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });

            if (user instanceof ServerPlayer && isCorrectToolForDrops(stack, state)) {
                mineBlocks(stack, (ServerLevel) level, (ServerPlayer) user, pos);
            }
        }
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            this.nextMiningMode(itemstack);
            this.playChangeSound(level, player);
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = performUse(context.getLevel(), context.getClickedPos(), context);
        if (result.consumesAction()) {
            useOnBlocks(context.getItemInHand(), context.getLevel(), context, context.getClickedPos());
        }
        return result;
    }

    @Override
    public InteractionResult performUse(Level level, BlockPos pos, UseOnContext context) {
        UOC context1 = new UOC(context, pos);
        BlockState blockstate = level.getBlockState(pos);
        if (context1.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = context1.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(context1, net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN, false);
            BlockState blockstate2 = null;
            if (blockstate1 != null && level.isEmptyBlock(pos.above())) {
                level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide()) {
                    level.levelEvent((Player)null, 1009, pos, 0);
                }
                CampfireBlock.dowse(context1.getPlayer(), level, pos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }
            if (blockstate2 != null) {
                if (!level.isClientSide) {
                    level.setBlock(pos, blockstate2, 11);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        getHoverDetails(stack, tooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }
}