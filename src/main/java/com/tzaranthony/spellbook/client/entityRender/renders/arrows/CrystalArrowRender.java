package com.tzaranthony.spellbook.client.entityRender.renders.arrows;

import com.tzaranthony.spellbook.core.entities.arrows.CrystalArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrystalArrowRender extends ArrowRenderer<CrystalArrow> {
    public static final ResourceLocation ARROW_LOCATION = new ResourceLocation("spellbook:textures/entity/projectiles/crystal_arrow.png");

    public CrystalArrowRender(EntityRendererProvider.Context p_174399_) {
        super(p_174399_);
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalArrow arrow) {
        return ARROW_LOCATION;
    }
}