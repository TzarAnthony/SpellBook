package com.tzaranthony.spellbook.core.world.features;

import com.tzaranthony.spellbook.core.blocks.plant.HeightBerries;
import com.tzaranthony.spellbook.core.blocks.plant.LavaBerries;
import com.tzaranthony.spellbook.core.blocks.plant.StoneBerries;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class SBBlockFeatureConfig {
    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_SILVER_ORES = List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, SBBlocks.SILVER_ORE.get().defaultBlockState())
            ,OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, SBBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())
    );

    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_CINNABAR_ORES = List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, SBBlocks.CINNABAR_ORE.get().defaultBlockState())
            ,OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, SBBlocks.DEEPSLATE_CINNABAR_ORE.get().defaultBlockState())
    );

    public static final Holder<ConfiguredFeature<ProbabilityFeatureConfiguration, ?>> PLANT_WATER = FeatureUtils.register("plant_water", SBFeatures.OCEAN_PLANT.get(), new ProbabilityFeatureConfiguration(0.1F));
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PLANT_PEAK = FeatureUtils.register("plant_peak", Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(SBBlocks.NUBIBUS_BUSH.get().defaultBlockState().setValue(HeightBerries.AGE, Integer.valueOf(3))))
                    , List.of(Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.GRASS_BLOCK), 8));
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PLANT_CAVE = FeatureUtils.register("plant_cave", Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(SBBlocks.TERRAN_BUSH.get().defaultBlockState().setValue(StoneBerries.AGE, Integer.valueOf(3))))
                    , List.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF, Blocks.DEEPSLATE
                            , Blocks.NETHERRACK, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.DRIPSTONE_BLOCK, Blocks.SMOOTH_BASALT
                            , Blocks.CRACKED_DEEPSLATE_BRICKS,Blocks.CRACKED_DEEPSLATE_TILES, Blocks.CRACKED_NETHER_BRICKS
                            , Blocks.RED_SANDSTONE,Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
                            ,Blocks.COBBLED_DEEPSLATE, Blocks.COBBLESTONE, Blocks.SANDSTONE, Blocks.CLAY, Blocks.GRAVEL), 10));
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PLANT_LAVA = FeatureUtils.register("plant_lava", SBFeatures.LAVA_PLANT.get(),
            FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(
                    SBBlocks.NETHER_BUSH.get().defaultBlockState().setValue(LavaBerries.AGE, Integer.valueOf(3)).setValue(LavaBerries.LAVALOGGED, Boolean.valueOf(true))))
                    , List.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF, Blocks.DEEPSLATE
                            , Blocks.NETHERRACK, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.DRIPSTONE_BLOCK, Blocks.SMOOTH_BASALT
                            , Blocks.CRACKED_DEEPSLATE_BRICKS,Blocks.CRACKED_DEEPSLATE_TILES, Blocks.CRACKED_NETHER_BRICKS
                            , Blocks.RED_SANDSTONE,Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
                            ,Blocks.MAGMA_BLOCK, Blocks.SAND, Blocks.CLAY, Blocks.GRAVEL, Blocks.DIRT), 9));
//    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PLANT_LAVA = FeatureUtils.register("plant_lava", Feature.RANDOM_PATCH,
//            FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider
//                            .simple(SBBlocks.NETHER_BUSH.get().defaultBlockState().setValue(LavaBerries.AGE, Integer.valueOf(3)).setValue(LavaBerries.LAVALOGGED, Boolean.valueOf(true))))
//                    , List.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF, Blocks.DEEPSLATE
//                            , Blocks.NETHERRACK, Blocks.BASALT, Blocks.BLACKSTONE, Blocks.DRIPSTONE_BLOCK, Blocks.SMOOTH_BASALT
//                            , Blocks.CRACKED_DEEPSLATE_BRICKS,Blocks.CRACKED_DEEPSLATE_TILES, Blocks.CRACKED_NETHER_BRICKS
//                            , Blocks.RED_SANDSTONE,Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
//                            ,Blocks.MAGMA_BLOCK, Blocks.SAND, Blocks.CLAY, Blocks.GRAVEL, Blocks.DIRT), 9));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER = FeatureUtils.register("ore_silver", Feature.ORE, new OreConfiguration(OVERWORLD_SILVER_ORES, 5));
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_CINNABAR = FeatureUtils.register("ore_cinnabar", Feature.ORE, new OreConfiguration(OVERWORLD_CINNABAR_ORES, 3, 0.5F));
}