package com.tzaranthony.spellbook.core.items.otherEquipment;

import com.tzaranthony.spellbook.core.items.SBItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ArchitectBag extends Item {
    public ArchitectBag() {
        super(SBItemProperties.Standard(Rarity.RARE, 1));
        // can set state too like in a DebugStickItem
    }
}