package com.tzaranthony.spellbook.core.items.spellBooks;

import com.tzaranthony.spellbook.core.items.SBItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class SpellBookNovice extends Item {
    public boolean hasBookmark = false;

    public SpellBookNovice(Rarity rare) {
        super(SBItemProperties.Standard(rare, 1));
    }

//    @Override
//    public InteractionResult useOn(UseOnContext context) {
//        BlockPos blockpos = context.getClickedPos();
//        Level level = context.getLevel();
//
//        Item item = context.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).getItem();
//
//        BlockState currentBlockState = level.getBlockState(blockpos);
//        Block block = currentBlockState.getBlock();
//        if (!(block instanceof CampfireBlock)) {
//            return super.useOn(context);
//        } else {
//            if (block == SBBlocks.END_CAMPFIRE.get() && item instanceof SpellBookAdv) {
//                convertBlockEffects(level, blockpos);
//                level.setBlock(blockpos, SBBlocks.ALTER_3.get().defaultBlockState(), 2);
//            } else if (block == Blocks.SOUL_CAMPFIRE && item instanceof SpellBookInter) {
//                convertBlockEffects(level, blockpos);
//                level.setBlock(blockpos, SBBlocks.ALTER_2.get().defaultBlockState(), 2);
//            } else if (block == Blocks.CAMPFIRE) {
//                convertBlockEffects(level, blockpos);
//                level.setBlock(blockpos, SBBlocks.ALTER_1.get().defaultBlockState(), 2);
//            }
//
//            return InteractionResult.sidedSuccess(level.isClientSide);
//        }
//    }
//
//    public void convertBlockEffects(Level level, BlockPos blockpos) {
//        level.playSound((Player) null, blockpos, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.BLOCKS, 1.0F, 1.0F);
//        for (int i = 0; i < 64; ++i) {
//            level.addParticle(ParticleTypes.PORTAL, blockpos.getX() + 0.5D, blockpos.getY() + level.random.nextDouble() + level.random.nextDouble(), blockpos.getZ() + 0.5D, level.random.nextGaussian(), 0.0D, level.random.nextGaussian());
//        }
//    }

    public void setHasBookmark(boolean bookmarked) {
        this.hasBookmark = bookmarked;
    }

    public boolean getHasBookmark() {
        return this.hasBookmark;
    }
}