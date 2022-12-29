package com.tzaranthony.spellbook.client.entityRender.renders.ghosts.boss;

import com.tzaranthony.spellbook.client.entityRender.models.GhostArcherModel;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostArcher;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhostArcherRender extends HumanoidMobRenderer<GhostArcher, GhostArcherModel> {
    private static final ResourceLocation GHOST_1 = new ResourceLocation("spellbook:textures/entity/ghost/ghost1.png");
    private static final ResourceLocation GHOST_4 = new ResourceLocation("spellbook:textures/entity/ghost/ghost4.png");

    public GhostArcherRender(EntityRendererProvider.Context manager) {
        super(manager, new GhostArcherModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(GhostArcher ghost) {
        switch (ghost.getVariant()) {
            case 1:
                return GHOST_1;
            default:
                return GHOST_4;
        }
    }
}