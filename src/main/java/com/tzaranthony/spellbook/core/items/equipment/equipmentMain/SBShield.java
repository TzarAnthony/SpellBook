package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import net.minecraft.world.item.ShieldItem;

public class SBShield extends ShieldItem {
    private final int magicReduction;

    public SBShield(Properties properties, int durability, String name, int magicDef) {
        super(properties.durability(durability));
        this.setRegistryName(name);
        this.magicReduction = magicDef;
    }

    // add magic dmg reduction
}