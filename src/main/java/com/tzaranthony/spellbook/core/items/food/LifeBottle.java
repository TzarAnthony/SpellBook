package com.tzaranthony.spellbook.core.items.food;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class LifeBottle extends ThickDrink {
    public LifeBottle() {
        super((new Item.Properties())
                .tab(SpellBook.TAB)
                .rarity(Rarity.EPIC)
                .craftRemainder(Items.GLASS_BOTTLE)
                .food((new FoodProperties.Builder())
                        .nutrition(6)
                        .saturationMod(1.5F)
                        .effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 3), 1.0F)
                        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 4), 1.0F)
                        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 1), 1.0F)
                        .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 1), 1.0F)
                        .effect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 1.0F)
                        .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 4), 1.0F)
                        .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 4), 1.0F)
                        .alwaysEat()
                        .build()
                )
        , 20);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        user.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.min(user.getAttributeValue(Attributes.MAX_HEALTH) + 4.0D, 100.0D));
        return super.finishUsingItem(stack, level, user);
    }
}