package com.tzaranthony.spellbook.core.items.equipment.equipmentOther;

import com.tzaranthony.spellbook.core.items.SBItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class XPBook extends Item {
    public static final String MODE = "XPTransferMode";
    public static final String STORAGE = "XPStored";
    public static final int maxXP = 10284;

    public XPBook() {
        super(SBItemProperties.Standard(Rarity.RARE, 1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            if (player.isShiftKeyDown() && stack.hasTag()) {
                int currentXp = stack.getTag().getInt(STORAGE);
                if (currentXp >= 1395) {
                    stack.getTag().putInt(STORAGE, currentXp - 1395);
                    player.giveExperiencePoints(1395);
                }
            } else {
                changeMode(stack);
                level.playSound(player, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
            }
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
        if (entity instanceof Player player && stack.hasTag()) {
            int mode = stack.getTag().getInt(MODE);
            int currentXp = stack.getTag().getInt(STORAGE);
            if (mode == 1 && player.experienceLevel > 0 && currentXp < maxXP) {
                int xpToTransfer = Math.min(maxXP - currentXp, getXpNeededForNextLevel(player.experienceLevel - 1));
                stack.getTag().putInt(STORAGE, currentXp + xpToTransfer);
                player.giveExperiencePoints(-xpToTransfer);
            } else if (mode == 3 && currentXp > 0) {
                int xpToTransfer =  Math.min(currentXp, getXpNeededForNextLevel(player.experienceLevel));
                stack.getTag().putInt(STORAGE, currentXp - xpToTransfer);
                player.giveExperiencePoints(xpToTransfer);
            }
        }
    }

    public int getXpNeededForNextLevel(int lvl) {
        if (lvl >= 30) {
            return 112 + (lvl - 30) * 9;
        } else {
            return lvl >= 15 ? 37 + (lvl - 15) * 5 : 7 + lvl * 2;
        }
    }

    public int calculateXp(int xp) {
        if (xp > 1507) {
            return (int) ((325.0D/18.0D) + Math.sqrt((2.0D/9.0D) * ((double) xp - (54215.0D/72.0D))));
        } else if (xp > 352) {
            return (int) ((81.0D/10.0D) + Math.sqrt((2.0D/5.0D) * ((double) xp - (7839.0D/40.0D))));
        }
        return (int) Math.sqrt(xp + 9) - 3;
    }

    public void changeMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MODE)) {
            tag.putInt(MODE, 1);
        } else {
            int mode = tag.getInt(MODE);
            ++mode;
            if (mode > 3) {
                mode = 0;
            }
            tag.putInt(MODE, mode);
        }
    }

    public String getModeName(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.getInt(MODE) == 1) {
            return "Absorption";
        } else if (compoundtag.getInt(MODE) == 3) {
            return "Release";
        }
        return "Off";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.xp_mode")).append(getModeName(stack)));
        int xp = stack.getOrCreateTag().getInt(STORAGE);
        tooltip.add(new TextComponent(String.valueOf(xp)).append(new TranslatableComponent("tooltip.spellbook.xp_stored"))
                .append(new TextComponent(String.valueOf(calculateXp(xp))).append(new TranslatableComponent("tooltip.spellbook.lvl_stored"))));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}