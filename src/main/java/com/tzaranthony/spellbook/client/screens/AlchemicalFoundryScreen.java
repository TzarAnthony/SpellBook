package com.tzaranthony.spellbook.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFoundryMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemicalFoundryScreen extends AbstractContainerScreen<AlchemicalFoundryMenu> {
    private static final ResourceLocation MENU = new ResourceLocation("spellbook:textures/gui/alchemical_foundry.png");
    protected int mouseX;
    protected int mouseY;
//    protected FluidRender fluidRender1;
//    protected FluidRender fluidRender2;

    public AlchemicalFoundryScreen(AlchemicalFoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 194;
        this.imageHeight = 190;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        createFluidRenderers();
    }

    public void createFluidRenderers() {
//        fluidRender1 = new FluidRender(menu.getBE().getFluidHandler().getTankCapacity(0), true, 16, 64);
//        fluidRender2 = new FluidRender(menu.getBE().getFluidHandler().getTankCapacity(1), true, 16, 64);
    }

    public void render(PoseStack poseStack, int x, int y, float partialTick) {
        mouseX = x - leftPos;
        mouseY = y - topPos;

        renderBackground(poseStack);
        super.render(poseStack, x, y, partialTick);
        this.renderTooltip(poseStack, x, y);
    }

//    @Override
//    protected void renderTooltip(PoseStack poseStack, int x, int y) {
//        super.renderTooltip(poseStack, x, y);
//        if(isMouseAboveArea(x, y)) {
//            renderTooltip(poseStack, fluidRender1.getTooltip(menu.getBE().getFluidHandler().getFluidInTank(0), TooltipFlag.Default.NORMAL), Optional.empty(), mouseX - x, mouseY - y);
//        }
//    }

    protected void renderLabels(PoseStack poseStack, int x, int y) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    protected void renderBg(PoseStack poseStack, float partialTick, int x, int y) {
        int burnX = 90;
        int burnY = 62;
        int burnXo = 194;
        int burnYo = 0;
        int bernH = 12;

        int progX = 123;
        int progY = 60;
        int progXo = 194;
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

        this.blit(poseStack, i + progX, j + progY, progXo, progYo, this.menu.getBurnProgress() + 1, 16);
    }

    private boolean isMouseAboveArea(int x, int y) {
        return (mouseX >= x && mouseX <= x + this.imageHeight) && (mouseY >= y && mouseY <= y + this.imageWidth);
    }
}