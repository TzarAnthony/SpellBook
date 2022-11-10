package com.tzaranthony.spellbook.client.blockEntityRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.blockEntities.AlterBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AlterRender implements BlockEntityRenderer<AlterBE> {
    protected double xMOff = 0.5D;
    protected double yMOff = 1.2D;
    protected double zMOff = 0.5D;

    public AlterRender(BlockEntityRendererProvider.Context context) {
    }

    public void render(AlterBE AltBe, float tick, PoseStack poseStack, MultiBufferSource buf, int lightVal, int overlayVal) {
        int id = (int) AltBe.getBlockPos().asLong();
        renderResultItem(AltBe.getItem(0), AltBe.getActiveRotation(tick),  poseStack, buf, overlayVal, id);

        float spin = AltBe.getActiveSpin(tick);
        int rendCount = AltBe.getFilledSlots();
        float ang1 = (float) 360 / rendCount;
        for(int j = 1; j < rendCount + 1; ++j) {
            double angle = (Math.PI * (j * ang1)) / 180.0D;
            ItemStack stack = AltBe.getItem(j);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5D + 1.5D * Math.cos(angle - (spin/16.0D)), 1.75, 0.5D + 1.5D * Math.sin(angle - (spin/16.0D)));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(spin/16.0F));
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, 15728880, overlayVal, poseStack, buf, id);
                poseStack.popPose();
            }
        }
    }

    public void renderResultItem(ItemStack stack, float tick, PoseStack poseStack, MultiBufferSource buf, int overlayVal, int id) {
        if (stack != ItemStack.EMPTY) {
            poseStack.pushPose();
            poseStack.translate(this.xMOff, this.yMOff, this.zMOff);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(tick));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, 15728880, overlayVal, poseStack, buf, id);
            poseStack.popPose();
        }
    }
}