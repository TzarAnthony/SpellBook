package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Inspiration extends Spell{
    public Inspiration(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos pos) {
        List<TamableAnimal> entities = level.getEntitiesOfClass(TamableAnimal.class,
                new AABB((double) (pos.getX() - 4), (double) (pos.getY() - 4), (double) (pos.getZ() - 4), (double) (pos.getX() + 4), (double) (pos.getY() + 4), (double) (pos.getZ() + 4)));

        if (!entities.isEmpty()) {
            for (TamableAnimal mob : entities) {
                if (mob.isOwnedBy(player) && mob.isAlive() && !mob.isRemoved()) {
                    mob.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 0));
                    mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 500, 0));
                    mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 0));
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 0));
                }
            }
            return true;
        } else {
            return false;
        }
    }
}