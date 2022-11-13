package com.tzaranthony.spellbook.core.blocks.spellBlocks;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

public class BlackHole extends SpellBlock {
    public BlackHole() {
        super(SBBlockProperties.MagicBlock().noCollission());
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState p_56699_, boolean placed) {
        level.scheduleTick(pos, this, 1);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        AABB box = new AABB(pos).inflate(6.0D);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);

        if (!entities.isEmpty()) {
            for (Entity e : entities) {
                if (!(e instanceof HangingEntity)) {
                    e.moveTo(pos, 0.0F, 0.0F);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        for(int i = 0; i < 8; ++i) {
            if (!(rand.nextInt(16) == 0)) {
                level.addParticle(
                        ParticleTypes.SQUID_INK
                        ,((double) pos.getX() + 0.5D)
                        ,((double) pos.getY() + 0.5D)
                        ,((double) pos.getZ() + 0.5D)
                        ,((float) pos.getX() - 3.5D + rand.nextInt(6) + rand.nextFloat())
                        ,((float) pos.getY() - 3.5D + rand.nextInt(6) + rand.nextFloat())
                        ,((float) pos.getZ() - 3.5D + rand.nextInt(6) + rand.nextFloat())
                );
            }
        }

        if (level.getGameTime() % 80L == 0L) {
            level.playSound((Player)null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 3.0F, 0.6F);
        }
    }
}