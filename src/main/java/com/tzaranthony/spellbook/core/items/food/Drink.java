package com.tzaranthony.spellbook.core.items.food;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class Drink extends Item {
    public boolean alwaysFoil;
    private final int consumptionTime;

    public Drink(Properties properties, boolean alwaysFoil) {
        this(properties, 32, alwaysFoil);
    }

    public Drink(Properties properties, int consumptionTime, boolean alwaysFoil) {
        super(properties);
        this.consumptionTime = consumptionTime;
        this.alwaysFoil = alwaysFoil;
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        super.finishUsingItem(stack, level, user);
        if (user instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) user;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof Player player && !player.getAbilities().invulnerable) {
                ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(itemstack)) {
                    player.drop(itemstack, false);
                }
            }
            return stack;
        }
    }

    public boolean isFoil(ItemStack stack) {
        return this.alwaysFoil;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return consumptionTime;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
}