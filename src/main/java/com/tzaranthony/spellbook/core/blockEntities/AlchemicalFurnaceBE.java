package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.containers.handlers.FluidTankHandler;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFurnaceMenu;
import com.tzaranthony.spellbook.core.crafting.AlchemicalFurnaceRecipe;
import com.tzaranthony.spellbook.core.network.FluidS2CPacket;
import com.tzaranthony.spellbook.core.network.ItemS2CPacket;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import com.tzaranthony.spellbook.registries.SBPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AlchemicalFurnaceBE extends ScreenCraftingBE {
    protected int litTime;
    protected int litDuration;
    private final ContainerData dataAccess = new ContainerData() {
        public int get(int id) {
            switch(id) {
                case 0:
                    return AlchemicalFurnaceBE.this.litTime;
                case 1:
                    return AlchemicalFurnaceBE.this.litDuration;
                case 2:
                    return AlchemicalFurnaceBE.this.progress;
                case 3:
                    return AlchemicalFurnaceBE.this.maxTime;
                default:
                    return 0;
            }
        }

        public void set(int id, int value) {
            switch(id) {
                case 0:
                    AlchemicalFurnaceBE.this.litTime = value;
                    break;
                case 1:
                    AlchemicalFurnaceBE.this.litDuration = value;
                    break;
                case 2:
                    AlchemicalFurnaceBE.this.progress = value;
                    break;
                case 3:
                    AlchemicalFurnaceBE.this.maxTime = value;
            }
        }

        public int getCount() {
            return 4;
        }
    };

    public AlchemicalFurnaceBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.ALCHEMICAL_FURNACE.get(), pos, state, AlchemicalFurnaceRecipe.TYPE);
        this.itemHandler = new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    SBPackets.sendToClients(new ItemS2CPacket(this, worldPosition));
                }
            }
        };
        this.fluidHandler = new FluidTankHandler(1, 8000) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    SBPackets.sendToClients(new FluidS2CPacket(this, worldPosition));
                }
            }
        };
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.spellbook.alchemical_furnace");
    }

    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new AlchemicalFurnaceMenu(id, inv, this.getBlockPos(), this.dataAccess);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.litTime = tag.getInt("BurnTime");
        this.litDuration = this.getBurnDuration(this.itemHandler.getStackInSlot(1));
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", this.litTime);
        CompoundTag compoundtag = new CompoundTag();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlchemicalFurnaceBE AlcBE) {
        transferToTank(AlcBE, AlchemicalFurnaceMenu.FluidSlotId, 0);

        boolean flag = AlcBE.isLit();
        boolean flag1 = false;
        if (AlcBE.isLit()) {
            --AlcBE.litTime;
        }

        ItemStack fuel = AlcBE.itemHandler.getStackInSlot(AlchemicalFurnaceMenu.FuelSlotId);
        if (AlcBE.isLit() || !fuel.isEmpty()) {
            AlchemicalFurnaceRecipe recipe = level.getRecipeManager().getRecipeFor(AlchemicalFurnaceRecipe.TYPE, AlcBE.createContainer(), level).orElse(null);
            if (recipe != null && recipe.fluidsMatch(AlcBE.fluidHandler.getTank(0).getFluid())) {
                AlcBE.maxTime = getTotalCookTime(recipe);
                if (!AlcBE.isLit() && AlcBE.canBurn(recipe)) {
                    // light
                    AlcBE.litTime = AlcBE.getBurnDuration(fuel);
                    AlcBE.litDuration = AlcBE.litTime;
                    // fuel check
                    if (AlcBE.isLit()) {
                        flag1 = true;
                        if (fuel.hasContainerItem())
                            AlcBE.itemHandler.setStackInSlot(AlchemicalFurnaceMenu.FuelSlotId, fuel.getContainerItem());
                        else if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);
                            if (fuel.isEmpty()) {
                                AlcBE.itemHandler.setStackInSlot(AlchemicalFurnaceMenu.FuelSlotId, fuel.getContainerItem());
                            }
                        }
                    }
                }

                if (AlcBE.isLit() && AlcBE.canBurn(recipe)) {
                    ++AlcBE.progress;
                    if (AlcBE.progress >= AlcBE.maxTime) {
                        AlcBE.progress = 0;
                        AlcBE.burn(recipe);
                        flag1 = true;
                    }
                } else {
                    AlcBE.progress = 0;
                }
            }
        } else if (!AlcBE.isLit() && AlcBE.progress > 0) {
            AlcBE.progress = Mth.clamp(AlcBE.progress - 2, 0, AlcBE.maxTime);
        }

        if (flag != AlcBE.isLit()) {
            flag1 = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(AlcBE.isLit()));
            level.setBlock(pos, state, 3);
        }
        if (flag1) {
            setChanged(level, pos, state);
        }
    }

    private boolean canBurn(AlchemicalFurnaceRecipe recipe) {
        if (itemHandler.getStackInSlot(AlchemicalFurnaceMenu.FuelSlotId - 1).isEmpty()) {
            return false;
        }

        return checkResult(recipe.assemble(this.createContainer())
                ,itemHandler.getStackInSlot(AlchemicalFurnaceMenu.XPResultSlotId)
                ,this.itemHandler.getSlotLimit(AlchemicalFurnaceMenu.XPResultSlotId)
        );
    }

    private boolean burn(AlchemicalFurnaceRecipe recipe) {
        if (this.canBurn(recipe)) {
            itemHandler.extractItem(AlchemicalFurnaceMenu.FuelSlotId - 1, 1, false);
            fluidHandler.drain(0, recipe.getFluidInput(), IFluidHandler.FluidAction.EXECUTE);

            int count = itemHandler.getStackInSlot(AlchemicalFurnaceMenu.XPResultSlotId).getCount() + recipe.getResultItem().getCount();
            itemHandler.setStackInSlot(AlchemicalFurnaceMenu.XPResultSlotId, new ItemStack(recipe.getResultItem().getItem(), count));


//            int count2 = Math.min(64, level.random.nextInt(3) + itemHandler.getStackInSlot(AlchemicalFurnaceMenu.ResultSlotId).getCount());
//            itemHandler.setStackInSlot(AlchemicalFurnaceMenu.ResultSlotId, new ItemStack(SBItems.ALCHEMICAL_RESIDUE.get(), count2));
            return true;
        } else {
            return false;
        }
    }

    private static int getTotalCookTime(AlchemicalFurnaceRecipe recipe) {
        return recipe.getCookTime() > 0 ? recipe.getCookTime() : 150;
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    public int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
        }
    }
}