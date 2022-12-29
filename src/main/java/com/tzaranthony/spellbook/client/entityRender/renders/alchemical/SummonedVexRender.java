package com.tzaranthony.spellbook.client.entityRender.renders.alchemical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.client.entityRender.models.SummonedVexModel;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedVex;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SummonedVexRender extends HumanoidMobRenderer<SummonedVex, SummonedVexModel> {
    private static final ResourceLocation VEX_LOCATION = new ResourceLocation("spellbook:textures/entity/summoned/vex/summoned_vex.png");
    private static final ResourceLocation VEX_CHARGING_LOCATION = new ResourceLocation("spellbook:textures/entity/summoned/vex/summoned_vex_charging.png");

    public SummonedVexRender(EntityRendererProvider.Context manager) {
        super(manager, new SummonedVexModel(manager.bakeLayer(ModelLayers.VEX)), 0.3F);
    }

    protected int getBlockLightLevel(SummonedVex p_225624_1_, BlockPos p_225624_2_) {
        return 15;
    }

    public ResourceLocation getTextureLocation(SummonedVex vex) {
        return vex.isCharging() ? VEX_CHARGING_LOCATION : VEX_LOCATION;
    }

    protected void scale(SummonedVex vex, PoseStack pose, float p_225620_3_) {
        pose.scale(0.4F, 0.4F, 0.4F);
    }
}