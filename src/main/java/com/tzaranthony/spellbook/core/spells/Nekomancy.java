package com.tzaranthony.spellbook.core.spells;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cat;

public class Nekomancy extends EntityConversionSpell{
    public Nekomancy(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void ConvertEntity(Mob entity) {
        if (AllowedToConvert(entity)) {
            Cat cat = entity.convertTo(EntityType.CAT, false);
            cat.setCatType(-1);
        }
    }
}