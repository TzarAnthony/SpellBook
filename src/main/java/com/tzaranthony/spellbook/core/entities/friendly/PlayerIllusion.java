package com.tzaranthony.spellbook.core.entities.friendly;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

//TODO create -- stage 2????
public class PlayerIllusion extends SBSummonedEntity {
    public PlayerIllusion(EntityType<? extends TamableAnimal> entity, Level level) {
        super(entity, level);
    }

    // gets bow or sword in off hand
    // gets spell in main hand
    // mp deducted from owner
}