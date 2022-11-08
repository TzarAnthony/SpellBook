package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.util.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBEffects {
    public static final DeferredRegister<MobEffect> reg = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SpellBook.MOD_ID);

    // From Tools, Potions and Food
    public static final RegistryObject<MobEffect> INCINERATION = reg.register("incineration", ()-> new SBEffect(MobEffectCategory.HARMFUL, 15555367));
    public static final RegistryObject<MobEffect> FRACTURED = reg.register("fractured", ()-> new SBEffect(MobEffectCategory.HARMFUL, 6901550)
            .addAttributeModifier(Attributes.ARMOR, "31ae43ee-506e-11ec-bf63-0242ac130002", -0.8D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "9fb70244-7fc2-403f-a260-46bf1d780fbb", -0.8D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> FREEZING = reg.register("freezing", ()-> new FreezingEffect(MobEffectCategory.HARMFUL, 9761279)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "7b99b10e-aa3a-4b01-8191-780b0752b50c", (double) -0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> FREEZING_ANIMATION = reg.register("freezing_animation", ()-> new FreezingEffect(MobEffectCategory.NEUTRAL, 9761279));
    public static final RegistryObject<MobEffect> ENDER_INFECTION = reg.register("ender_infection", ()-> new SBEffect(MobEffectCategory.HARMFUL, 1521706));
    public static final RegistryObject<MobEffect> CLUMSY = reg.register("clumsy", ()-> new ClumsyEffect(MobEffectCategory.NEUTRAL, 14008076));
    public static final RegistryObject<MobEffect> WAKEFUL_SLEEP = reg.register("wakeful_sleep", ()-> new SBInstantEffect(MobEffectCategory.BENEFICIAL, 3113910));
    public static final RegistryObject<MobEffect> MERCURY_POISONING = reg.register("mercury_poisoning", ()-> new MercuryEffect(MobEffectCategory.HARMFUL, 8557698));

    // From Mobs
    public static final RegistryObject<MobEffect> BLEEDING = reg.register("bleeding", ()-> new SBEffect(MobEffectCategory.HARMFUL, 16736852));

    // From Spells
    public static final RegistryObject<MobEffect> CONCUSSED = reg.register("concussed", ()-> new SBEffect(MobEffectCategory.HARMFUL, 14600906)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, "51bdbb73-99cc-4b31-8c9f-c47a62152a41", 0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.JUMP_STRENGTH, "23239905-d0bb-42df-a5a5-fcedaa7af5b4", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "b7bcfd76-ca0b-4e56-b6a5-2103e3fb42a7", (double) -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> LETHARGY = reg.register("lethargy", ()-> new SBEffect(MobEffectCategory.HARMFUL, 7620390));
    public static final RegistryObject<MobEffect> STONE_ARMOR = reg.register("stone_armor", ()-> new SBEffect(MobEffectCategory.BENEFICIAL, 11632224)
            .addAttributeModifier(Attributes.ARMOR, "df37222c-1a73-4593-8d0b-0760bf63ea7c", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "c1c0404e-c8ef-4129-9173-c5fdfa5f3c68", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> PACIFICATION = reg.register("pacification", ()-> new SBEffect(MobEffectCategory.BENEFICIAL, 14365343)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, "df37222c-1a73-4593-8d0b-0760bf63ea7c", -0.99D, AttributeModifier.Operation.MULTIPLY_TOTAL));
}