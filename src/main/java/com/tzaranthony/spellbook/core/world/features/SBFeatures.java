package com.tzaranthony.spellbook.core.world.features;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.world.features.featureTypes.LavaPlant;
import com.tzaranthony.spellbook.core.world.features.featureTypes.OceanPlant;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBFeatures {
    public static final DeferredRegister<Feature<?>> reg = DeferredRegister.create(ForgeRegistries.FEATURES, SpellBook.MOD_ID);

    public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> OCEAN_PLANT = reg.register("ocean_plant", () -> new OceanPlant(ProbabilityFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<RandomPatchConfiguration>> LAVA_PLANT = reg.register("lava_plant", () -> new LavaPlant(RandomPatchConfiguration.CODEC));
}