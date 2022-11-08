package com.tzaranthony.spellbook;

import com.tzaranthony.spellbook.core.crafting.SBBrewingRecipe;
import com.tzaranthony.spellbook.core.world.features.SBFeatures;
import com.tzaranthony.spellbook.registries.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("spellbook")
public class SpellBook {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "spellbook";
    public static boolean curiosLoaded = false;
    public static boolean isUsingMixin;

    public SpellBook() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::CommonSetup);
        bus.addListener(this::ClientSetup);
        bus.addListener(this::CompleteSetup);

        SBBlocks.reg.register(bus);
        SBEntities.reg.register(bus);
        SBItems.reg.register(bus);
        SBBlockEntities.reg.register(bus);
        SBFeatures.reg.register((bus));
        SBParticleTypes.reg.register(bus);
        SBEffects.reg.register(bus);
        SBPotions.reg.register(bus);
        SBMenus.reg.register(bus);
        SBRecipes.reg.register(bus);
        SBFluids.registerFluids();
        SBVillagers.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void CommonSetup(final FMLCommonSetupEvent event) {
        SBPackets.registerPackets();
    }

    private void ClientSetup(final FMLClientSetupEvent event) {
        SBBlockRender.renderBlocks();
        SBBlockEntityRender.renderBlockEntities();
        SBEntityRender.renderEntities();
        SBItemPropertiesRender.renderItemProperties();
        SBScreenRender.renderScreens();
    }

    private void CompleteSetup(final FMLLoadCompleteEvent event) {
        BrewingRecipeRegistry.addRecipe(new SBBrewingRecipe(Potions.MUNDANE, SBItems.CINNABAR_FRAGMENT.get(), Potions.EMPTY, SBItems.BOTTLE_OF_MERCURY.get()));
        SBVillagers.registerPOIs();
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("SpellBook") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SBItems.LIBER_EXPONENTIA_CREATIVE.get());
        }
    };
    public static final CreativeModeTab SPELL_TAB = new CreativeModeTab("SpellDebug") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SBItems.SUMMONING_2_DEBUG.get());
        }
    };
}