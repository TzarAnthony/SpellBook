package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.core.entities.arrows.SilverArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SilverArrowRender extends ArrowRenderer<SilverArrow> {
    public static final ResourceLocation ARROW_LOCATION = new ResourceLocation("spellbook:textures/entity/projectiles/silver_arrow.png");

    public SilverArrowRender(EntityRendererProvider.Context p_174399_) {
        super(p_174399_);
    }

    @Override
    public ResourceLocation getTextureLocation(SilverArrow arrow) {
        return ARROW_LOCATION;
    }
}