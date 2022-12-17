package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class AreaBlockSpell extends ProjectileSpell {
    public AreaBlockSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    public boolean placeBlocksInArea(BlockPos pos, Level level, int rl, int rw, int rh, int outOf) {
        for (int i = 0; i < rl; ++i) {
            for (int j = 0; j < rw; ++j) {
                for (int k = 0; k < rh; ++k) {
                    BlockPos pos1 = pos.south(i).west(j).below(k);
                    if (level.random.nextInt(outOf) == 0 && level.getBlockState(pos1).isAir() && level.getBlockState(pos1.below()).isSolidRender(level, pos1.below())) {
                        placeBlock(level, pos1);
                    }
                }
            }
        }
        return true;
    }

    public abstract void placeBlock(Level level, BlockPos pos);
}