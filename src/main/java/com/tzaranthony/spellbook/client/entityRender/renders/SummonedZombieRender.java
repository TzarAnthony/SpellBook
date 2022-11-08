package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.client.entityRender.models.ZombieLikeModel;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedZombie;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SummonedZombieRender extends HumanoidMobRenderer<SummonedZombie, ZombieLikeModel<SummonedZombie>> {
    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("spellbook:textures/entity/summoned/zombie/summoned_zombie.png");

    public SummonedZombieRender(EntityRendererProvider.Context manager) {
        super(manager, new ZombieLikeModel<>(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(manager.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new SkeletonModel(manager.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR))));
    }

    @Override
    public ResourceLocation getTextureLocation(SummonedZombie entity) {
        return ZOMBIE_LOCATION;
    }
}