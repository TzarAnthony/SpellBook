package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;

public class SBDyeableArmorItem extends ArmorItem implements DyeableLeatherItem {
    public SBDyeableArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties properties) {
        super(materialIn, slot, properties);
    }
}