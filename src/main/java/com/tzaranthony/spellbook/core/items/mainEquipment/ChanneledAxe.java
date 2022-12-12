package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.ChanneledElement;
import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import com.tzaranthony.spellbook.core.util.events.SBToolUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
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

public class ChanneledAxe extends AxeItem {
    public SBToolMaterial material;
    public static final String ELEMENT = "ToolElement";
    public int MP = 10000;

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
        if (true) {
            canUseMP(user, element);
            ((ChanneledAxe) stack.getItem()).MP -= element.getMPUseValue();
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
                ((ChanneledAxe) stack.getItem()).MP -= element.getMPUseValue();
            }
        } else {
            setElement(stack, ChanneledElement.NOTHING);
            playChangeSound(user.level, user);
        }
        if (user instanceof ServerPlayer && isCorrectToolForDrops(stack, state)) {
//            SBToolUtils.areaMine((ServerLevel) level, (ServerPlayer) user, pos, stack, 1, 1);
//            SBToolUtils.areaMine((ServerLevel) level, (ServerPlayer) user, pos, 2, 2);
                SBToolUtils.veinMine((ServerLevel) level, (ServerPlayer) user, pos, level.getBlockState(pos).getBlock(), 49);
        }
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide() && player.isCrouching()) {
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

    public void nextElement(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (!compoundtag.contains(ELEMENT)) {
            compoundtag.putInt(ELEMENT, ChanneledElement.NOTHING.getId());
        }

        int id = compoundtag.getInt(ELEMENT);
        if (id == ChanneledElement.NOTHING.getId()) {
            setElement(stack, ChanneledElement.FIRE);
        } else if (id == ChanneledElement.FIRE.getId()) {
            setElement(stack, ChanneledElement.WATER);
        } else if (id == ChanneledElement.WATER.getId()) {
            setElement(stack, ChanneledElement.AIR);
        } else if (id == ChanneledElement.AIR.getId()) {
            setElement(stack, ChanneledElement.EARTH);
        } else if (id == ChanneledElement.EARTH.getId()) {
            setElement(stack, ChanneledElement.NOTHING);
        }
    }

    public void setElement(ItemStack stack, ChanneledElement element) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(ELEMENT, element.getId());
    }

    public ChanneledElement getElement(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (!compoundtag.contains(ELEMENT)) {
            compoundtag.putInt(ELEMENT, ChanneledElement.NOTHING.getId());
            return ChanneledElement.NOTHING;
        }

        int id = compoundtag.getInt(ELEMENT);
        if (id == ChanneledElement.FIRE.getId()) {
            return ChanneledElement.FIRE;
        } else if (id == ChanneledElement.WATER.getId()) {
            return ChanneledElement.WATER;
        } else if (id == ChanneledElement.AIR.getId()) {
            return ChanneledElement.AIR;
        } else if (id == ChanneledElement.EARTH.getId()) {
            return ChanneledElement.EARTH;
        }
        return ChanneledElement.NOTHING;
    }

    public boolean canUseMP(LivingEntity entity, ChanneledElement element) {
        if (this.MP > element.getMPUseValue()) {
            return true;
        }
        return false;
    }

    public void playChangeSound(Level level, LivingEntity entity) {
        level.playSound((Player) null, entity.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.8F, level.random.nextFloat() * 0.1F + 0.9F);
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
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }
            level.setBlock(blockpos, optional3.get(), 11);
            ChanneledElement element = getElement(itemstack);
            if (player != null && canUseMP(player, element)) {
                ((ChanneledAxe) itemstack.getItem()).MP -= element.getMPUseValue();
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}