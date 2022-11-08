package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.crafting.RitualRecipe;
import com.tzaranthony.spellbook.core.network.ItemS2CPacket;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import com.tzaranthony.spellbook.registries.SBBlocks;
import com.tzaranthony.spellbook.registries.SBPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class AlterBE extends SBCraftingBE {
    public static int tier;
    protected boolean isCrafting;
    protected NonNullList<LivingEntity> boundEntitites;

    public AlterBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.ALTER.get(), pos, state, RitualRecipe.TYPE);
        this.itemHandler = new ItemStackHandler(21) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    SBPackets.sendToClients(new ItemS2CPacket(this, worldPosition));
                }
            }
        };
        this.maxTime = 200;
    }

    public void setTier() {
        if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_1.get())) {
            this.tier = 0;
        } else if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_2.get())) {
            this.tier = 1;
        } else if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_3.get())) {
            this.tier = 2;
        }
    }

    public void startCrafting() {
        this.isCrafting = true;
    }

    public boolean checkMultiBlock() {
        assert this.level != null;
        switch (tier) {
            case 1:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier2_fmt, AlterTiers.tier2_map, this.level, this.getBlockPos().above());
            case 2:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier3_fmt, AlterTiers.tier3_map, this.level, this.getBlockPos().above())
                    || MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier3_fmt_1, AlterTiers.tier3_map, this.level, this.getBlockPos().above());
            default:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier1_fmt, AlterTiers.tier1_map, this.level, this.getBlockPos().above());
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlterBE AltBe) {
        boolean flag1 = false;
        if (AltBe.isCrafting) {
            RitualRecipe recipe = level.getRecipeManager().getRecipeFor(RitualRecipe.TYPE, AltBe.createContainer(), level).orElse(null);
            if (recipe != null && recipe.getAlterTier() <= AltBe.tier && recipe.getMobCount() == AltBe.boundEntitites.size()) {
                if (AltBe.checkMultiBlock() && AltBe.canCraft(recipe)) {
                    ++AltBe.progress;
                    if (AltBe.progress >= AltBe.maxTime) {
                        AltBe.progress = 0;
                        AltBe.craft(recipe);
                        flag1 = true;
                    }
                } else {
                    AltBe.progress = 0;
                }
            }
        } else if (!AltBe.checkMultiBlock() && AltBe.progress > 0) {
            AltBe.progress = Mth.clamp(AltBe.progress - 2, 0, AltBe.maxTime);
        }
        if (flag1) {
            setChanged(level, pos, state);
        }
    }

    private boolean canCraft(RitualRecipe recipe) {
        boolean hasStuff = false;
        for (int i = 0; i < itemHandler.getSlots() - 1; ++i) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                hasStuff = hasStuff || true;
                break;
            }
        }

        return hasStuff && checkResult(recipe.assemble(this.createContainer()), itemHandler.getStackInSlot(itemHandler.getSlots() - 1), this.itemHandler.getSlotLimit(itemHandler.getSlots() - 1));
    }

    private void craft(RitualRecipe recipe) {
        NonNullList<Ingredient> ing = recipe.getIngredients();
        for (int i = 0; i < itemHandler.getSlots() - 1; ++i) {
            ItemStack currItm = itemHandler.getStackInSlot(i);
            for (int j = 0; j < ing.size(); ++j) {
                for (ItemStack ingStack : ing.get(i).getItems()) {
                    if (currItm.is(ingStack.getItem()) && currItm.getCount() >= ingStack.getCount()) {
                        itemHandler.extractItem(i, ingStack.getCount(), false);
                    }
                }
            }
        }

        int count = itemHandler.getStackInSlot(itemHandler.getSlots() - 1).getCount() + recipe.getResultItem().getCount();
        itemHandler.setStackInSlot(itemHandler.getSlots() - 1, new ItemStack(recipe.getResultItem().getItem(), count));
    }
}