package com.tzaranthony.spellbook.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemicalFurnaceScreen extends AbstractContainerScreen<AlchemicalFurnaceMenu> {
    private static final ResourceLocation MENU = new ResourceLocation("spellbook:textures/gui/alchemical_furnace.png");

    public AlchemicalFurnaceScreen(AlchemicalFurnaceMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        createFluidRenderers();
    }

    public void createFluidRenderers() {
//        fluidRender1 = new FluidRender(menu.getBE().getFluidHandler().getTankCapacity(0), true, 16, 64);
    }

    public void render(PoseStack poseStack, int x, int y, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, x, y, partialTick);
        this.renderTooltip(poseStack, x, y);
    }

    protected void renderBg(PoseStack poseStack, float partialTick, int x, int y) {
        int burnX = 63;
        int burnY = 37;
        int burnXo = 176;
        int burnYo = 0;
        int bernH = 12;

        int progX = 88;
        int progY = 41;
        int progXo = 176;
        int progYo = 14;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MENU);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isLit()) {
            int k = this.menu.getLitProgress();
            this.blit(poseStack, i + burnX, j + burnY + bernH - k, burnXo, bernH - k, burnYo + bernH + 1, k + 1);
        }

        this.blit(poseStack, i + progX, j + progY, progXo, progYo, this.menu.getBurnProgress(), 4);
    }
}