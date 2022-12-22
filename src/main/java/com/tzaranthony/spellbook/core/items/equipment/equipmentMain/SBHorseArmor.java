package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class SBHorseArmor extends HorseArmorItem {
    public SBHorseArmor(int protection, String texture, Properties properties) {
        super(protection, new ResourceLocation(SpellBook.MOD_ID + ":textures/entity/horse/armor/horse_armor_" + texture + ".png"), properties);
    }
}