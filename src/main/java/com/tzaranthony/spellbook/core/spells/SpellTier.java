package com.tzaranthony.spellbook.core.spells;

import net.minecraft.ChatFormatting;

public enum SpellTier implements net.minecraftforge.common.IExtensibleEnum {
    NOVICE(ChatFormatting.WHITE),
    PRACTITIONER(ChatFormatting.YELLOW),
    MASTERY(ChatFormatting.AQUA);

    public final ChatFormatting color;

    private SpellTier(ChatFormatting format) {
        this.color = format;
    }

    public static SpellTier create(String name, ChatFormatting format) {
        throw new IllegalStateException("Enum not extended");
    }
}