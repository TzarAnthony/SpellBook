package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBPotions {
    public static final DeferredRegister<Potion> reg = DeferredRegister.create(ForgeRegistries.POTIONS, SpellBook.MOD_ID);

    public static final RegistryObject<Potion> INCINERATION = reg.register("incineration", () -> new Potion(new MobEffectInstance(SBEffects.INCINERATION.get(), 1800)));
    public static final RegistryObject<Potion> LONG_INCINERATION = reg.register("long_incineration", () -> new Potion("incineration", new MobEffectInstance(SBEffects.INCINERATION.get(), 4800)));

    public static final RegistryObject<Potion> FREEZING = reg.register("freezing", () -> new Potion(new MobEffectInstance(SBEffects.FREEZING.get(), 1800)));
    public static final RegistryObject<Potion> LONG_FREEZING = reg.register("long_freezing", () -> new Potion("freezing", new MobEffectInstance(SBEffects.FREEZING.get(), 4800)));

    public static final RegistryObject<Potion> FRACTURED = reg.register("fracturing", () -> new Potion(new MobEffectInstance(SBEffects.FRACTURED.get(), 1800)));
    public static final RegistryObject<Potion> LONG_FRACTURED = reg.register("long_fracturing", () -> new Potion("fracturing", new MobEffectInstance(SBEffects.FRACTURED.get(), 4800)));

    public static final RegistryObject<Potion> LEVITATION = reg.register("levitation", () -> new Potion(new MobEffectInstance(MobEffects.LEVITATION, 1800)));
    public static final RegistryObject<Potion> LONG_LEVITATION = reg.register("long_levitation", () -> new Potion("levitation", new MobEffectInstance(MobEffects.LEVITATION, 4800)));

    public static final RegistryObject<Potion> CLUMSY = reg.register("clumsy", () -> new Potion(new MobEffectInstance(SBEffects.CLUMSY.get(), 1800)));
    public static final RegistryObject<Potion> STRONG_CLUMSY = reg.register("strong_clumsy", () -> new Potion("clumsy", new MobEffectInstance(SBEffects.CLUMSY.get(), 400, 1)));

    public static final RegistryObject<Potion> WAKEFUL_SLEEP = reg.register("wakeful_sleep", () -> new Potion(new MobEffectInstance(SBEffects.WAKEFUL_SLEEP.get(), 1)));

    //TODO: Animal Conversion to Alchemical Animal
}