package com.tzaranthony.spellbook.core.items;

import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public enum ChanneledElement implements Elements{
    NOTHING("Nothing", 0, 0, null),

    FIRE("Fire", 1, 3, SBEffects.INCINERATION.get()),

    WATER("Water", 2, 1, SBEffects.FREEZING.get()),

    AIR("Air", 3, 3, MobEffects.LEVITATION),

    EARTH("Earth", 4, 2, SBEffects.FRACTURED.get());

    private final String name;
    private final int id;
    private final int MPUseValue;
    private final MobEffect effect;

    private ChanneledElement(String name, int id, int MPUseValue, MobEffect effect) {
        this.name = name;
        this.id = id;
        this.MPUseValue = MPUseValue;
        this.effect = effect;
    }

    @Override
    public int getMPUseValue() {
        return MPUseValue;
    }

    @Override
    public MobEffectInstance getMagicEffects() {
        return  new MobEffectInstance(effect, 400, 0);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}