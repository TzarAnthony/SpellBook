package com.tzaranthony.spellbook.client.blockEntityRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.core.blockEntities.PedestalSpinGlowBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PedestalRenderSpinGlow extends AbstractPedestalRender<PedestalSpinGlowBE> {
    public PedestalRenderSpinGlow(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PedestalSpinGlowBE pedestal, float mod, PoseStack poseStack, MultiBufferSource buf, int lightVal, int overlayVal) {
        ItemStack stack = pedestal.getItem();

        if (stack != ItemStack.EMPTY) {
            poseStack = initalizePose(poseStack, pedestal.getActiveRotation(mod));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, 15728880, overlayVal, poseStack, buf, (int) pedestal.getBlockPos().asLong());
            poseStack.popPose();
        }
    }
}