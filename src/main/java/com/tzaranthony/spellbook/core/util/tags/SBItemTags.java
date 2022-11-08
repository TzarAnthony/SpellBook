package com.tzaranthony.spellbook.core.util.tags;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SBItemTags {
    //TODO: phoenix food
    public static final TagKey<Item> CATALYSTS = registerItemTag("catalysts");

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(SpellBook.MOD_ID, name));
    }
}