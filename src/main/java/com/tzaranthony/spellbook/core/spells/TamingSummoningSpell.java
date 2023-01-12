package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.friendly.SBSummonedEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class TamingSummoningSpell extends SummoningSpell {
    public  Supplier<? extends EntityType<? extends SBSummonedEntity>> entity;

    public TamingSummoningSpell(int id, String name, SpellTier tier, Supplier<? extends EntityType<? extends SBSummonedEntity>> entity) {
        super(id, name, tier);
        this.entity = entity;
    }

    @Override
    public EntityType getEntity(Level level) {
        return this.entity.get();
    }

    @Override
    public void playCustomSound(Entity user) {
        user.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
    }
}