package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import net.minecraft.world.item.PickaxeItem;

public class SBPickaxe extends PickaxeItem {
    public SBPickaxe(SBToolMaterial tier, Properties properties) {
        super(tier, 1, -2.8F, properties);
    }
}