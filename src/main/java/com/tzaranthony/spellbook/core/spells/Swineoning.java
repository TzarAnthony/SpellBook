package com.tzaranthony.spellbook.core.spells;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Creeper;

public class Swineoning extends EntityConversionSpell{
    public Swineoning(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void ConvertEntity(Mob entity) {
        if (entity instanceof Pig) {
            Creeper creeper = entity.convertTo(EntityType.CREEPER, false);
        } else if (AllowedToConvert(entity)) {
            Pig pig = entity.convertTo(EntityType.PIG, false);
        }
    }
}