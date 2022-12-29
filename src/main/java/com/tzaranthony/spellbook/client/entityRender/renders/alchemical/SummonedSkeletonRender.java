package com.tzaranthony.spellbook.client.entityRender.renders.alchemical;

import com.tzaranthony.spellbook.core.entities.friendly.SBAbstractFriendlySkeleton;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SummonedSkeletonRender extends HumanoidMobRenderer<SBAbstractFriendlySkeleton, SkeletonModel<SBAbstractFriendlySkeleton>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("spellbook:textures/entity/summoned/skeleton/summoned_skeleton.png");

    public SummonedSkeletonRender(EntityRendererProvider.Context manager) {
        super(manager, new SkeletonModel<>(manager.bakeLayer(ModelLayers.SKELETON)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(manager.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)), new SkeletonModel(manager.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR))));
    }

    public ResourceLocation getTextureLocation(SBAbstractFriendlySkeleton p_110775_1_) {
        return SKELETON_LOCATION;
    }
}