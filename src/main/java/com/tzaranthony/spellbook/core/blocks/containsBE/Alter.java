package com.tzaranthony.spellbook.core.blocks.containsBE;

import com.tzaranthony.spellbook.core.blockEntities.AlterBE;
import com.tzaranthony.spellbook.core.blockEntities.CraftingBE;
import com.tzaranthony.spellbook.core.items.spellBooks.SpellBookNovice;
import com.tzaranthony.spellbook.core.spells.Binding;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Alter extends TickingBEBlock {
    public Alter(Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlterBE(pos, state);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof AlterBE abe) {
            abe.setTier();
            ItemStack itemstack = player.getItemInHand(hand);
            if (itemstack.getItem() instanceof SpellBookNovice && abe.checkMultiBlock()) {
                level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.BLOCKS, 0.5F, 1.0F);
                abe.startCrafting();
                for(int i = 0; i < 8; ++i) {
                    double d0 = (double) pos.getX() + level.random.nextDouble();
                    double d1 = (double) pos.above().getY() + level.random.nextDouble();
                    double d2 = (double) pos.getZ() + level.random.nextDouble();
                    level.addParticle(ParticleTypes.ENCHANTED_HIT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            } else if (player.isShiftKeyDown() && itemstack.isEmpty()) {
                List<Mob> mobs = level.getEntitiesOfClass(Mob.class,
                        new AABB((double) (pos.getX() - 10), (double) (pos.getY() - 10), (double) (pos.getZ() - 10), (double) (pos.getX() + 10), (double) (pos.getY() + 10), (double) (pos.getZ() + 10)));
                boolean addedMobs = false;
                for (Mob mob : mobs) {
                    if (Binding.isBound(mob) && Binding.isBoundBy(mob) == player) {
                        abe.addMob(mob);
                        addedMobs = true;
                    }
                }
                if (!addedMobs) {
                    abe.clearMobs();
                    abe.takeOrAddItem(player, itemstack);
                }
            } else {
                abe.takeOrAddItem(player, itemstack);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state1, boolean huh) {
        if (!state.is(state1.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof CraftingBE craftingBE) {
                if (level instanceof ServerLevel) {
                    craftingBE.dropInventory();
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, state1, huh);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, SBBlockEntities.ALTER.get(), level.isClientSide ? AlterBE::clientTick : AlterBE::serverTick);
    }
}