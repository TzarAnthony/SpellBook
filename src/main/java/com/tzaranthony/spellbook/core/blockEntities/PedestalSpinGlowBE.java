package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalSpinGlowBE extends PedestalBE {
    private float activeRotation;

    public PedestalSpinGlowBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.PEDESTAL_SPIN_GLOW.get(), pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, PedestalSpinGlowBE pedestal) {
        ++pedestal.activeRotation;
        if (pedestal.activeRotation > 359) {
            pedestal.activeRotation = 0;
        }
    }

    public float getActiveRotation(float mod) {
        return this.activeRotation + mod;
    }

}