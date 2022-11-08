package com.tzaranthony.spellbook.core.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceRecipe implements Recipe<Container> {
    public static final ResourceLocation TYPE_ID = new ResourceLocation(SpellBook.MOD_ID, "alchemical_furnace");
    public static final RecipeType<AlchemicalFurnaceRecipe> TYPE = RecipeType.register(TYPE_ID.toString());

    private final ResourceLocation id;
    private final Ingredient itemInput;
    private final FluidStack fluidInput;
    private final ItemStack output;
    private final float experience;
    private final int cookingTime;

    public AlchemicalFurnaceRecipe(ResourceLocation id, Ingredient itemInput, FluidStack fluidInput, ItemStack output, float experience, int cookingTime) {
        this.id = id;
        this.itemInput = itemInput;
        this.fluidInput = fluidInput;
        this.output = output;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public boolean fluidsMatch(FluidStack stack) {
        return stack.containsFluid(fluidInput);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return itemInput.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.itemInput);
        return nonnulllist;
    }

    public FluidStack  getFluidInput() {
        return fluidInput;
    }


    public Ingredient getItemInput() {
        return itemInput;
    }

    public float getXp() {
        return experience;
    }

    public int getCookTime() {
        return cookingTime;
    }


    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlchemicalFurnaceRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public AlchemicalFurnaceRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            Ingredient iInput = Ingredient.fromJson(ingredients.get(0));
            FluidStack fInput = SBCraftingHelper.getFluid(json.get("fluid").getAsJsonObject());

            float xp = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int ct = GsonHelper.getAsInt(json, "cookingtime", 200);
            return new AlchemicalFurnaceRecipe(id, iInput, fInput, output, xp, ct);
        }

        @Nullable
        @Override
        public AlchemicalFurnaceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient iInput = Ingredient.fromNetwork(buffer);
            FluidStack fInput = FluidStack.readFromPacket(buffer);
            ItemStack output = buffer.readItem();
            float xp = buffer.readFloat();
            int ct = buffer.readVarInt();
            return new AlchemicalFurnaceRecipe(id, iInput, fInput, output, xp, ct);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlchemicalFurnaceRecipe recipe) {
            recipe.getItemInput().toNetwork(buffer);
            recipe.getFluidInput().writeToPacket(buffer);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeFloat(recipe.getXp());
            buffer.writeVarInt(recipe.getCookTime());
        }
    }
}