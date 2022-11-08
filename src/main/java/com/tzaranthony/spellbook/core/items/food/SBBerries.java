package com.tzaranthony.spellbook.core.items.food;

import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SBBerries extends ItemNameBlockItem {
    public SBBerries(Block block, Properties properties) {
        super(block, properties);
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (context.getItemInHand().is(SBItems.CAELUM_BERRIES.get()) && !(context.getClickedPos().getY() >= 170)) {
            return InteractionResult.FAIL;
        }
        BlockState state = context.getLevel().getBlockState(context.getClickedPos().below());
        if (context.getItemInHand().is(SBItems.TERRENUS_BERRIES.get()) && ((state.is(BlockTags.DIRT)) || (state.is(Blocks.FARMLAND)))) {
            return InteractionResult.FAIL;
        }
        return super.place(context);
    }
}