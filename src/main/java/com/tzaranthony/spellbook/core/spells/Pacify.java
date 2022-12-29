package com.tzaranthony.spellbook.core.spells;

import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Pacify extends Spell{
    public Pacify(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public boolean perform_spell(Level level, LivingEntity entity, InteractionHand hand, BlockPos pos) {
        List<Mob> entities = level.getEntitiesOfClass(Mob.class,
                new AABB((double) (pos.getX() - 20), (double) (pos.getY() -20), (double) (pos.getZ() - 20), (double) (pos.getX() + 20), (double) (pos.getY() + 20), (double) (pos.getZ() + 20)));

        if (!entities.isEmpty()) {
            for (Mob mob : entities) {
                if (mob instanceof Villager villager && !villager.hasEffect(SBEffects.VILLAGER_PACIFICATION.get())) {
                    villager.addEffect(new MobEffectInstance(SBEffects.VILLAGER_PACIFICATION.get(), 1000, 0));
                    if (entity instanceof ServerPlayer player && !level.isClientSide()) {
                        ((ServerLevel) level).onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, player, villager);
                    }
                }
                if (mob.isAlive() && !mob.isRemoved() && mob instanceof Monster && !(mob.getType().is(SBEntityTags.PACIFY_IMMUNE))) {
                    mob.addEffect(new MobEffectInstance(SBEffects.PACIFICATION.get(), 500, 0));
                }
            }
            return true;
        } else {
            return false;
        }
    }

}