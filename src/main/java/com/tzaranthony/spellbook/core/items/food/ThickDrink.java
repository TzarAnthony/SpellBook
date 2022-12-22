package com.tzaranthony.spellbook.core.items.food;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class ThickDrink extends Drink {
    public ThickDrink(Properties properties) {
        this(properties, 40);
    }

    public ThickDrink(Properties properties, int consumptionTime) {
        super(properties, consumptionTime, false);
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }
}