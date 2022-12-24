package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.tzaranthony.spellbook.core.items.equipment.equipUtils.ChanneledElement;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.ElementalTool;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.SBToolMaterial;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ChanneledAxe extends AxeItem implements ElementalTool {
    public SBToolMaterial material;

    public ChanneledAxe(SBToolMaterial tier, Properties properties) {
        super(tier, 6, -3.0F, properties);
        this.material = tier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        ChanneledElement element = getElement(stack);
        if (element == ChanneledElement.NOTHING) {
            return false;
        }
        if (canUseMP(user, element)) {
            consumeMP(user, element);
            target.addEffect(element.getMagicEffects());
            return true;
        } else {
            setElement(stack, ChanneledElement.NOTHING);
            playChangeSound(user.level, user);
            return false;
        }
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        ChanneledElement element = getElement(stack);
        if (canUseMP(user, element)) {
            if (state.getDestroySpeed(level, pos) != 0.0F) {
                consumeMP(user, element);
            }
        } else {
            setElement(stack, ChanneledElement.NOTHING);
            playChangeSound(user.level, user);
        }
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            this.nextElement(itemstack);
            this.playChangeSound(level, player);
            if (!canUseMP(player, getElement(itemstack))) {
                setElement(itemstack, ChanneledElement.NOTHING);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ChanneledElement element = getElement(stack);
        tooltip.add((new TranslatableComponent("tooltip.spellbook.channeled_tool")).append(getElement(stack).getName()));
        tooltip.add((new TranslatableComponent("tooltip.spellbook.vein_mode")));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_40529_) {
        Level level = p_40529_.getLevel();
        BlockPos blockpos = p_40529_.getClickedPos();
        Player player = p_40529_.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(p_40529_, net.minecraftforge.common.ToolActions.AXE_STRIP, false));
        Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(p_40529_, net.minecraftforge.common.ToolActions.AXE_SCRAPE, false));
        Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(p_40529_, net.minecraftforge.common.ToolActions.AXE_WAX_OFF, false));
        ItemStack itemstack = p_40529_.getItemInHand();
        Optional<BlockState> optional3 = Optional.empty();
        if (optional.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            optional3 = optional;
        } else if (optional1.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockpos, 0);
            optional3 = optional1;
        } else if (optional2.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, blockpos, 0);
            optional3 = optional2;
        }

        if (optional3.isPresent()) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
            }
            level.setBlock(blockpos, optional3.get(), 11);
            ChanneledElement element = getElement(itemstack);
            if (player != null && canUseMP(player, element)) {
                consumeMP(player, element);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}