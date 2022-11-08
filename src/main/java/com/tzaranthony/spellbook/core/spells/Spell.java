package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Spell {
    int id;
    String name;
    SpellTier tier;

    public Spell(int id, String name, SpellTier tier) {
        this.id = id;
        this.name = name;
        this.tier = tier;
    }

    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos pos) {
        return false;
    }

    public boolean perform_spell(Entity user, Entity target) {
        return false;
    }

    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        return false;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}