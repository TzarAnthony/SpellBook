package com.tzaranthony.spellbook.core.spells;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AnimalSummoning extends SummoningSpell {
    public AnimalSummoning(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public EntityType getEntity(Level level) {
        int selector = level.getRandom().nextInt(5);
        if (selector == 1) {
            return EntityType.PIG;
        } else if (selector == 2) {
            return EntityType.COW;
        } else if (selector == 3) {
            return EntityType.SHEEP;
        } else if (selector == 4) {
            return EntityType.RABBIT;
        } else {
            return EntityType.CHICKEN;
        }
    }
}