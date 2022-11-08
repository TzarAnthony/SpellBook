package com.tzaranthony.spellbook.core.world.features.featureTypes;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import java.util.Random;

public class LavaPlant extends Feature<RandomPatchConfiguration> {
    public LavaPlant(Codec<RandomPatchConfiguration> RPC) {
        super(RPC);
    }

    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> RPC) {
//        boolean flag = false;
//        Random random = RPC.random();
//        WorldGenLevel worldgenlevel = PFC.level();
//        BlockPos blockpos = PFC.origin();
//        int i = random.nextInt(10) - random.nextInt(10);
//        int j = random.nextInt(10) - random.nextInt(10);
//        int k = worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockpos.getX() + i, blockpos.getZ() + j);
//        BlockPos blockpos1 = new BlockPos(blockpos.getX() + i, k, blockpos.getZ() + j);
//        if (worldgenlevel.getBlockState(blockpos1).is(Blocks.LAVA)) {
//            BlockState blockstate = SBBlocks.NETHER_BUSH.get().defaultBlockState().setValue(LavaBerries.AGE, Integer.valueOf(3)).setValue(LavaBerries.LAVALOGGED, Boolean.valueOf(true));
//            if (blockstate.canSurvive(worldgenlevel, blockpos1)) {
//                worldgenlevel.setBlock(blockpos1, blockstate, 2);
//                flag = true;
//            }
//        }
//        return flag;

        RandomPatchConfiguration randompatchconfiguration = RPC.config();
        Random random = RPC.random();
        BlockPos blockpos = RPC.origin();
        WorldGenLevel worldgenlevel = RPC.level();
        int i = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int j = randompatchconfiguration.xzSpread() + 1;
        int k = randompatchconfiguration.ySpread() + 1;

        for(int l = 0; l < randompatchconfiguration.tries(); ++l) {
            blockpos$mutableblockpos.setWithOffset(blockpos, random.nextInt(j) - random.nextInt(j), random.nextInt(k) - random.nextInt(k), random.nextInt(j) - random.nextInt(j));
            if (worldgenlevel.getBlockState(blockpos$mutableblockpos).is(Blocks.LAVA) && randompatchconfiguration.feature().value().place(worldgenlevel, RPC.chunkGenerator(), random, blockpos$mutableblockpos)) {
                ++i;
            }
        }
        return i > 0;
    }
}