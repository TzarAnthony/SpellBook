package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.tzaranthony.spellbook.core.entities.arrows.EffectCarryingArrow;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.ChanneledElement;
import com.tzaranthony.spellbook.core.items.equipment.equipUtils.ElementalTool;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ChanneledBow extends BowItem implements ElementalTool {
    public ChanneledBow(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainder) {
        if (user instanceof Player player) {
            ChanneledElement element = getElement(stack);
            boolean flag = (player.getAbilities().instabuild || canUseMP(player, element)) && element != ChanneledElement.NOTHING;

            int i = this.getUseDuration(stack) - remainder;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, flag);
            if (i < 0) return;

            if (canUseMP(player, element) || element != ChanneledElement.NOTHING) {
                float f = getPowerForTime(i);
                if (!((double) f < 0.1D)) {
                    if (!level.isClientSide) {
                        EffectCarryingArrow arrow = getArrow(element, level);
                        arrow.setPos(player.getX(), player.getEyeY() - (double)0.1F, player.getZ());
                        arrow.setOwner(player);
                        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            arrow.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (j > 0) {
                            arrow.setBaseDamage((arrow.getBaseDamage() * 4.0D) + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0) {
                            arrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            arrow.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, player, (p_40665_) -> {
                            p_40665_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        level.addFreshEntity(arrow);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        boolean flag = player.getAbilities().instabuild || canUseMP(player, getElement(itemstack));

        if (!level.isClientSide() && player.isShiftKeyDown()) {
            this.nextElement(itemstack);
            this.playChangeSound(level, player);
            if (!flag) {
                setElement(itemstack, ChanneledElement.NOTHING);
                level.playSound((Player) null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.8F, 1.0F);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            ChanneledElement element = getElement(itemstack);

            InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, level, player, hand, canUseMP(player, element) || element != ChanneledElement.NOTHING);
            if (ret != null) return ret;

            if (!flag || element == ChanneledElement.NOTHING) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                player.startUsingItem(hand);
                consumeMP(player, element);
                return InteractionResultHolder.consume(itemstack);
            }
        }
    }

    public EffectCarryingArrow getArrow(ChanneledElement element, Level level) {
        EffectCarryingArrow arrow;
        if (element == ChanneledElement.FIRE) {
            arrow = new EffectCarryingArrow(SBEntities.FIRE_ARROW.get(), level);
            arrow.setElement((byte) 1);
        } else if (element == ChanneledElement.WATER) {
            arrow = new EffectCarryingArrow(SBEntities.WATER_ARROW.get(), level);
            arrow.setElement((byte) 2);
        } else if (element == ChanneledElement.AIR) {
            arrow = new EffectCarryingArrow(SBEntities.AIR_ARROW.get(), level);
            arrow.setElement((byte) 3);
        } else {
            arrow = new EffectCarryingArrow(SBEntities.EARTH_ARROW.get(), level);
            arrow.setElement((byte) 4);
        }
        arrow.addEffect(element.getMagicEffects());
        return arrow;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ChanneledElement element = getElement(stack);
        tooltip.add((new TranslatableComponent("tooltip.spellbook.channeled_tool")).append(getElement(stack).getName()));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}