package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.core.blockEntities.AlterBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Alter extends BaseEntityBlock {
    public Alter(Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlterBE(pos, state);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof AlterBE) {
            AlterBE abe = (AlterBE) blockentity;
            abe.setTier();
            ItemStack itemstack = player.getItemInHand(hand);
            if (abe.checkMultiBlock()) {
                abe.startCrafting();
                Vec3 vec3 = player.getDeltaMovement();
                for(int i = 0; i < 8; ++i) {
                    double d0 = (double) pos.getX() + level.random.nextDouble();
                    double d1 = (double) pos.above().getY() + level.random.nextDouble();
                    double d2 = (double) pos.getZ() + level.random.nextDouble();
                    level.addParticle(ParticleTypes.ENCHANTED_HIT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}