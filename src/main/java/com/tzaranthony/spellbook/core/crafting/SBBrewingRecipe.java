package com.tzaranthony.spellbook.core.crafting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class SBBrewingRecipe implements IBrewingRecipe {

    private final Potion input;
    private final Item ingredient;
    private final Potion POutput;
    private final Item IOutput;

    public SBBrewingRecipe(Potion input, Item ingredient, Potion POutput, Item IOutput) {
        this.input = input;
        this.ingredient = ingredient;
        this.POutput = POutput;
        this.IOutput = IOutput;
    }

    @Override
    public boolean isInput(ItemStack input) {
        return PotionUtils.getPotion(input) == this.input;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem() == this.ingredient;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!this.isInput(input) || !this.isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        if (this.POutput == Potions.EMPTY) {
            return new ItemStack(this.IOutput);
        }

        ItemStack stack = new ItemStack(input.getItem());
        stack.setTag(new CompoundTag());
        PotionUtils.setPotion(stack, this.POutput);
        return stack;
    }
}