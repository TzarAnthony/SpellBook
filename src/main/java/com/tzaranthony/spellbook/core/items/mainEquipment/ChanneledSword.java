package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.ChanneledElement;
import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ChanneledSword extends SwordItem {
    public SBToolMaterial material;
    public static final String ELEMENT = "ToolElement";
    public int MP = 10000;

    public ChanneledSword(SBToolMaterial tier, Properties properties) {
        super(tier, 3, -2.4F, properties);
        this.material = tier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        ChanneledElement element = getElement(stack);
        if (element == ChanneledElement.NOTHING) {
            return false;
        }
        if (canUseMP(user, element)) {
            ((ChanneledSword) stack.getItem()).MP -= element.getMPUseValue();
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
                ((ChanneledSword) stack.getItem()).MP -= element.getMPUseValue();
            }
        } else {
            setElement(stack, ChanneledElement.NOTHING);
            playChangeSound(user.level, user);
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
        super.appendHoverText(stack, level, tooltip, flag);
    }
}