package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import net.minecraft.world.item.HoeItem;

public class SBHoe extends HoeItem {
    public SBHoe(SBToolMaterial tier, Properties properties) {
        super(tier, 10, -3.5F, properties);
    }
}