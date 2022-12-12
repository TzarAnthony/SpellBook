package com.tzaranthony.spellbook.core.blocks.spellBlocks;

import com.tzaranthony.spellbook.core.blockEntities.TimerBE;
import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.core.blocks.containsBE.TickingBEBlock;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Timer extends TickingBEBlock {
    public Timer() {
        super(SBBlockProperties.MagicBlock().noCollission());
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimerBE(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, SBBlockEntities.TIMER.get(), TimerBE::tick);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        for(int i = 0; i < 8; ++i) {
            level.addParticle(
//                    new ItemParticleOption(ParticleTypes.ITEM, Items.SAND).setPos(pos)
                    new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState())
                    ,(double) pos.getX() + rand.nextDouble()
                    ,(double) pos.getY() + rand.nextDouble()
                    ,(double) pos.getZ() + rand.nextDouble()
                    ,((double) rand.nextFloat() - 0.5D) * 0.5D
                    ,((double) rand.nextFloat() - 0.5D) * 0.5D
                    ,((double) rand.nextFloat() - 0.5D) * 0.5D
            );
        }

        if (level.getGameTime() % 50L == 0L) {
            level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D
                    , SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1.0F, rand.nextFloat() * 0.4F + 0.5F, false
            );
        }
    }
}