package com.tzaranthony.spellbook.core.world.features.featureTypes;

import com.mojang.serialization.Codec;
import com.tzaranthony.spellbook.core.blocks.plant.WaterBerries;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

import java.util.Random;

public class OceanPlant extends Feature<ProbabilityFeatureConfiguration> {
    public OceanPlant(Codec<ProbabilityFeatureConfiguration> PFC) {
        super(PFC);
    }

    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> PFC) {
        boolean flag = false;
        Random random = PFC.random();
        WorldGenLevel worldgenlevel = PFC.level();
        BlockPos blockpos = PFC.origin();
        ProbabilityFeatureConfiguration pfc = PFC.config();
        int i = random.nextInt(10) - random.nextInt(10);
        int j = random.nextInt(10) - random.nextInt(10);
        int k = worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockpos.getX() + i, blockpos.getZ() + j);
        BlockPos blockpos1 = new BlockPos(blockpos.getX() + i, k, blockpos.getZ() + j);
        if (worldgenlevel.getBlockState(blockpos1).is(Blocks.WATER)) {
            boolean flag1 = random.nextDouble() < (double) pfc.probability;

            BlockState blockstate = flag1 ? SBBlocks.NYMPH_BUSH.get().defaultBlockState().setValue(WaterBerries.AGE, Integer.valueOf(3)) : SBBlocks.NYMPH_BUSH.get().defaultBlockState().setValue(WaterBerries.AGE, Integer.valueOf(2));
            if (blockstate.canSurvive(worldgenlevel, blockpos1)) {
                worldgenlevel.setBlock(blockpos1, blockstate, 2);
                flag = true;
            }
        }
        return flag;
    }
}