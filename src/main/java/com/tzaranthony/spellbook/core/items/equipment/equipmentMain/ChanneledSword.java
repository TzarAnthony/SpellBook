package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.tzaranthony.spellbook.core.items.equipment.equipUtils.ChanneledElement;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.ElementalTool;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.SBToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

public class ChanneledSword extends SwordItem implements ElementalTool {
    public SBToolMaterial material;

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
        super.appendHoverText(stack, level, tooltip, flag);
    }
}