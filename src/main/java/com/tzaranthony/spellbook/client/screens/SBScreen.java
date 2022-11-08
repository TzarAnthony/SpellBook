package com.tzaranthony.spellbook.client.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.client.screens.renders.ITooltipWidget;
import com.tzaranthony.spellbook.client.screens.renders.InfoArea;
import com.tzaranthony.spellbook.client.screens.renders.ResettableLazy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class SBScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ResettableLazy<List<InfoArea>> infoAreas;
    protected final ResourceLocation background;

    public SBScreen(T menu, Inventory inv, Component title, ResourceLocation background)
    {
        super(menu, inv, title);
        this.background = background;
        this.infoAreas = new ResettableLazy<>(this::makeInfoAreas);
        this.inventoryLabelY = this.imageHeight-91;
    }

    @Override
    protected void init()
    {
        super.init();
        this.infoAreas.reset();
    }

    @Nonnull
    protected List<InfoArea> makeInfoAreas() {
        return ImmutableList.of();
    }

    @Override
    protected void renderLabels(PoseStack transform, int mouseX, int mouseY)
    {
        // Only difference to super version is the text color
        final int color = 0x190b06;
        this.font.draw(transform, title, titleLabelX, titleLabelY, color);
        this.font.draw(transform, playerInventoryTitle, inventoryLabelX, inventoryLabelY, color);
    }

    @Override
    public void render(@Nonnull PoseStack transform, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(transform);
        super.render(transform, mouseX, mouseY, partialTicks);
        List<Component> tooltip = new ArrayList<>();
        for (InfoArea area : infoAreas.get())
            area.fillTooltip(mouseX, mouseY, tooltip);
        for (GuiEventListener w : children())
            if (w.isMouseOver(mouseX, mouseY) && w instanceof ITooltipWidget ttw)
                ttw.gatherTooltip(mouseX, mouseY, tooltip);
        gatherAdditionalTooltips(
                mouseX, mouseY, tooltip::add, t -> tooltip.add(applyFormat(t, ChatFormatting.GRAY))
        );
        if (!tooltip.isEmpty())
            renderTooltip(transform, tooltip, Optional.empty(), mouseX, mouseY);
        else
            this.renderTooltip(transform, mouseX, mouseY);
    }

    protected boolean isMouseIn(int mouseX, int mouseY, int x, int y, int w, int h)
    {
        return mouseX >= leftPos+x&&mouseY >= topPos+y
                &&mouseX < leftPos+x+w&&mouseY < topPos+y+h;
    }

    protected void clearIntArray(ContainerData ints)
    {
        // Clear GUI ints, the sync code assumes that 0 is the initial state
        for(int i = 0; i < ints.getCount(); ++i)
            ints.set(i, 0);
    }

    public void fullInit()
    {
        super.init(minecraft, width, height);
    }

    @Override
    protected final void renderBg(@Nonnull PoseStack transform, float partialTicks, int x, int y)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, background);
        drawBackgroundTexture(transform);
        drawContainerBackgroundPre(transform, partialTicks, x, y);
        for(InfoArea area : infoAreas.get())
            area.draw(transform);
    }

    protected void drawBackgroundTexture(PoseStack transform)
    {
        blit(transform, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    protected void drawContainerBackgroundPre(@Nonnull PoseStack matrixStack, float partialTicks, int x, int y) {}

    protected void gatherAdditionalTooltips(
            int mouseX, int mouseY, Consumer<Component> addLine, Consumer<Component> addGray
    ) {}

    public static ResourceLocation makeTextureLocation(String name)
    {
        return new ResourceLocation(SpellBook.MOD_ID, "textures/gui/"+name+".png");
    }

    public static MutableComponent applyFormat(Component component, ChatFormatting... color)
    {
        Style style = component.getStyle();
        for(ChatFormatting format : color)
            style = style.applyFormat(format);
        return component.copy().setStyle(style);
    }
}