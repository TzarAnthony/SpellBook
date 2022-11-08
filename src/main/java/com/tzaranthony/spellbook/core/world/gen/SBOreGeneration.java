package com.tzaranthony.spellbook.core.world.gen;

import com.tzaranthony.spellbook.core.world.features.SBFeaturePlacements;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class SBOreGeneration {
    public static void generateOres(final BiomeLoadingEvent event) {
        List<Holder<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        base.add(SBFeaturePlacements.ORE_SILVER_NEAR_COPPER);
        base.add(SBFeaturePlacements.ORE_SILVER_NEAR_IRON);
        base.add(SBFeaturePlacements.ORE_CINNABAR);
    }
}