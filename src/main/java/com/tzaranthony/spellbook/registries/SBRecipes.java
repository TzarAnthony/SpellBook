package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.crafting.AlchemicalFoundryRecipe;
import com.tzaranthony.spellbook.core.crafting.AlchemicalFurnaceRecipe;
import com.tzaranthony.spellbook.core.crafting.RitualRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SBRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> reg = DeferredRegister.create(Registry.RECIPE_SERIALIZER_REGISTRY, SpellBook.MOD_ID);

    public static final RegistryObject<RecipeSerializer<AlchemicalFurnaceRecipe>> ALCHEMICAL_FURNACE = reg.register("alchemical_furnace"
            , () -> AlchemicalFurnaceRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<AlchemicalFoundryRecipe>> ALCHEMICAL_FOUNDRY = reg.register("alchemical_foundry"
            , () -> AlchemicalFoundryRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<RitualRecipe>> RITUAL = reg.register("ritual"
            , () -> RitualRecipe.Serializer.INSTANCE);
}