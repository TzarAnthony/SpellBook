package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SnareBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SnareBlock() {
        super(SBBlockProperties.MagicBlock().lightLevel((lightSupplier) -> {return 3;}).sound(SoundType.CHAIN).randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(POWERED, Boolean.valueOf(false)));
    }

    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE, POWERED);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && !(entity instanceof Player || (entity instanceof TamableAnimal ta && !ta.isTame()))) {
            entity.setPos(new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));
            entity.makeStuckInBlock(state, new Vec3((double) 0.005F, 0.005F, (double) 0.005F));
            if (!level.isClientSide && !state.getValue(POWERED)) {
                level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(true)), 3);
                level.playSound((Player) null, pos, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        int i = state.getValue(AGE);
        if (i < 2) {
            level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    public void setLifespan(int lifespan, BlockState state) {
        state.setValue(AGE, Integer.valueOf(lifespan));
    }
}