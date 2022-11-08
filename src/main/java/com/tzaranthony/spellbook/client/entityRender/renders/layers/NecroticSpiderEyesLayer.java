package com.tzaranthony.spellbook.client.entityRender.renders.layers;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NecroticSpiderEyesLayer<T extends Entity, M extends SpiderModel<T>> extends SpiderEyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation("spellbook:textures/entity/spider/necrotic_spider_eyes.png"));

    public NecroticSpiderEyesLayer(RenderLayerParent<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}