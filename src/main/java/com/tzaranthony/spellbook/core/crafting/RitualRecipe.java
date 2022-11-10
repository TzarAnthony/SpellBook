package com.tzaranthony.spellbook.core.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RitualRecipe implements Recipe<Container> {
    public static final ResourceLocation TYPE_ID = new ResourceLocation(SpellBook.MOD_ID, "ritual");
    public static final RecipeType<RitualRecipe> TYPE = RecipeType.register(TYPE_ID.toString());

    private final ResourceLocation id;
    private final NonNullList<ItemStack> itemInput;
    private final ItemStack output;
    private final int alterTier;
    private final int mobCount;
    private final int mp;

    public RitualRecipe(ResourceLocation id, NonNullList<ItemStack> itemInput, ItemStack output, int alterTier, int mobCount, int mp) {
        this.id = id;
        this.itemInput = itemInput;
        this.output = output;
        this.alterTier = alterTier;
        this.mobCount = mobCount;
        this.mp = mp;
    }

    @Override
    public boolean matches(Container container, Level level) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;
        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }
        return i == this.itemInput.size() && SBCraftingHelper.findMatches(inputs, this.itemInput);
    }

    @Override
    public ItemStack assemble(Container container) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
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
        return NonNullList.create();
    }

    public NonNullList<ItemStack> getInputs() {
        return this.itemInput;
    }

    public int getAlterTier() {
        return this.alterTier;
    }

    public int getMobCount() {
        return this.mobCount;
    }

    public int getMp() {
        return this.mp;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RitualRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public RitualRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            NonNullList<ItemStack> iInputs = SBCraftingHelper.itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (iInputs.isEmpty()) {
                throw new JsonParseException("No ingredients for ritual.");
            }

            int alterTier = GsonHelper.getAsInt(json, "tier", 0);
            int mobCount = GsonHelper.getAsInt(json, "mobs", 0);
            int mp = GsonHelper.getAsInt(json, "mp", 0);
            return new RitualRecipe(id, iInputs, output, alterTier, mobCount, mp);
        }

        public RitualRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            NonNullList<ItemStack> iInputs = NonNullList.create();
            for(int k = 0; k < iInputs.size(); ++k) {
                iInputs.add(buffer.readItem());
            }
            ItemStack output = buffer.readItem();
            int alterTier = buffer.readVarInt();
            int mobCount = buffer.readVarInt();
            int mp = buffer.readVarInt();
            return new RitualRecipe(id, iInputs, output, alterTier, mobCount, mp);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RitualRecipe recipe) {
            for(ItemStack ingredient : recipe.getInputs()) {
                buffer.writeItem(ingredient);
            }
            buffer.writeItem(recipe.getResultItem());
            buffer.writeVarInt(recipe.getAlterTier());
            buffer.writeVarInt(recipe.getMobCount());
            buffer.writeVarInt(recipe.getMp());
        }
    }
}