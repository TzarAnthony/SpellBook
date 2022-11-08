package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.world.gen.SBOreGeneration;
import com.tzaranthony.spellbook.core.world.gen.SBPlantGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpellBook.MOD_ID)
public class SBWorldGeneration {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        SBOreGeneration.generateOres(event);
        SBPlantGeneration.generatePlants(event);
    }
}