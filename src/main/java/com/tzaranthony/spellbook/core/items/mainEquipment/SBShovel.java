package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import net.minecraft.world.item.ShovelItem;

public class SBShovel extends ShovelItem {
    public SBShovel(SBToolMaterial tier, Properties properties) {
        super(tier, 1.5F, -3.0F, properties);
    }
}