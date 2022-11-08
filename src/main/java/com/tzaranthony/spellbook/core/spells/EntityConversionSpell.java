package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.entities.hostile.alchemical.NecroticSpider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityConversionSpell extends Spell{
    public EntityConversionSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos pos) {
        List<Mob> entities = level.getEntitiesOfClass(Mob.class,
                new AABB((double) (pos.getX() - 4), (double) (pos.getY() - 1), (double) (pos.getZ() - 4), (double) (pos.getX() + 4), (double) (pos.getY() + 3), (double) (pos.getZ() + 4)));

        if (!entities.isEmpty()) {
            for (Mob mob : entities) {
                if (mob.isAlive() && !mob.isRemoved()) {
                    ConvertEntity(mob);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void ConvertEntity(Mob entity) {
    }

    public static boolean AllowedToConvert(Mob entity) {
        if (entity instanceof Zombie) {
            return true;
        } else if (entity instanceof AbstractSkeleton) {
            return true;
        } else if (entity instanceof Spider & !(entity instanceof NecroticSpider)) {
            return true;
        } else if (entity instanceof FlyingMob) {
            return true;
        } else if (entity instanceof Slime) {
            return true;
        } else if (entity instanceof Silverfish) {
            return true;
        } else if (entity instanceof AmbientCreature) {
            return true;
        } else if (entity instanceof WaterAnimal) {
            return true;
        } else if (entity instanceof Animal & !(entity instanceof TamableAnimal) & !(entity instanceof AbstractHorse)) {
            return true;
        } else {
            return false;
        }
    }
}