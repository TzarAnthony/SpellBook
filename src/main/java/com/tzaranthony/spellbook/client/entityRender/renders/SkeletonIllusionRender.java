package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.core.entities.hostile.alchemical.SkeletonIllusion;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class SkeletonIllusionRender extends HumanoidMobRenderer<SkeletonIllusion, SkeletonModel<SkeletonIllusion>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SkeletonIllusionRender(EntityRendererProvider.Context manager) {
        super(manager, new SkeletonModel<>(manager.bakeLayer(ModelLayers.SKELETON)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(manager.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)), new SkeletonModel(manager.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR))));
    }

    public ResourceLocation getTextureLocation(SkeletonIllusion p_110775_1_) {
        return SKELETON_LOCATION;
    }
}