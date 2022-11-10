package com.tzaranthony.spellbook.core.blockEntities;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public class MultiblockHelper {

    public static Boolean validateMultiBlockPattern(String[][] pattern, Map<String, TagKey<Block>> map, Level level, BlockPos posCenter) {

        BlockPos pos = posCenter.below((int) Math.floor(pattern.length/2.0D)).north((int) Math.floor(pattern[0].length/2.0D)).east((int) Math.floor(pattern[0][0].length()/2.0D));

        if (level.isOutsideBuildHeight(pos)) {
            System.out.print("Unable to create multiblock outside of build height.\n");
            return false;
        }

        Set<String> set = Sets.newHashSet(map.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length; ++j) {
                for(int k = 0; k < pattern[i][j].length(); ++k) {
                    String s = pattern[i][j].substring(k, k + 1);
                    if (s.equals("#")) {
                        continue;
                    } else if (s.equals("Z")) {
                        set.remove(s);
                        BlockState state = level.getBlockState(pos.above(i).south(j).west(k));
                        if (!(state.isCollisionShapeFullBlock(level, pos) || state.getBlock() instanceof SlabBlock || state.getBlock() instanceof StairBlock
                                || state.getBlock() instanceof WallBlock || state.getBlock() instanceof FenceBlock)) {
                            System.out.print("Block '" + state.getBlock().getRegistryName().toString() + "' is not a full block, slab, stair, wall, or fence. " + pos.above(i).south(j).west(k) + "\n"
                            );
                            return false;
                        }
                    } else {
                        TagKey<Block> blockTag = map.get(s);

                        if (blockTag == null) {
                            System.out.print("Unable to create multiblock. Pattern references symbol '" + s + "' but it's not defined in the keys\n");
                            return false;
                        }
                        set.remove(s);
                        if (!level.getBlockState(pos.above(i).south(j).west(k)).is(blockTag)) {
                            System.out.print("Block '" +
                                    level.getBlockState(pos.above(i).south(j).west(k)).getBlock().getRegistryName().toString()
                                    + "' is not valid structure block for '" +
                                    blockTag.toString()
                                    + "'.\n"
                            );
                            return false;
                        }
                    }
                }
            }
        }

        if (!set.isEmpty()) {
            System.out.print("Unable to create multiblock. Key defines symbols that aren't used in pattern: " + set + "\n");
            return false;
        } else {
            return true;
        }
    }
}