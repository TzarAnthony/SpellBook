package com.tzaranthony.spellbook.core.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;

public interface Elements {
    int getMPUseValue();

    MobEffectInstance getMagicEffects();

    int getId();

    Component getName();
}