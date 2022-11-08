package com.tzaranthony.spellbook.client.entityRender.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.client.entityRender.renders.layers.NecroticSpiderEyesLayer;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.NecroticSpider;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NecroticSpiderRender extends MobRenderer<NecroticSpider, SpiderModel<NecroticSpider>> {
    private static final ResourceLocation loc = new ResourceLocation("spellbook:textures/entity/spider/necrotic_spider.png");
    private final float size = 1.8F;

    public NecroticSpiderRender(EntityRendererProvider.Context manager) {
        super(manager, new SpiderModel<>(manager.bakeLayer(ModelLayers.SPIDER)), 0.8F * 1.8F);
        this.addLayer(new NecroticSpiderEyesLayer(this));
    }

    protected float getFlipDegrees(NecroticSpider p_77037_1_) {
        return 180.0F;
    }

    protected void scale(NecroticSpider entity, PoseStack stack, float mult) {
        stack.scale(size, size, size);
    }

    public ResourceLocation getTextureLocation(NecroticSpider location) {
        return this.loc;
    }
}