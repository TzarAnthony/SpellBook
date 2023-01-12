package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Revelation extends Spell{
    public Revelation(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, LivingEntity entity, InteractionHand hand, BlockPos pos) {
        List<Monster> entities = level.getEntitiesOfClass(Monster.class,
                new AABB((double) (pos.getX() - 60), (double) (pos.getY() -60), (double) (pos.getZ() - 60), (double) (pos.getX() + 60), (double) (pos.getY() + 60), (double) (pos.getZ() + 60)));

        if (!entities.isEmpty()) {
            for (Monster mob : entities) {
                if (mob.isAlive() && !mob.isRemoved()) {
                    mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 500, 0));
                }
            }
            playCustomSound(entity);
            return true;
        } else {
            return false;
        }
    }
}