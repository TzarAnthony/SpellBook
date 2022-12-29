package com.tzaranthony.spellbook.client.entityRender.renders.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.entities.other.ShatteringCrystal;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShatteringCrystalRender extends EntityRenderer<ShatteringCrystal> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("spellbook:textures/entity/crystal/shattering_crystal.png");
    private final CrystalModel<ShatteringCrystal> model;

    public ShatteringCrystalRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CrystalModel<>(CrystalModel.createBodyLayer().bakeRoot());
    }

    public void render(ShatteringCrystal crystal, float p_114529_, float mod, PoseStack pose, MultiBufferSource buff, int light) {
        float f = crystal.getAnimationProgress(mod);
        if (f != 0.0F) {
            float f1 = 2.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }

            pose.pushPose();
            pose.mulPose(Vector3f.YP.rotationDegrees(90.0F - crystal.getYRot()));
            pose.scale(-f1, -f1, f1);
            pose.translate(0.0D, -0.626D, 0.0D);
            pose.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(crystal, f, 0.0F, 0.0F, crystal.getYRot(), crystal.getXRot());
            VertexConsumer vertexconsumer = buff.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(pose, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pose.popPose();
            super.render(crystal, p_114529_, mod, pose, buff, light);
        }
    }

    public ResourceLocation getTextureLocation(ShatteringCrystal p_114526_) {
        return TEXTURE_LOCATION;
    }

    public class CrystalModel<T extends Entity> extends HierarchicalModel<T> {
        private final ModelPart root;
        private final ModelPart bb_main;
        private final ModelPart xza;
        private final ModelPart xzb;

        public CrystalModel(ModelPart part) {
            this.root = part;
            this.bb_main = root.getChild("bb_main");
            this.xza = bb_main.getChild("xza");
            this.xzb = bb_main.getChild("xzb");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
            bb_main.addOrReplaceChild("xza", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
            bb_main.addOrReplaceChild("xzb", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
            return LayerDefinition.create(meshdefinition, 16, 16);
        }

        public void setupAnim(T entity, float ticks, float p_102634_, float p_102635_, float rotY, float rotX) {
            float f1 = (ticks + Mth.sin(ticks * 2.7F)) * 0.6F * 12.0F;
            this.xza.y = -f1;
            this.xzb.y = this.xza.y;
        }

        public ModelPart root() {
            return this.root;
        }
    }
}