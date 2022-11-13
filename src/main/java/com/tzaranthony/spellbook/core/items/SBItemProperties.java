package com.tzaranthony.spellbook.core.items;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class SBItemProperties {
    public static Item.Properties Standard() {
        return new Item.Properties().tab(SpellBook.TAB);
    }

    public static Item.Properties Standard(Rarity rare) {
        return new Item.Properties().tab(SpellBook.TAB).rarity(rare);
    }

    public static Item.Properties Standard(int stackSize) {
        return new Item.Properties().tab(SpellBook.TAB).stacksTo(stackSize);
    }

    public static Item.Properties Standard(Rarity rare, int stackSize) {
        return new Item.Properties().tab(SpellBook.TAB).rarity(rare).stacksTo(stackSize);
    }

    public static Item.Properties Unburnable() {
        return new Item.Properties().tab(SpellBook.TAB).fireResistant();
    }

    public static Item.Properties Unburnable(Rarity rare) {
        return new Item.Properties().tab(SpellBook.TAB).fireResistant().rarity(rare);
    }

    public static Item.Properties Unburnable(int stackSize) {
        return new Item.Properties().tab(SpellBook.TAB).fireResistant().stacksTo(stackSize);
    }

    public static Item.Properties Unburnable(Rarity rare, int stackSize) {
        return new Item.Properties().tab(SpellBook.TAB).fireResistant().rarity(rare).stacksTo(stackSize);
    }

    public static Item.Properties SpellDebug() {
        return new Item.Properties().tab(SpellBook.SPELL_TAB).rarity(Rarity.EPIC).stacksTo(1);
    }

    public static Item.Properties Berries(MobEffect primaryEffect, int amplifier1, MobEffect secondaryEffect, int amplifier2) {
        return new Item.Properties().tab(SpellBook.TAB).rarity(Rarity.UNCOMMON).food(
                (new FoodProperties.Builder())
                        .nutrition(2)
                        .saturationMod(1.2f)
                        .effect(new MobEffectInstance(secondaryEffect, 400, amplifier2), 1.0F)
                        .effect(new MobEffectInstance(primaryEffect, 600, amplifier1), 1.0F)
                        .alwaysEat()
                        .fast()
                        .build()
        );
    }

    public static Item.Properties Berries(MobEffect primaryEffect, MobEffect secondaryEffect) {
        return Berries(primaryEffect, 0, secondaryEffect, 0);
    }

    public static Item.Properties UnburnableBerries(MobEffect primaryEffect, MobEffect secondaryEffect) {
        return Berries(primaryEffect, secondaryEffect).fireResistant();
    }

    public static Item.Properties VampireBlood(Rarity rare, int hunger, int potency, float probability, int negPotency, float negProbability) {
        return new Item.Properties().tab(SpellBook.TAB).rarity(rare).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(
                (new FoodProperties.Builder())
                        .nutrition(hunger)
                        .saturationMod(0.1F)
                        .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, potency), probability)
                        .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, potency), probability)
                        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, potency), probability)
                        .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, potency), probability)
                        .effect(new MobEffectInstance(MobEffects.POISON, 600, negPotency), negProbability)
                        .alwaysEat()
                        .build()
        );
    }

    public static Item.Properties Smoothie() {
        return new Item.Properties().tab(SpellBook.TAB).rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE).food(
                (new FoodProperties.Builder())
                        .nutrition(6)
                        .saturationMod(1.4f)
                        .effect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 0), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 0), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 800, 1), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 1), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 0.5F)
                        .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 0.5F)
                        .effect(() -> new MobEffectInstance(SBEffects.FREEZING_ANIMATION.get(), 600, 1), 0.10F)
                        .alwaysEat()
                        .build()
        );
    }
}