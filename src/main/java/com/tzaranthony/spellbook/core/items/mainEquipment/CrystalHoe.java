package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalHoe extends HoeItem {
    public CrystalHoe(SBToolMaterial tier, Properties properties) {
        super(tier, 10, -3.5F, properties);
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, user, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });

            if (user instanceof ServerPlayer && isCorrectToolForDrops(stack, state)) {
//                SBToolUtils.areaMine((ServerLevel) level, (ServerPlayer) user, pos, stack, 1, 1);
//                SBToolUtils.areaMine((ServerLevel) level, (ServerPlayer) user, pos, stack, 2, 2);
//                SBToolUtils.veinMine((ServerLevel) level, (ServerPlayer) user, pos, level.getBlockState(pos).getBlock(), 49);
            }
        }
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.normal_mode")));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}