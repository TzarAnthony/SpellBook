package com.tzaranthony.spellbook.core.world.features;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class SBFeaturePlacements {
    // Plants
    public static final Holder<PlacedFeature> OCEAN_PLANT = PlacementUtils.register("ocean_plant",
            SBBlockFeatureConfig.PLANT_WATER, List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, RarityFilter.onAverageOnceEvery(6), BiomeFilter.biome()));
    public static final Holder<PlacedFeature> PEAK_PLANT = PlacementUtils.register("peak_plant",
            SBBlockFeatureConfig.PLANT_PEAK, CountPlacement.of(20), InSquarePlacement.spread()
            , HeightRangePlacement.uniform(VerticalAnchor.absolute(160), VerticalAnchor.absolute(256))
            , EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12)
            , RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());
    public static final Holder<PlacedFeature> CAVE_PLANT = PlacementUtils.register("cave_plant",
            SBBlockFeatureConfig.PLANT_CAVE, CountPlacement.of(30), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT
            , EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12)
            , RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());
//    public static final Holder<PlacedFeature> LAVA_PLANT = PlacementUtils.register("lava_plant",
//            SBBlockFeatureConfig.PLANT_LAVA, List.of(InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, RarityFilter.onAverageOnceEvery(6), BiomeFilter.biome()));
    public static final Holder<PlacedFeature> LAVA_PLANT = PlacementUtils.register("lava_plant",
            SBBlockFeatureConfig.PLANT_LAVA, RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());

    // Ores
    public static final Holder<PlacedFeature> ORE_SILVER_NEAR_COPPER = PlacementUtils.register("ore_silver_copper",
            SBBlockFeatureConfig.ORE_SILVER, commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(40))));
    public static final Holder<PlacedFeature> ORE_SILVER_NEAR_IRON = PlacementUtils.register("ore_silver_iron",
            SBBlockFeatureConfig.ORE_SILVER, commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(-57), VerticalAnchor.absolute(0))));
    public static final Holder<PlacedFeature> ORE_CINNABAR = PlacementUtils.register("ore_cinnabar",
            SBBlockFeatureConfig.ORE_CINNABAR, commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(-74), VerticalAnchor.absolute(-16))));

    public static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
        return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int veinCount, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(veinCount), modifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int chunkAvg, PlacementModifier modifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chunkAvg), modifier);
    }
}