package com.tzaranthony.spellbook.client.entityRender.renders.alchemical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.core.entities.friendly.SBAbstractFriendlySkeleton;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SummonedWitherSkeletonRender extends SummonedSkeletonRender {
    private static final ResourceLocation WITHER_SKELETON_LOCATION = new ResourceLocation("spellbook:textures/entity/summoned/skeleton/summoned_wither_skeleton.png");

    public SummonedWitherSkeletonRender(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(SBAbstractFriendlySkeleton p_110775_1_) {
        return WITHER_SKELETON_LOCATION;
    }

    protected void scale(SBAbstractFriendlySkeleton p_225620_1_, PoseStack p_225620_2_, float p_225620_3_) {
        p_225620_2_.scale(1.2F, 1.2F, 1.2F);
    }
}