package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.client.entityRender.models.WraithModel;
import com.tzaranthony.spellbook.client.entityRender.renders.layers.WraithClothingLayer;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Wraith;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraithRender extends HumanoidMobRenderer<Wraith, WraithModel<Wraith>> {
    private static final ResourceLocation WRAITH = new ResourceLocation("spellbook:textures/entity/ghost/wraith_body.png");

    public WraithRender(EntityRendererProvider.Context manager) {
        super(manager, new WraithModel(manager.bakeLayer(ModelLayers.SKELETON)), 0.5F);
        this.addLayer(new WraithClothingLayer<>(this, manager.getModelSet()));
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(Wraith wraith) {
        return WRAITH;
    }
}