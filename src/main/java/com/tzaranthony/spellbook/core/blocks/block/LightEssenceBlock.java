package com.tzaranthony.spellbook.core.blocks.block;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.SBGhostEntity;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightEssenceBlock extends Block {
    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, (double)0.9F, 1.0D);

    public LightEssenceBlock( float resistance, int light) {
        super(SBBlockProperties.LightyNetheriteBasedBlock(resistance, light).noOcclusion().dynamicShape());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext) {
            if (((EntityCollisionContext) context).getEntity() instanceof Player) {
                if (context.isAbove(Shapes.block(), pos, true) && !context.isDescending()) {
                    return super.getCollisionShape(state, reader, pos, context);
                } else {
                    return Shapes.empty();
                }
            } else if ((((EntityCollisionContext) context).getEntity() instanceof SBVampireEntity) || (((EntityCollisionContext) context).getEntity() instanceof SBGhostEntity)) {
                return Shapes.empty();
            } else if (!(((EntityCollisionContext) context).getEntity() instanceof Player)
                    && ((EntityCollisionContext) context).getEntity() instanceof LivingEntity
                    && !context.isAbove(Shapes.block(), pos, true)) {
                return super.getCollisionShape(state, reader, pos, context);
            } else {
                return Shapes.empty();
            }
        } else {
            return super.getCollisionShape(state, reader, pos, context);
        }
    }

    public boolean skipRendering(BlockState p_154268_, BlockState p_154269_, Direction p_154270_) {
        return p_154269_.is(this) ? true : super.skipRendering(p_154268_, p_154269_, p_154270_);
    }

    public VoxelShape getOcclusionShape(BlockState p_154272_, BlockGetter p_154273_, BlockPos p_154274_) {
        return Shapes.empty();
    }

    public boolean isPathfindable(BlockState p_154258_, BlockGetter p_154259_, BlockPos p_154260_, PathComputationType p_154261_) {
        return true;
    }

    public VoxelShape getVisualShape(BlockState p_154276_, BlockGetter p_154277_, BlockPos p_154278_, CollisionContext p_154279_) {
        return Shapes.empty();
    }
}