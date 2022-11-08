package com.tzaranthony.spellbook.client.blockEntityRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.blockEntities.PedestalBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractPedestalRender<T extends PedestalBE> implements BlockEntityRenderer<T> {
    protected double xOff = 0.5D;
    protected double yOff = 1.2D;
    protected double zOff = 0.5D;

    public AbstractPedestalRender(BlockEntityRendererProvider.Context context) {
    }

    public PoseStack initalizePose(PoseStack poseStack, float rotation) {
        poseStack.pushPose();
        poseStack.translate(this.xOff, this.yOff, this.zOff);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
        return poseStack;
    }
}