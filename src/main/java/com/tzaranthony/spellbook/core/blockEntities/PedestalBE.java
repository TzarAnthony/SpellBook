package com.tzaranthony.spellbook.core.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PedestalBE extends BlockEntity {
    private NonNullList<ItemStack> item = NonNullList.withSize(1, ItemStack.EMPTY);

    public PedestalBE(BlockEntityType<? extends PedestalBE> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.item = NonNullList.withSize(this.item.size(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.item);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.item);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean changeItem(Player player, ItemStack stack) {


        if (stack.isEmpty() || stack.is(this.getItem().getItem())) {
            if (!this.getItem().isEmpty()) {
                addOrPopItem(player);
                player.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
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
        player.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.item.set(0, stackA);
        stack.shrink(1);
        update();
        return true;
    }

    protected void addOrPopItem(Player player) {
        if ((player.getInventory().getFreeSlot() == -1) && (player.getInventory().getSlotWithRemainingSpace(this.getItem()) == -1)) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getBlockPos().getX(), this.getBlockPos().above().getY(), this.getBlockPos().getZ(), this.getItem()));
        } else {
            player.getInventory().add(this.getItem());
        }
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