package com.tzaranthony.spellbook.client.entityRender.renders.managers;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class CursedPaintingTextureManager extends TextureAtlasHolder {
    private static final ResourceLocation BACK_SPRITE_LOCATION = new ResourceLocation("back");

    public CursedPaintingTextureManager(TextureManager manager) {
        super(manager, new ResourceLocation("spellbook:textures/atlas/cursed_paintings.png"), "cursed_painting");
    }

    protected Stream<ResourceLocation> getResourcesToLoad() {
        return Stream.concat(Registry.MOTIVE.keySet().stream(), Stream.of(BACK_SPRITE_LOCATION));
    }

    public TextureAtlasSprite get(Motive p_118805_) {
        return this.getSprite(Registry.MOTIVE.getKey(p_118805_));
    }

    public TextureAtlasSprite getBackSprite() {
        return this.getSprite(BACK_SPRITE_LOCATION);
    }
}