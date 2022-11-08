package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.util.tags.SBBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GrowthSpell extends Spell {
    public GrowthSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        int diameter = 9;
        int height = 3;

        pos = pos.below((int) Math.floor(height/2.0D)).north((int) Math.floor(diameter/2.0D)).east((int) Math.floor(diameter/2.0D));
        BlockPos tmpPos;
        for(int i = 0; i < height; ++i) {
            for (int j = 0; j < diameter; ++j) {
                for (int k = 0; k < diameter; ++k) {
                    tmpPos = pos.above(i).south(j).west(k);
                    state = level.getBlockState(tmpPos);
                    block = state.getBlock();
                    tryGrowPlant(block, state, level, tmpPos);
                }
            }
        }
        return true;
    }

    public boolean tryGrowPlant(Block block, BlockState state, Level level, BlockPos pos) {
        boolean flag = false;
        boolean flag1 = false;
        IntegerProperty integerproperty = null;
        if (state.is(SBBlockTags.GROWTH_SPELL_GROWABLES) && !level.isClientSide()) {
            if (block instanceof BonemealableBlock) {
                flag1 = true;
                ((BonemealableBlock) block).performBonemeal((ServerLevel) level, level.random, pos, state);
            } else if (block instanceof SugarCaneBlock) {
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, true)) {
                    state = state.setValue(SugarCaneBlock.AGE, Integer.valueOf(0));
                    ((ServerLevel) level).setBlockAndUpdate(pos.above(), state);
                    ((ServerLevel) level).setBlock(pos, state, 4);
                    flag1 = true;
                }
            } else if (block instanceof CactusBlock) {
                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, true)) {
                    state = state.setValue(CactusBlock.AGE, Integer.valueOf(0));
                    ((ServerLevel) level).setBlockAndUpdate(pos.above(), state);
                    ((ServerLevel) level).setBlock(pos, state, 4);
                    state.neighborChanged(((ServerLevel) level), pos.above(), block, pos, false);
                    flag1 = true;
                }
            } else if (block instanceof ChorusFlowerBlock) {
                flag = false;
            }

            if (flag) {
//                level.levelEvent(2005, pos, 0);
                level.setBlockAndUpdate(pos, state.setValue(integerproperty, Integer.valueOf(state.getValue(integerproperty) + 1)));
            }
        }
        return flag1;
    }
}