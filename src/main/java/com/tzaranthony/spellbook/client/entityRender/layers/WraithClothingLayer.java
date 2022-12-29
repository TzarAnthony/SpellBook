package com.tzaranthony.spellbook.client.entityRender.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tzaranthony.spellbook.client.entityRender.models.WraithModel;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Wraith;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraithClothingLayer<T extends Wraith, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation CLOTHES_LOCATION = new ResourceLocation("spellbook:textures/entity/ghost/wraith_overlay.png");
    private final WraithModel<T> layerModel;

    public WraithClothingLayer(RenderLayerParent<T, M> p_i50919_1_, EntityModelSet p_174545_) {
        super(p_i50919_1_);
        this.layerModel = new WraithModel<>(p_174545_.bakeLayer(ModelLayers.STRAY_OUTER_LAYER));
    }

    public void render(PoseStack p_225628_1_, MultiBufferSource p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        coloredTranslucentModelCopyLayerRender(this.getParentModel(), this.layerModel, CLOTHES_LOCATION, p_225628_1_, p_225628_2_, p_225628_3_, p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, 1.0F, 1.0F, 1.0F);
    }

    protected static <T extends LivingEntity> void coloredTranslucentModelCopyLayerRender(EntityModel<T> entityModel, EntityModel<T> layerModel, ResourceLocation location, PoseStack matrixStack, MultiBufferSource buffer, int modifier, T entity, float Apos1, float Apos2, float Apos3, float rot1, float rot2, float rot3, float rend1, float rend2, float rend3) {
        if (!entity.isInvisible()) {
            entityModel.copyPropertiesTo(layerModel);
            layerModel.prepareMobModel(entity, Apos1, Apos2, rot3);
            layerModel.setupAnim(entity, Apos1, Apos2, Apos3, rot1, rot2);
            VertexConsumer vertex = buffer.getBuffer(RenderType.entityTranslucent(location));
            layerModel.renderToBuffer(matrixStack, vertex, modifier, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), rend1, rend2, rend3, 1.0F);
        }
    }
}