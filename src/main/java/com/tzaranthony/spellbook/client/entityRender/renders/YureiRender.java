package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.client.entityRender.models.YureiModel;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Yurei;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class YureiRender extends HumanoidMobRenderer<Yurei, YureiModel> {
    private static final ResourceLocation GHOST_1 = new ResourceLocation("spellbook:textures/entity/ghost/ghost1.png");
    private static final ResourceLocation GHOST_2 = new ResourceLocation("spellbook:textures/entity/ghost/ghost2.png");
    private static final ResourceLocation GHOST_3 = new ResourceLocation("spellbook:textures/entity/ghost/ghost3.png");
    private static final ResourceLocation GHOST_4 = new ResourceLocation("spellbook:textures/entity/ghost/ghost4.png");

    public YureiRender(EntityRendererProvider.Context manager) {
        super(manager, new YureiModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(Yurei yurei) {
        switch (yurei.getVariant()) {
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