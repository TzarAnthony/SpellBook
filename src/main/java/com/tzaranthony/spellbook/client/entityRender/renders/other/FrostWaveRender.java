package com.tzaranthony.spellbook.client.entityRender.renders.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrostWaveRender extends EntityRenderer<MagicProjectile> {
    public static final ResourceLocation LOCATION = new ResourceLocation("spellbook:textures/entity/projectiles/frost_wave.png");

    public FrostWaveRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicProjectile mpjt) {
        return LOCATION;
    }

    public void render(MagicProjectile mpjt, float yaw, float pTick, PoseStack pose, MultiBufferSource buff, int light) {
        pose.pushPose();
        pose.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pTick, mpjt.yRotO, mpjt.getYRot()) - 45.0F));
        pose.scale(1.1F, 1.1F, 1.1F);
        pose.translate(0.0D, -0.1F, 0.0D);
        VertexConsumer vertexconsumer = buff.getBuffer(RenderType.entityCutout(LOCATION));
        PoseStack.Pose posestack$pose = pose.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        //TODO: i might actually have to make this into a model to get it to render on both sides :(
        this.vertex(matrix4f, matrix3f, vertexconsumer, -1, 0, -1, 0, 0, 1, 0, 1, light);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -1, 0, 1, 0, 1, 1, 0, 1, light);
        this.vertex(matrix4f, matrix3f, vertexconsumer, 1, 0, 1, 1, 1, 1, 0, 1, light);
        this.vertex(matrix4f, matrix3f, vertexconsumer, 1, 0, -1, 1, 0, 1, 0, 1, light);

        pose.popPose();
        super.render(mpjt, yaw, pTick, pose, buff, light);
    }

    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, int p_113829_, int p_113830_, int p_113831_, float p_113832_, float p_113833_, int p_113834_, int p_113835_, int p_113836_, int light) {
        consumer.vertex(matrix4f, (float)p_113829_, (float)p_113830_, (float)p_113831_).color(255, 255, 255, 255).uv(p_113832_, p_113833_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, (float)p_113834_, (float)p_113836_, (float)p_113835_).endVertex();
    }
}