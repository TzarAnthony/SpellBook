package com.tzaranthony.spellbook.core.world.gen;

import com.tzaranthony.spellbook.core.world.features.SBFeaturePlacements;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;

public class SBPlantGeneration {
    public static void generatePlants(final BiomeLoadingEvent event) {
        ResourceKey<Biome> name = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(name);

        if (name == Biomes.DEEP_COLD_OCEAN || name == Biomes.DEEP_OCEAN || name == Biomes.DEEP_FROZEN_OCEAN || name == Biomes.DEEP_LUKEWARM_OCEAN) {
            List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
            base.add(SBFeaturePlacements.OCEAN_PLANT);
        }
        if (types.contains(BiomeDictionary.Type.PEAK)) {
            List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
            base.add(SBFeaturePlacements.PEAK_PLANT);
        }
        if (types.contains(BiomeDictionary.Type.UNDERGROUND)) {
            List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
            base.add(SBFeaturePlacements.CAVE_PLANT);
        }
        if (types.contains(BiomeDictionary.Type.HOT) || types.contains(BiomeDictionary.Type.COLD) || types.contains(BiomeDictionary.Type.WATER)
                || types.contains(BiomeDictionary.Type.FOREST) || types.contains(BiomeDictionary.Type.PLAINS) || types.contains(BiomeDictionary.Type.MOUNTAIN) ) {
            List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
            base.add(SBFeaturePlacements.LAVA_PLANT);
        }
    }
}