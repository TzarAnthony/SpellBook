package com.tzaranthony.spellbook.core.items.creative;

import com.tzaranthony.spellbook.core.items.spellBooks.SpellBookAdv;
import net.minecraft.world.item.Rarity;

public class SpellBookCreative extends SpellBookAdv {
    public SpellBookCreative() {
        super(Rarity.EPIC);
        this.setHasBookmark(true);
    }
}