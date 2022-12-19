package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.core.blockEntities.RiftBE;
import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Rift extends TickingBEBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);

    public Rift() {
        super(SBBlockProperties.MagicBlock().lightLevel((p_50804_) -> {return 8;}));
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RiftBE(pos, state);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (level instanceof ServerLevel && blockentity instanceof RiftBE && !entity.isVehicle()) {
            ServerLevel sLevel = (ServerLevel) level;
            RiftBE rift = (RiftBE) blockentity;

            if (!entity.isOnPortalCooldown()) {
                entity.setPortalCooldown();
                if (rift.getTpDim() != level.dimension() && entity.canChangeDimensions()) {
                    entity.changeDimension(sLevel.getServer().getLevel(rift.getTpDim()));
                }
                Vec3 dir = entity.getDeltaMovement();
                entity.teleportTo(rift.getTpPos().getX() + 0.5D, rift.getTpPos().getY(), rift.getTpPos().getZ() + 0.5D);
                if (rift.getTpDim() == level.dimension()) {
                    level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 1.0F, 0.25F);
                    entity.playSound(SoundEvents.PORTAL_TRAVEL, 1.0F, 0.25F);
                }
//                entity.setPos(new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));
                entity.setDeltaMovement(dir);
            } else {
                entity.setPortalCooldown();
            }
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        if (rand.nextInt(100) == 0) {
            level.playSound((Player)null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        for(int j = 0; j < 4; ++j) {
            double d0 = (double) pos.getX() + rand.nextDouble();
            double d1 = (double) pos.getY() + 0.5D + rand.nextDouble();
            double d2 = (double) pos.getZ() + rand.nextDouble();
            double d3 = (rand.nextDouble() - 0.5D) * 0.5D;
            double d4 = (rand.nextDouble() - 0.5D) * 0.5D;
            double d5 = (rand.nextDouble() - 0.5D) * 0.5D;
            int k = rand.nextInt(2) * 2 - 1;
            if (rand.nextBoolean()) {
                d2 = (double) pos.getZ() + 0.5D + 0.25D * (double)k;
                d5 = (double)(rand.nextFloat() * 2.0F * (float)k);
            } else {
                d0 = (double) pos.getX() + 0.5D + 0.25D * (double)k;
                d3 = (double)(rand.nextFloat() * 2.0F * (float)k);
            }

            level.addParticle(ParticleTypes.REVERSE_PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    public VoxelShape getShape(BlockState p_52122_, BlockGetter p_52123_, BlockPos p_52124_, CollisionContext p_52125_) {
        return SHAPE;
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.INVISIBLE;
    }

    public boolean canBeReplaced(BlockState p_53012_, Fluid p_53013_) {
        return false;
    }
}