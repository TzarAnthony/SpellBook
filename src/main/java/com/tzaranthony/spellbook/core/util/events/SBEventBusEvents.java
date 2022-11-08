package com.tzaranthony.spellbook.core.util.events;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.client.particle.BloodDripParticle;
import com.tzaranthony.spellbook.client.particle.ScreamParticle;
import com.tzaranthony.spellbook.core.crafting.AlchemicalFoundryRecipe;
import com.tzaranthony.spellbook.core.crafting.AlchemicalFurnaceRecipe;
import com.tzaranthony.spellbook.core.crafting.RitualRecipe;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpellBook.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SBEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(SBParticleTypes.END_FIRE_FLAME.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(SBParticleTypes.BLOOD_DRIP.get(), BloodDripParticle.BloodFlowerHangProvider::new);
        Minecraft.getInstance().particleEngine.register(SBParticleTypes.BLOOD_FALL.get(), BloodDripParticle.BloodFlowerFallProvider::new);
        Minecraft.getInstance().particleEngine.register(SBParticleTypes.BLOOD_LAND.get(), BloodDripParticle.BloodFlowerLandProvider::new);
        Minecraft.getInstance().particleEngine.register(SBParticleTypes.SCREAM.get(), ScreamParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, AlchemicalFurnaceRecipe.TYPE_ID, AlchemicalFurnaceRecipe.TYPE);
        Registry.register(Registry.RECIPE_TYPE, AlchemicalFoundryRecipe.TYPE_ID, AlchemicalFoundryRecipe.TYPE);
        Registry.register(Registry.RECIPE_TYPE, RitualRecipe.TYPE_ID, RitualRecipe.TYPE);
    }
}