package com.tzaranthony.spellbook.client.blockEntityRender;

import com.tzaranthony.spellbook.core.blockEntities.DimensionalBE;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DimensionalRender extends TheEndPortalRenderer<DimensionalBE> {
    public DimensionalRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected float getOffsetUp() {
        return 1.0F;
    }

    protected float getOffsetDown() {
        return 0.0F;
    }

    protected RenderType renderType() {
        return RenderType.endGateway();
    }

    public int getViewDistance() {
        return 256;
    }
}