package com.tzaranthony.spellbook.core.util.tags;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class SBEntityTags {
    public static final TagKey<EntityType<?>> SILVER_VULNERABLE = registerEntityTag("silver_vulnerable");
    public static final TagKey<EntityType<?>> PACIFY_IMMUNE = registerEntityTag("pacify_immune");

    private static @NotNull TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(SpellBook.MOD_ID, name));
    }
}