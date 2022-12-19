package com.tzaranthony.spellbook.client.blockEntityRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.blockEntities.RiftBE;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RiftRenderer implements BlockEntityRenderer<RiftBE> {
    public static final ResourceLocation LOCATION = new ResourceLocation("spellbook:textures/entity/rift.png");
    private final ModelPart frame;
    private final ModelPart portal;
    private final BlockEntityRenderDispatcher renderer;

    public RiftRenderer(BlockEntityRendererProvider.Context context) {
        this.renderer = context.getBlockEntityRenderDispatcher();
        this.frame = createFrameLayer().bakeRoot();
        this.portal = createPortalLayer().bakeRoot();
    }

    public static LayerDefinition createFrameLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 32.0F, 0.0F, new CubeDeformation(0.02F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 32);
    }

    public static LayerDefinition createPortalLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("portal", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -9.0F, 0.0F, 6.0F, 18.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 8, 20);
    }

    public void render(RiftBE riftBE, float mod, PoseStack poseStack, MultiBufferSource buf, int lightVal, int overlayVal) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.0D, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-this.renderer.camera.getYRot()));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        this.frame.render(poseStack, buf.getBuffer(RenderType.entityCutout(LOCATION)), 15728880, overlayVal);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.0D, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-this.renderer.camera.getYRot()));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        this.portal.render(poseStack, buf.getBuffer(RenderType.endGateway()), 15728880, overlayVal);
        poseStack.popPose();
    }
}