package com.tzaranthony.spellbook.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.core.containers.menus.BuilderBagMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BuilderBagScreen extends SBBagScreen<BuilderBagMenu> {
    private static final ResourceLocation MENU = new ResourceLocation("spellbook:textures/gui/builder_bag.png");

    public BuilderBagScreen(BuilderBagMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    public void render(PoseStack poseStack, int x, int y, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, x, y, partialTick);
        this.renderTooltip(poseStack, x, y);
    }

    protected void renderLabels(PoseStack poseStack, int x, int y) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    @Override
    protected void renderBg(PoseStack pose, float partialTick, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MENU);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pose, i, j, 0, 0, this.imageWidth, this.imageHeight);

//        this.blit(pose, i + 10, j + 35, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 30, 16);
        this.blit(pose, i + 73, j + 35, 0, this.imageHeight + (this.menu.getSlot(1).hasItem() ? 0 : 16), 30, 16);
        this.blit(pose, i + 136, j + 35, 0, this.imageHeight + (this.menu.getSlot(2).hasItem() ? 0 : 16), 30, 16);
    }
}