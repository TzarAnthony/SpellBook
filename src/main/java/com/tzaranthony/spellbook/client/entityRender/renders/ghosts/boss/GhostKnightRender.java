package com.tzaranthony.spellbook.client.entityRender.renders.ghosts.boss;

import com.tzaranthony.spellbook.client.entityRender.models.GhostKnightModel;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostKnight;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhostKnightRender extends HumanoidMobRenderer<GhostKnight, GhostKnightModel> {
    private static final ResourceLocation GHOST_1 = new ResourceLocation("spellbook:textures/entity/ghost/ghost2.png");
    private static final ResourceLocation GHOST_2 = new ResourceLocation("spellbook:textures/entity/ghost/ghost3.png");

    public GhostKnightRender(EntityRendererProvider.Context manager) {
        super(manager, new GhostKnightModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(GhostKnight ghost) {
        switch (ghost.getVariant()) {
            case 1:
                return GHOST_1;
            default:
                return GHOST_2;
        }
    }
}