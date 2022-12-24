package com.tzaranthony.spellbook.core.items.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public class SpawnerBI extends BlockItem {
    public SpawnerBI() {
        super(Blocks.SPAWNER, new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionResult interactionresult = this.place(new BlockPlaceContext(context));
        //TODO: fix to make it not work like this since this doesn't allow customization of other spawner attributes. Try copying SetBlock command???
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity sbe) {
            BaseSpawner spawner = sbe.getSpawner();
            spawner.setEntityId(getEntityID(stack));
            sbe.setChanged();
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        }
        if (!interactionresult.consumesAction() && this.isEdible()) {
            InteractionResult interactionresult1 = this.use(level, context.getPlayer(), context.getHand()).getResult();
            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
        } else {
            return interactionresult;
        }
    }

    public EntityType<?> getEntityID(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return EntityType.byString(tag.getString("SBEntityIDTag")).orElse(EntityType.BAT);
    }
}