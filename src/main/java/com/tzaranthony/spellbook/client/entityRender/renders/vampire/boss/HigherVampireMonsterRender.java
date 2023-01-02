package com.tzaranthony.spellbook.client.entityRender.renders.vampire.boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.client.entityRender.models.HigherVampireMonsterModel;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampirePhase2;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HigherVampireMonsterRender extends MobRenderer<HigherVampirePhase2, HigherVampireMonsterModel> {
    private static final ResourceLocation VAMP = new ResourceLocation("spellbook:textures/entity/vampire/higher/vampire_monster.png");

    public HigherVampireMonsterRender(EntityRendererProvider.Context context) {
        super(context, new HigherVampireMonsterModel(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    public ResourceLocation getTextureLocation(HigherVampirePhase2 vampire) {
        return VAMP;
    }

    protected void setupRotations(HigherVampirePhase2 vampire, PoseStack p_115015_, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(vampire, p_115015_, p_115016_, p_115017_, p_115018_);
        if (!((double)vampire.animationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = vampire.animationPosition - vampire.animationSpeed * (1.0F - p_115018_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            p_115015_.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}