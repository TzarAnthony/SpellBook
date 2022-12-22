package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.tzaranthony.spellbook.core.items.equipment.equipUtils.MiningModeTool;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.SBToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalHoe extends HoeItem implements MiningModeTool {
    protected static final String TOOL_MODE = "HarvestStyle";

    public CrystalHoe(SBToolMaterial tier, Properties properties) {
        super(tier, 10, -3.5F, properties);
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
        net.minecraft.world.item.ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            this.nextMiningMode(itemstack);
            this.playChangeSound(level, player);
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        getHoverDetails(stack, tooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }
}