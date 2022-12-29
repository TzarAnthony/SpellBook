package com.tzaranthony.spellbook.client.entityRender.renders.ghosts;

import com.tzaranthony.spellbook.client.entityRender.models.ShadeModel;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Shade;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShadeRender extends HumanoidMobRenderer<Shade, ShadeModel> {
    private static final ResourceLocation SHADE_1 = new ResourceLocation("spellbook:textures/entity/ghost/shade1.png");
    private static final ResourceLocation SHADE_2 = new ResourceLocation("spellbook:textures/entity/ghost/shade2.png");

    public ShadeRender(EntityRendererProvider.Context manager) {
        super(manager, new ShadeModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(Shade shade) {
        switch (shade.getVariant()) {
            case 1:
                return SHADE_1;
            default:
                return SHADE_2;
        }
    }
}