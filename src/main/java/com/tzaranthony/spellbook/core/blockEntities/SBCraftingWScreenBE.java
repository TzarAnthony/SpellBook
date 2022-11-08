package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.containers.handlers.FluidTankHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class SBCraftingWScreenBE extends SBCraftingBE implements MenuProvider, Nameable {
    private LockCode lockKey = LockCode.NO_LOCK;
    private Component name;

    public SBCraftingWScreenBE(BlockEntityType<?> type, BlockPos pos, BlockState state, RecipeType recipeType) {
        super(type, pos, state, recipeType);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.lockKey = LockCode.fromTag(tag);
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.lockKey.addToTag(tag);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    // processing generic
    protected static void transferToTank(SBCraftingWScreenBE SBBE, int idI, int idF) {
        ItemStack stack = SBBE.itemHandler.getStackInSlot(idI);
        if (!stack.isEmpty() && stack.getItem() instanceof BucketItem) {
            FluidStack fluid = new FluidStack(((BucketItem) stack.getItem()).getFluid(), 1000);
            int filled = 0;
            if (SBBE.fluidHandler.isFluidValid(idF, fluid)) {
                filled = SBBE.fluidHandler.fill(idF, fluid, IFluidHandler.FluidAction.EXECUTE);
            }

            if (filled > 0) {
                SBBE.itemHandler.setStackInSlot(idI, new ItemStack(Items.BUCKET));
            }
        }
    }

    // for menus and screens
    public FluidTank tankGetter(int tankId) {
        return this.fluidHandler.getTank(tankId);
    }

    public FluidTankHandler getFluidHandler() {
        return this.fluidHandler;
    }

    public ItemStackHandler getItemHandler() {
        return  this.itemHandler;
    }

    public void setFluidHandler(FluidTankHandler fluidTanks) {
        this.fluidHandler = fluidTanks;
    }

    public void setItemHandler(ItemStackHandler items) {
        this.itemHandler = items;
    }

    // menu stuff
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return this.canOpen(player) ? this.createMenu(id, inv) : null;
    }

    protected abstract AbstractContainerMenu createMenu(int id, Inventory inv);

    public boolean canOpen(Player player) {
        return canUnlock(player, this.lockKey, this.getDisplayName());
    }

    public static boolean canUnlock(Player player, LockCode locker, Component comp) {
        if (!player.isSpectator() && !locker.unlocksWith(player.getMainHandItem())) {
            player.displayClientMessage(new TranslatableComponent("container.isLocked", comp), true);
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    // naming
    public void setCustomName(Component p_58639_) {
        this.name = p_58639_;
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    protected abstract Component getDefaultName();

}