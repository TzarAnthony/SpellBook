package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import net.minecraft.world.item.HoeItem;

public class SBHoe extends HoeItem {
    public SBHoe(SBToolMaterial tier, Properties properties) {
        super(tier, (int) (0 - tier.getAttackDamageBonus() * 0.5), 0.0F, properties);
    }
}