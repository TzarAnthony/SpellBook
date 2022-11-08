package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.containers.handlers.FluidTankHandler;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFoundryMenu;
import com.tzaranthony.spellbook.core.crafting.AlchemicalFoundryRecipe;
import com.tzaranthony.spellbook.core.network.FluidS2CPacket;
import com.tzaranthony.spellbook.core.network.ItemS2CPacket;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import com.tzaranthony.spellbook.registries.SBItems;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AlchemicalFoundryBE extends SBCraftingWScreenBE {
    protected int litTime;
    protected int litDuration;
    private final ContainerData dataAccess = new ContainerData() {
        public int get(int id) {
            switch(id) {
                case 0:
                    return AlchemicalFoundryBE.this.litTime;
                case 1:
                    return AlchemicalFoundryBE.this.litDuration;
                case 2:
                    return AlchemicalFoundryBE.this.progress;
                case 3:
                    return AlchemicalFoundryBE.this.maxTime;
                default:
                    return 0;
            }
        }

        public void set(int id, int value) {
            switch(id) {
                case 0:
                    AlchemicalFoundryBE.this.litTime = value;
                    break;
                case 1:
                    AlchemicalFoundryBE.this.litDuration = value;
                    break;
                case 2:
                    AlchemicalFoundryBE.this.progress = value;
                    break;
                case 3:
                    AlchemicalFoundryBE.this.maxTime = value;
            }
        }

        public int getCount() {
            return 4;
        }
    };

    public AlchemicalFoundryBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.ALCHEMICAL_FOUNDRY.get(), pos, state, AlchemicalFoundryRecipe.TYPE);
        this.itemHandler = new ItemStackHandler(12) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    SBPackets.sendToClients(new ItemS2CPacket(this, worldPosition));
                }
            }
        };
        this.fluidHandler = new FluidTankHandler(2, 8000) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    SBPackets.sendToClients(new FluidS2CPacket(this, worldPosition));
                }
            }
        };
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.spellbook.alchemical_foundry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new AlchemicalFoundryMenu(id, inv, this.getBlockPos(), this.dataAccess);
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemicalFoundryBE AlcBE) {
        transferToTank(AlcBE, AlchemicalFoundryMenu.FluidSlotId1, 0);
        transferToTank(AlcBE, AlchemicalFoundryMenu.FluidSlotId2, 1);

        boolean flag = AlcBE.isLit();
        boolean flag1 = false;
        if (AlcBE.isLit()) {
            --AlcBE.litTime;
        }

        ItemStack fuel = AlcBE.itemHandler.getStackInSlot(AlchemicalFoundryMenu.FuelSlotId);
        ItemStack catalyst = AlcBE.itemHandler.getStackInSlot(AlchemicalFoundryMenu.CatalystSlotId);
        if (AlcBE.isLit() || !fuel.isEmpty()) {
            AlchemicalFoundryRecipe recipe = level.getRecipeManager().getRecipeFor(AlchemicalFoundryRecipe.TYPE, AlcBE.createContainer(), level).orElse(null);
            if (recipe != null && recipe.fluidsMatch(AlcBE.fluidHandler) && recipe.catalystMatches(catalyst)) {
                AlcBE.maxTime = getTotalCookTime(recipe);
                if (!AlcBE.isLit() && AlcBE.canBurn(recipe)) {
                    // light
                    AlcBE.litTime = AlcBE.getBurnDuration(fuel);
                    AlcBE.litDuration = AlcBE.litTime;
                    // fuel check
                    if (AlcBE.isLit()) {
                        flag1 = true;
                        if (fuel.hasContainerItem())
                            AlcBE.itemHandler.setStackInSlot(AlchemicalFoundryMenu.FuelSlotId, fuel.getContainerItem());
                        else if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);
                            if (fuel.isEmpty()) {
                                AlcBE.itemHandler.setStackInSlot(AlchemicalFoundryMenu.FuelSlotId, fuel.getContainerItem());
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

    private boolean canBurn(AlchemicalFoundryRecipe recipe) {
        boolean hasStuff = false;
        for (int i = 0; i < AlchemicalFoundryMenu.CatalystSlotId; ++i) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                hasStuff = hasStuff || true;
            }
        }

        return hasStuff && checkResult(recipe.assemble(this.createContainer())
                , itemHandler.getStackInSlot(AlchemicalFoundryMenu.XPResultSlotId)
                , this.itemHandler.getSlotLimit(AlchemicalFoundryMenu.XPResultSlotId)
        );
    }

    private boolean burn(AlchemicalFoundryRecipe recipe) {
        if (this.canBurn(recipe)) {
            for (int i = 0; i < AlchemicalFoundryMenu.CatalystSlotId; ++i) {
                itemHandler.extractItem(i, 1, false);
            }
            itemHandler.extractItem(AlchemicalFoundryMenu.CatalystSlotId, recipe.getCatalystItem().getCount(), false);

            int fluidUsed = -1;
            for (int i = 0; i < fluidHandler.getTanks(); ++i) {
                FluidStack fluidStack = fluidHandler.getTank(i).getFluid();
                for (int j = 0; j < recipe.getFluidInput().size(); ++j) {
                    if (fluidStack.containsFluid(recipe.getFluidInput().get(j)) && fluidUsed != j) {
                        fluidHandler.drain(i, recipe.getFluidInput().get(j), IFluidHandler.FluidAction.EXECUTE);
                        fluidUsed = j;
                        break;
                    }
                }
            }

            int count = itemHandler.getStackInSlot(AlchemicalFoundryMenu.XPResultSlotId).getCount() + recipe.getResultItem().getCount();
            itemHandler.setStackInSlot(AlchemicalFoundryMenu.XPResultSlotId, new ItemStack(recipe.getResultItem().getItem(), count));


            int count2 = Math.min(64, level.random.nextInt(3) + itemHandler.getStackInSlot(AlchemicalFoundryMenu.ResultSlotId).getCount());
            itemHandler.setStackInSlot(AlchemicalFoundryMenu.ResultSlotId, new ItemStack(SBItems.ALCHEMICAL_RESIDUE.get(), count2));
            return true;
        } else {
            return false;
        }
    }

    private static int getTotalCookTime(AlchemicalFoundryRecipe recipe) {
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