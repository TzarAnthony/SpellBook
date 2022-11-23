package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.registries.SBBlockEntities;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class TimerBE extends BlockEntity {
    private final double diameter = 9;
    private int lifespan = 60*20;

    public TimerBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.TIMER.get(), pos, state);
    }

    public void setLifespan(int seconds) {
        this.lifespan = seconds * 20;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TimerBE tm) {
        BlockPos posSt = pos.below((int) Math.floor(tm.diameter/2.0D)).north((int) Math.floor(tm.diameter/2.0D)).east((int) Math.floor(tm.diameter/2.0D));
        BlockPos tmpPos;
        for(int i = 0; i < tm.diameter; ++i) {
            for (int j = 0; j < tm.diameter; ++j) {
                for (int k = 0; k < tm.diameter; ++k) {
                    tmpPos = posSt.above(i).south(j).west(k);
                    state = level.getBlockState(tmpPos);
                    if (state.is(SBBlocks.TIMER.get())) {
                        continue;
                    }

                    if (level instanceof ServerLevel && state.isRandomlyTicking()) {
                        state.randomTick((ServerLevel) level, tmpPos, level.random);
                    }

                    FluidState fstate = state.getFluidState();
                    if (fstate.isRandomlyTicking()) {
                        fstate.randomTick(level, tmpPos, level.random);
                    }

                    if (state.hasBlockEntity()) {
                        tickBE(level.getBlockEntity(tmpPos), state, tmpPos, level);
                    }

                    if (state.is(Blocks.ANCIENT_DEBRIS)) {

                        if (level.random.nextInt(10000) < 5) {
                            level.setBlock(tmpPos, SBBlocks.NETHERITE_ORE.get().defaultBlockState(), 2);
                            level.playSound(null, tmpPos, SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }

                        if (level.isClientSide() && level.random.nextInt(10000) < 50) {
                            ParticleUtils.spawnParticlesOnBlockFaces(level, tmpPos, ParticleTypes.CRIMSON_SPORE, UniformInt.of(3, 5));
                        }
                    }
                }
            }
        }

        --tm.lifespan;
        if (tm.lifespan <= 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    protected static <T extends BlockEntity> void tickBE(T be, BlockState state, BlockPos pos, Level level) {
        BlockEntityTicker<T> bet = state.getTicker(level, (BlockEntityType<T>) be.getType());
        if (bet != null) {
            bet.tick(level, pos, state, be);
        }
    }
}