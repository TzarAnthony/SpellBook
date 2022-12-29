package com.tzaranthony.spellbook.client.entityRender.renders.alchemical;

import com.tzaranthony.spellbook.client.entityRender.models.ZombieLikeModel;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.ZombieIllusion;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZombieIllusionRender extends HumanoidMobRenderer<ZombieIllusion, ZombieLikeModel<ZombieIllusion>> {
    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    public ZombieIllusionRender(EntityRendererProvider.Context manager) {
        super(manager, new ZombieLikeModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(manager.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new SkeletonModel(manager.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR))));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieIllusion entity) {
        return ZOMBIE_LOCATION;
    }
}