package com.tzaranthony.spellbook.core.items.otherEquipment;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ResearchBook extends Item {
    public static final String RESEARCH_POINTS = "researchPoints";

    public ResearchBook(Properties properties) {
        super(properties);
    }

    public static int getColor(ItemStack stack) {
        switch (stack.getOrCreateTag().getInt(RESEARCH_POINTS)) {
            case 1:
                return PotionUtils.getColor(Potions.SLOWNESS);
            case 2:
                return PotionUtils.getColor(Potions.HARMING);
            case 3:
                return PotionUtils.getColor(Potions.STRENGTH);
            case 4:
                return PotionUtils.getColor(Potions.HEALING);
            case 5:
                return PotionUtils.getColor(Potions.FIRE_RESISTANCE);
            case 6:
                return PotionUtils.getColor(Potions.REGENERATION);
            case 7:
                return PotionUtils.getColor(Potions.NIGHT_VISION);
            case 8:
                return PotionUtils.getColor(Potions.SWIFTNESS);
            case 9:
                return PotionUtils.getColor(Potions.LUCK);
            case 10:
                return PotionUtils.getColor(Potions.LEAPING);
            default:
                return PotionUtils.getColor(Potions.SLOW_FALLING);
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            level.playSound((Player) null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL,
                    1.0F, level.random.nextFloat() * 0.1F + 0.9F);
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stackList) {
        if (this.allowdedIn(tab)) {
            for(int i = 1; i <= 10; i++) {
                stackList.add(assignResearchPoints(new ItemStack(this), i));
            }
        }
    }

    public static ItemStack assignResearchPoints(ItemStack stack, int points) {
        stack.getOrCreateTag().putInt(RESEARCH_POINTS, points);
        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.research_book")).append(String.valueOf(stack.getOrCreateTag().getInt(RESEARCH_POINTS))).append(" points."));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}