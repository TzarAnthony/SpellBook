package com.tzaranthony.spellbook.core.crafting;

import com.google.gson.JsonObject;
import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.containers.handlers.FluidTankHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Map;

public class AlchemicalFoundryRecipe implements IShapedRecipe<Container> {
    public static final ResourceLocation TYPE_ID = new ResourceLocation(SpellBook.MOD_ID, "alchemical_foundry");
    public static final RecipeType<AlchemicalFoundryRecipe> TYPE = RecipeType.register(TYPE_ID.toString());

    private final ResourceLocation id;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> itemInput;
    private final NonNullList<FluidStack> fluidInput;
    private final ItemStack catalyst;
    private final ItemStack output;
    private final float experience;
    private final int cookingTime;

    public AlchemicalFoundryRecipe(ResourceLocation id, int w, int h, NonNullList<Ingredient> itemInput, NonNullList<FluidStack>  fluidInput, ItemStack catalyst, ItemStack output, float experience, int cookingTime) {
        this.id = id;
        this.width = w;
        this.height = h;
        this.itemInput = itemInput;
        this.fluidInput = fluidInput;
        this.catalyst = catalyst;
        this.output = output;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }


    public boolean fluidsMatch(FluidTankHandler tanks) {
        boolean handlerMatches = true;
        boolean tankMatches;
        int fluidUsed = -1;
        for (int i = 0; i < tanks.getTanks(); i++) {
            tankMatches = false;
            for (int j = 0; j < fluidInput.size(); j++) {
                if (j == fluidUsed) {
                    continue;
                } else if (tanks.getFluidInTank(i).containsFluid(fluidInput.get(j)) || fluidInput.get(j).isEmpty()) {
                    tankMatches = true;
                    fluidUsed = j;
                    break;
                }
            }
            handlerMatches = handlerMatches && tankMatches;
        }
        return handlerMatches;
    }

    public boolean catalystMatches(ItemStack catalystIn) {
        return catalystIn.sameItem(getCatalystItem()) && catalystIn.getCount() >= getCatalystItem().getCount();
    }

    @Override
    public boolean matches(Container container, Level level) {
        for(int i = 0; i <= 3 - this.width; ++i) {
            for(int j = 0; j <= 2 - this.height; ++j) {
                if (this.matchesPattern(container, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(container, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesPattern(Container container, int slotI, int slotJ, boolean backwards) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 2; ++j) {
                int k = i - slotI;
                int l = j - slotJ;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (backwards) {
                        ingredient = this.getIngredients().get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.getIngredients().get(k + l * this.width);
                    }
                }
                if (!ingredient.test(container.getItem(i + j * 3))) {
                    return false;
                }
            }
        }

        return true;
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
    public int getRecipeWidth() {
        return this.width;
    }

    @Override
    public int getRecipeHeight() {
        return this.height;
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
        return this.itemInput;
    }

    public NonNullList<FluidStack>  getFluidInput() {
        return fluidInput;
    }

    public ItemStack getCatalystItem() {
        return catalyst;
    }

    public float getXp() {
        return experience;
    }

    public int getCookTime() {
        return cookingTime;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlchemicalFoundryRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public AlchemicalFoundryRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            ItemStack catalyst = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "catalyst"));

            Map<String, Ingredient> map = SBCraftingHelper.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = SBCraftingHelper.shrink(SBCraftingHelper.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern"), 2, 3));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> iInputs = SBCraftingHelper.dissolvePattern(astring, map, i, j);

            JsonObject fluids = GsonHelper.getAsJsonObject(json, "fluids");
            NonNullList<FluidStack> fInputs = NonNullList.withSize(2, FluidStack.EMPTY);

            fInputs.set(0, SBCraftingHelper.getFluid(fluids.get("fluid1").getAsJsonObject()));
            if (fluids.has("fluid2")) {
                fInputs.set(1, SBCraftingHelper.getFluid(fluids.get("fluid2").getAsJsonObject()));
            }

            float xp = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int ct = GsonHelper.getAsInt(json, "processingTime", 200);
            return new AlchemicalFoundryRecipe(id, i, j, iInputs, fInputs, catalyst, output, xp, ct);
        }

        public AlchemicalFoundryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();

            NonNullList<Ingredient> iInputs = NonNullList.withSize(i * j, Ingredient.EMPTY);
            for(int k = 0; k < iInputs.size(); ++k) {
                iInputs.set(k, Ingredient.fromNetwork(buffer));
            }

            NonNullList<FluidStack> fInputs = NonNullList.withSize(2, FluidStack.EMPTY);
            for(int k = 0; k < fInputs.size(); ++k) {
                fInputs.set(k, FluidStack.readFromPacket(buffer));
            }

            ItemStack catalyst = buffer.readItem();
            ItemStack output = buffer.readItem();
            float xp = buffer.readFloat();
            int ct = buffer.readVarInt();
            return new AlchemicalFoundryRecipe(id, i, j, iInputs, fInputs, catalyst, output, xp, ct);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlchemicalFoundryRecipe recipe) {
            buffer.writeVarInt(recipe.getRecipeWidth());
            buffer.writeVarInt(recipe.getRecipeHeight());

            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            for(FluidStack fStack : recipe.getFluidInput()) {
                fStack.writeToPacket(buffer);
            }

            buffer.writeItem(recipe.getCatalystItem());
            buffer.writeItem(recipe.getResultItem());
            buffer.writeFloat(recipe.getXp());
            buffer.writeVarInt(recipe.getCookTime());
        }
    }
}