package com.tzaranthony.spellbook.client.entityRender.renders.ghosts;

import com.tzaranthony.spellbook.client.entityRender.models.PoltergeistModel;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Poltergeist;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoltergeistRender extends HumanoidMobRenderer<Poltergeist, PoltergeistModel> {
    private static final ResourceLocation GHOST_1 = new ResourceLocation("spellbook:textures/entity/ghost/ghost1.png");
    private static final ResourceLocation GHOST_2 = new ResourceLocation("spellbook:textures/entity/ghost/ghost2.png");
    private static final ResourceLocation GHOST_3 = new ResourceLocation("spellbook:textures/entity/ghost/ghost3.png");
    private static final ResourceLocation GHOST_4 = new ResourceLocation("spellbook:textures/entity/ghost/ghost4.png");

    public PoltergeistRender(EntityRendererProvider.Context manager) {
        super(manager, new PoltergeistModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(Poltergeist poltergeist) {
        switch (poltergeist.getVariant()) {
            case 1:
                return GHOST_1;
            case 2:
                return GHOST_2;
            case 3:
                return GHOST_3;
            default:
                return GHOST_4;
        }
    }
}