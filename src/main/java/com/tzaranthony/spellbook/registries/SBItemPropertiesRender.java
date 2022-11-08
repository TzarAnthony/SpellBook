package com.tzaranthony.spellbook.registries;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SBItemPropertiesRender extends ItemProperties {
    public static void renderItemProperties() {
        registerGeneric(new ResourceLocation("magicelement"), (p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return getIntTag(p_174625_, "ToolElement");
        });

        registerGeneric(new ResourceLocation("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration() - p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        registerGeneric(new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });

        registerGeneric(new ResourceLocation("bookmarkedbook"), (p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return getBooleanTag(p_174625_, "bookmarked");
        });

        registerGeneric(new ResourceLocation("activatedbouquet"), (p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return getBooleanTag(p_174625_, "UltraDeathActive");
        });
    }

    public static int getBooleanTag(ItemStack stack, String tagName) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.getBoolean(tagName)) {
            return 1;
        }
        return 0;
    }

    public static int getIntTag(ItemStack stack, String tagName) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (!compoundtag.contains(tagName)) {
            return 0;
        }
        return compoundtag.getInt(tagName);
    }
}