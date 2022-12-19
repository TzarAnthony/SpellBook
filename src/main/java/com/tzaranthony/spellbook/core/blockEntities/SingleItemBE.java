package com.tzaranthony.spellbook.core.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SingleItemBE extends BlockEntity {
    protected NonNullList<ItemStack> item = NonNullList.withSize(1, ItemStack.EMPTY);

    public SingleItemBE(BlockEntityType<? extends BlockEntity> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.item);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.item = NonNullList.withSize(this.item.size(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.item);
    }

    public abstract void playAddSound();

    public abstract void playRemoveSound();

    public boolean changeItem(Player player, ItemStack stack) {
        if (stack.isEmpty() || stack.is(this.getItem().getItem())) {
            if (!this.getItem().isEmpty()) {
                addOrPopItem(player);
                update();
                return true;
            } else {
                return false;
            }
        }

        ItemStack stackA = stack.copy();
        stackA.setCount(1);

        if (!this.getItem().isEmpty()) {
            addOrPopItem(player);
        }
        playAddSound();
        this.item.set(0, stackA);
        stack.shrink(1);
        update();
        return true;
    }

    protected void addOrPopItem(Player player) {
        ItemStack stack = this.getItem();
        player.getInventory().add(stack);
        if (!stack.isEmpty()) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX(), this.getBlockPos().above().getY(), this.getBlockPos().getZ(), stack));
        }
        playRemoveSound();
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    protected void update() {
        setChanged();
        getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, this.item, true);
        return tag;
    }

    public ItemStack getItem() {
        return this.item.get(0);
    }

    public NonNullList<ItemStack> getItems() {
        return this.item;
    }
}