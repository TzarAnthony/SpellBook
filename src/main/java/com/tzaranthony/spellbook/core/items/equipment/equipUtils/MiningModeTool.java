package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface MiningModeTool {
    String MINING_MODE = "ToolMode";

    default void useOnBlocks(ItemStack stack, Level level, UseOnContext context, BlockPos pos) {
        int mode = getMiningMode(stack);
        switch (mode) {
            case 1 -> areaUseOn(level, pos, context, 1, 1);
            case 2 -> areaUseOn(level, pos, context, 2, 2);
        }
    }

    default void mineBlocks(ItemStack stack, ServerLevel level, ServerPlayer player, BlockPos pos) {
        int mode = getMiningMode(stack);
        switch (mode) {
            case 1 -> areaMine(level, player, pos, stack, 1, 1);
            case 2 -> areaMine(level, player, pos, stack, 2, 2);
            case 3 -> veinMine(level, player, pos, level.getBlockState(pos).getBlock(), 49);
        }
    }

    default void getHoverDetails(ItemStack stack, List<Component> tooltip) {
        int mode = getMiningMode(stack);
        switch (mode) {
            case 1 -> tooltip.add((new TranslatableComponent("tooltip.spellbook.area_mode")).append("3x3"));
            case 2 -> tooltip.add((new TranslatableComponent("tooltip.spellbook.area_mode")).append("5x5"));
            case 3 -> tooltip.add(new TranslatableComponent("tooltip.spellbook.vein_mode"));
        }
    }

    default void playChangeSound(Level level, LivingEntity entity) {
        level.playSound((Player) null, entity.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.8F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    default void nextMiningMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MINING_MODE)) {
            tag.putInt(MINING_MODE, 0);
        }
        int id = tag.getInt(MINING_MODE);
        if (++id > 3) {
            id = 0;
        }
        setMiningMode(tag, id);
    }

    default void setMiningMode(CompoundTag tag, int id) {
        tag.putInt(MINING_MODE, id);
    }

    default int getMiningMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MINING_MODE)) {
            tag.putInt(MINING_MODE, 0);
            return 0;
        }
        return tag.getInt(MINING_MODE);
    }

    // Use on details
    private void veinUseOn(Level level, UseOnContext context, BlockPos pos, Block originBlock, int useCount) {
        NonNullList<BlockPos> prevPos = NonNullList.create();
        prevPos.add(pos);
        NonNullList<BlockPos> minePos = NonNullList.create();

        useCount = popVeinArray(prevPos, minePos, level, context, originBlock, useCount);

        while (useCount > 0 && minePos.size() > 0) {
            prevPos.addAll(minePos);
            minePos.clear();
            useCount = popVeinArray(prevPos, minePos, level, context, originBlock, useCount);
        }
    }

    private int popVeinArray(NonNullList<BlockPos> prev, NonNullList<BlockPos> curr, Level level, UseOnContext context, Block originBlock, int availSz) {
        BlockPos pos1;
        for (BlockPos pos : prev) {
            for (Direction dir: Direction.values()) {
                pos1 = pos.relative(dir);
                if (!prev.contains(pos1) && level.getBlockState(pos1).is(originBlock) && consumeMP(context.getPlayer())) {
                    curr.add(pos1);
                    performUse(level, pos1, context);
                    --availSz;
                }
                if (availSz <= 0) {
                    break;
                }
            }
            if (availSz <= 0) {
                break;
            }
        }
        return availSz;
    }

    private void areaUseOn(Level level, BlockPos pos, UseOnContext context, int r1, int r2) {
        Player player = context.getPlayer();
        Direction facing = getPlayerPOVHitResult(level, player).getDirection();
        int i = r1 * 2 + 1;
        int j = r2 * 2 + 1;
        BlockPos sqPos;
        BlockPos tmpPos;
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            sqPos = pos.above(i-2).east(r2);
            for (int m = 0; m < i; ++m) {
                for (int n = 0; n < j; ++n) {
                    tmpPos = sqPos.below(m).west(n);
                    if (!(tmpPos == pos) && consumeMP(context.getPlayer())) {
                        performUse(level, tmpPos, context);
                    }
                }
            }
        } else if (facing == Direction.EAST || facing == Direction.WEST) {
            sqPos = pos.above(i-2).north(r2);
            for (int m = 0; m < i; ++m) {
                for (int n = 0; n < j; ++n) {
                    tmpPos = sqPos.below(m).south(n);
                    if (!(tmpPos == pos) && consumeMP(context.getPlayer())) {
                        performUse(level, tmpPos, context);
                    }
                }
            }
        } else if (facing == Direction.UP || facing == Direction.DOWN) {
            sqPos = pos.north(r1).east(r2);
            for (int m = 0; m < i; ++m) {
                for (int n = 0; n < j; ++n) {
                    tmpPos = sqPos.south(m).west(n);
                    if (!(tmpPos == pos) && consumeMP(context.getPlayer())) {
                        performUse(level, tmpPos, context);
                    }
                }
            }
        }
    }

    InteractionResult performUse(Level level, BlockPos pos, UseOnContext context);


    // Mining details
    private static void veinMine(ServerLevel level, ServerPlayer player, BlockPos pos, Block originBlock, int breakCount) {
        NonNullList<BlockPos> prevPos = NonNullList.create();
        prevPos.add(pos);
        NonNullList<BlockPos> minePos = NonNullList.create();

        breakCount = popVeinList(prevPos, minePos, level, player, originBlock, breakCount);

        while (breakCount > 0 && minePos.size() > 0) {
            prevPos.addAll(minePos);
            minePos.clear();
            breakCount = popVeinList(prevPos, minePos, level, player, originBlock, breakCount);
        }
    }

    private static int popVeinList(NonNullList<BlockPos> prev, NonNullList<BlockPos> curr, ServerLevel level, ServerPlayer player, Block originBlock, int availSz) {
        BlockPos pos1;
        for (BlockPos pos : prev) {
            for (Direction dir: Direction.values()) {
                pos1 = pos.relative(dir);
                if (!prev.contains(pos1) && level.getBlockState(pos1).is(originBlock) && consumeMP(player)) {
                    curr.add(pos1);
                    breakBlock(level, player, pos1);
                    --availSz;
                }
                if (availSz <= 0) {
                    break;
                }
            }
            if (availSz <= 0) {
                break;
            }
        }
        return availSz;
    }

    private static void areaMine(ServerLevel level, ServerPlayer player, BlockPos pos, ItemStack stack, int r1, int r2) {
        Direction facing = getPlayerPOVHitResult(level, player).getDirection();
        int i = r1 * 2 + 1;
        int j = r2 * 2 + 1;
        BlockPos sqPos;
        BlockPos tmpPos;
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            sqPos = pos.above(i-2).east(r2);
            for (int m = 0; m < i; ++m) {
                for (int n = 0; n < j; ++n) {
                    tmpPos = sqPos.below(m).west(n);
                    if (!(tmpPos == pos) && stack.isCorrectToolForDrops(level.getBlockState(tmpPos)) && consumeMP(player)) {
                        breakBlock(level, player, tmpPos);
                    }
                }
            }
        } else if (facing == Direction.EAST || facing == Direction.WEST) {
            sqPos = pos.above(i-2).north(r2);
            for (int m = 0; m < i; ++m) {
                for (int n = 0; n < j; ++n) {
                    tmpPos = sqPos.below(m).south(n);
                    if (!(tmpPos == pos) && stack.isCorrectToolForDrops(level.getBlockState(tmpPos)) && consumeMP(player)) {
                        breakBlock(level, player, tmpPos);
                    }
                }
            }
        } else if (facing == Direction.UP || facing == Direction.DOWN) {
            sqPos = pos.north(r1).east(r2);
            for (int m = 0; m < i; ++m) {
                for (int n = 0; n < j; ++n) {
                    tmpPos = sqPos.south(m).west(n);
                    if (!(tmpPos == pos) && stack.isCorrectToolForDrops(level.getBlockState(tmpPos)) && consumeMP(player)) {
                        breakBlock(level, player, tmpPos);
                    }
                }
            }
        }
    }

    private static boolean breakBlock(ServerLevel level, ServerPlayer player, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(level, player.gameMode.getGameModeForPlayer(), player, pos);
        if (exp == -1) {
            return false;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            Block block = blockstate.getBlock();
            if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
                level.sendBlockUpdated(pos, blockstate, blockstate, 3);
                return false;
            } else if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
                return false;
            } else if (player.blockActionRestricted(level, pos, player.gameMode.getGameModeForPlayer())) {
                return false;
            } else  {
                if (player.isCreative()) {
                    removeBlock(level, player, pos, false);
                } else {
                    ItemStack itemstack = player.getMainHandItem();
                    ItemStack itemstack1 = itemstack.copy();
                    boolean flag1 = blockstate.canHarvestBlock(level, pos, player); // previously player.hasCorrectToolForDrops(blockstate)
                    // already ran
//                    itemstack.mineBlock(level, blockstate, pos, player);
//                    if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
//                        net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
//                    }
                    boolean flag = removeBlock( level, player,pos, flag1);

                    if (flag) {
                        if (flag1) {
                            block.playerDestroy(level, player, pos, blockstate, blockentity, itemstack1);
                        }

                        if (exp > 0) {
                            blockstate.getBlock().popExperience(level, pos, exp);
                        }
                    }
                }
                return true;
            }
        }
    }

    private static boolean removeBlock(ServerLevel level, ServerPlayer player, BlockPos pos, boolean canHarvest) {
        BlockState state = level.getBlockState(pos);
        //        if (removed)
//            state.getBlock().destroy(level, pos, state);
        return state.onDestroyedByPlayer(level, pos, player, canHarvest, level.getFluidState(pos));
    }


    private static BlockHitResult getPlayerPOVHitResult(Level level, Player player) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    //TODO: add magic consumption
    private static boolean consumeMP(LivingEntity entity) {
        int mp = 100000;
        if (mp > 0) {
            --mp;
            return true;
        }
        return false;
    }
}