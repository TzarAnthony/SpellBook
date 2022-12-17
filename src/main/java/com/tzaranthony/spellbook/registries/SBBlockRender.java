package com.tzaranthony.spellbook.registries;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class SBBlockRender {
    public static void renderBlocks() {
        // cutout mipped
        RenderType mipped = RenderType.cutoutMipped();
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.NYMPH_BUSH.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.NETHER_BUSH.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.TERRAN_BUSH.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.NUBIBUS_BUSH.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.CRYSTALAN_CLUSTER.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.CRYSTALAN_BUD_LARGE.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.CRYSTALAN_BUD_MEDIUM.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.CRYSTALAN_BUD_SMALL.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.BLOOD_ROSE.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.POTTED_BLOOD_ROSE.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.PEDESTAL_STILL.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.PEDESTAL_SPIN.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.PEDESTAL_GLOW_STILL.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.PEDESTAL_GLOW_SPIN.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.END_TORCH.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.END_WALL_TORCH.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.END_LANTERN.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.END_FIRE.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.END_CAMPFIRE.get(), mipped);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.CRYSTALLIZED_ENDER_CLUSTER.get(), mipped);

        // glass like
        RenderType clear = RenderType.translucent();
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.INFUSED_GLASS.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_BLACK.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_BLUE.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_BROWN.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_CYAN.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_GRAY.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_GREEN.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_LIGHT_BLUE.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_LIGHT_GRAY.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_LIME.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_MAGENTA.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_ORANGE.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_PINK.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_PURPLE.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_RED.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_WHITE.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.LIGHT_ESSENCE_BLOCK_YELLOW.get(), clear);
        ItemBlockRenderTypes.setRenderLayer(SBBlocks.SNARE.get(), clear);
    }
}