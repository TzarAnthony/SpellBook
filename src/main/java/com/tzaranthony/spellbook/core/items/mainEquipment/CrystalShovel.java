package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import com.tzaranthony.spellbook.core.util.events.SBToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalShovel extends ShovelItem {
    public CrystalShovel(SBToolMaterial tier, Properties properties) {
        super(tier, 1.5F, -3.0F, properties);
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, user, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });

            if (user instanceof ServerPlayer && isCorrectToolForDrops(stack, state)) {
                SBToolUtils.areaMine((ServerLevel) level, (ServerPlayer) user, pos, stack, 1, 1);
//                SBToolUtils.areaMine((ServerLevel) level, (ServerPlayer) user, pos, stack, 2, 2);
//                SBToolUtils.veinMine((ServerLevel) level, (ServerPlayer) user, pos, level.getBlockState(pos).getBlock(), 49);
            }
        }
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.area_mode")).append("3x3"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}