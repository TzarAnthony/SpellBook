package com.tzaranthony.spellbook.client.entityRender.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.client.entityRender.models.HigherVampireBatModel;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampireBat;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HigherVampireBatRender extends MobRenderer<HigherVampireBat, HigherVampireBatModel> {
    private static final ResourceLocation VAMP = new ResourceLocation("spellbook:textures/entity/vampire/higher/vampire_bat.png");

    public HigherVampireBatRender(EntityRendererProvider.Context manager) {
        super(manager, new HigherVampireBatModel(manager.bakeLayer(ModelLayers.BAT)), 0.25F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(HigherVampireBat higherVampire) {
        return VAMP;
    }

    protected void scale(HigherVampireBat vamp_bat, PoseStack pose, float scale) {
        pose.scale(0.35F, 0.35F, 0.35F);
    }

    protected void setupRotations(HigherVampireBat vamp_bat, PoseStack pose, float p_113884_, float p_113885_, float p_113886_) {
        pose.translate(0.0D, (double)(Mth.cos(p_113884_ * 0.3F) * 0.1F), 0.0D);
        super.setupRotations(vamp_bat, pose, p_113884_, p_113885_, p_113886_);
    }
}