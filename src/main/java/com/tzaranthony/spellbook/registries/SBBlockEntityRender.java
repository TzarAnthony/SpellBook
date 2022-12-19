package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.client.blockEntityRender.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SBBlockEntityRender {
    public static void renderBlockEntities() {
        BlockEntityRenderers.register(SBBlockEntities.END_CAMPFIRE.get(), CampfireRenderer::new);
        BlockEntityRenderers.register(SBBlockEntities.PEDESTAL_SPIN.get(), PedestalRenderSpin::new);
        BlockEntityRenderers.register(SBBlockEntities.PEDESTAL_STILL.get(), PedestalRenderStill::new);
        BlockEntityRenderers.register(SBBlockEntities.PEDESTAL_SPIN_GLOW.get(), PedestalRenderSpinGlow::new);
        BlockEntityRenderers.register(SBBlockEntities.PEDESTAL_STILL_GLOW.get(), PedestalRenderStillGlow::new);
        BlockEntityRenderers.register(SBBlockEntities.DIMENSIONAL_BE.get(), DimensionalRender::new);
        BlockEntityRenderers.register(SBBlockEntities.ALTER.get(), AlterRender::new);
        BlockEntityRenderers.register(SBBlockEntities.RIFT.get(), RiftRenderer::new);
    }
}