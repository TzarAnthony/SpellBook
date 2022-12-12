package com.tzaranthony.spellbook.core.items;

import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public enum ChanneledElement implements Elements{
    NOTHING(new TranslatableComponent("tooltip.spellbook.no_element"), 0, 0, null),

    FIRE(new TranslatableComponent("tooltip.spellbook.fire"), 1, 3, SBEffects.INCINERATION.get()),

    WATER(new TranslatableComponent("tooltip.spellbook.water"), 2, 1, SBEffects.FREEZING.get()),

    AIR(new TranslatableComponent("tooltip.spellbook.air"), 3, 3, MobEffects.LEVITATION),

    EARTH(new TranslatableComponent("tooltip.spellbook.earth"), 4, 2, SBEffects.FRACTURED.get());

    private final TranslatableComponent name;
    private final int id;
    private final int MPUseValue;
    private final MobEffect effect;

    private ChanneledElement(TranslatableComponent name, int id, int MPUseValue, MobEffect effect) {
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
    public Component getName() {
        return name;
    }
}