package com.tzaranthony.spellbook.client.entityRender.renders.ghosts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.client.entityRender.models.GhostHorseModel;
import com.tzaranthony.spellbook.core.entities.neutral.GhostHorse;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhostHorseRender extends MobRenderer<GhostHorse, GhostHorseModel<GhostHorse>> {
    private static final ResourceLocation GHOST_1 = new ResourceLocation("spellbook:textures/entity/ghost/horse/ghost_horse_1.png");
    private static final ResourceLocation GHOST_2 = new ResourceLocation("spellbook:textures/entity/ghost/horse/ghost_horse_2.png");
    private static final ResourceLocation GHOST_3 = new ResourceLocation("spellbook:textures/entity/ghost/horse/ghost_horse_3.png");
    private final float scale;

    public GhostHorseRender(EntityRendererProvider.Context manager) {
        super(manager, new GhostHorseModel<>(manager.bakeLayer(ModelLayers.HORSE)), 0.75F);
        this.scale = 1.0F;
        this.shadowRadius = 0;
    }

    protected void scale(GhostHorse p_113754_, PoseStack p_113755_, float p_113756_) {
        p_113755_.scale(this.scale, this.scale, this.scale);
        super.scale(p_113754_, p_113755_, p_113756_);
    }

    public ResourceLocation getTextureLocation(GhostHorse horse) {
        switch (horse.getVariant()) {
            case 1:
                return GHOST_2;
            case 2:
                return GHOST_3;
            default:
                return GHOST_1;
        }
    }
}