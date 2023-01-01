package com.tzaranthony.spellbook.client.entityRender.renders.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.entities.arrows.ThrownTool;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ThrownToolRender extends EntityRenderer<ThrownTool> {
    private final ItemRenderer itemRenderer;
    private final Random random = new Random();

    public ThrownToolRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ThrownTool tool, float yaw, float pTick, PoseStack pose, MultiBufferSource buff, int light) {
        pose.pushPose();
        ItemStack itemstack = tool.getTool();
        int i = itemstack.isEmpty() ? 187 : Item.getId(itemstack.getItem()) + itemstack.getDamageValue();
        this.random.setSeed((long)i);
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, tool.level, null, tool.getId());

        pose.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pTick, tool.yRotO, tool.getYRot()) - 90.0F));
        pose.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pTick, tool.xRotO, tool.getXRot()) - 90.0F));

        this.itemRenderer.render(itemstack, ItemTransforms.TransformType.FIXED, false, pose, buff, light, OverlayTexture.NO_OVERLAY, bakedmodel);
        pose.popPose();
        super.render(tool, yaw, pTick, pose, buff, light);
    }

    protected int getRenderAmount(ItemStack p_115043_) {
        int i = 1;
        if (p_115043_.getCount() > 48) {
            i = 5;
        } else if (p_115043_.getCount() > 32) {
            i = 4;
        } else if (p_115043_.getCount() > 16) {
            i = 3;
        } else if (p_115043_.getCount() > 1) {
            i = 2;
        }

        return i;
    }

    public ResourceLocation getTextureLocation(ThrownTool tool) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}