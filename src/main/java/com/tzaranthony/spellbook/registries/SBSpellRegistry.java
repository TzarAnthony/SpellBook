package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.core.spells.*;
import net.minecraft.world.effect.MobEffects;

public class SBSpellRegistry {
    // summoning
    public static final Spell GROWTH = new GrowthSpell(1, "growth", SpellTier.NOVICE); //TODO: might need to be improved?
    public static final Spell ANIMAL_SUMMONING = new AnimalSummoning(2, "animal_summoning", SpellTier.NOVICE);
    public static final Spell NECROMANCY_1 = new TamingSummoningSpell(3, "necromancy_1", SpellTier.NOVICE, SBEntities.SUMMONED_ZOMBIE);
    public static final Spell NECROMANCY_2 = new TamingSummoningSpell(4, "necromancy_2", SpellTier.PRACTITIONER, SBEntities.SUMMONED_SKELLY);
    public static final Spell NECROMANCY_3 = new TamingSummoningSpell(5, "necromancy_3", SpellTier.PRACTITIONER, SBEntities.SUMMONED_WITHER_SKELLY);
    public static final Spell SWINEONING = new Swineoning(7, "swineoning", SpellTier.MASTERY);
    public static final Spell NEKOMANCY = new Nekomancy(8, "nekomancy", SpellTier.MASTERY);

    // sorcery
    public static final Spell FIREWALL = new ProjectileSpell(9, "firewall", SpellTier.NOVICE); //TODO: creates a wall of fire a few blocks in front of the user
    public static final Spell FROST_WAVE = new FrostWave(10, "frost_wave", SpellTier.NOVICE);
    public static final Spell STONE_REINFORCEMENT = new SelfImprovementSpell(11, "stone_reinforcement", SpellTier.NOVICE, null, SBEffects.STONE_ARMOR, 600, 0);
    public static final Spell OUTWARD_WINDS = new ProjectileSpell(12, "outward_winds", SpellTier.NOVICE); //TODO: pushes all entities around the user away
    public static final Spell LONG_JUMP = new SelfImprovementSpell(13, "long_jump", SpellTier.NOVICE, MobEffects.JUMP, null, 40, 20); //TODO: might need improvement
    public static final Spell EXPLOSION = new Explosion(15, "explosion", SpellTier.PRACTITIONER); //TODO: EXPLOSION! Sparky sparky boom man? Does not destroy items, can silk touch, can fortune
    public static final Spell CHAIN_LIGHTNING = new Spell(16, "chain_lightning", SpellTier.PRACTITIONER); //TODO: arching electricity spikes between entities
    public static final Spell PSYCHIC_BARRIER = new Spell(17, "psychic_barrier", SpellTier.PRACTITIONER); //TODO: creates a temporary shield around the user
    public static final Spell ILLUSIONS = new Spell(18, "illusions", SpellTier.PRACTITIONER); //TODO: creates illusions of the user
    public static final Spell REVELATION = new Revelation(19, "revelation", SpellTier.PRACTITIONER);
    public static final Spell INSATIABLE_LETHARGY = new ProjectileSpell(20, "insatiable_lethargy", SpellTier.PRACTITIONER); //TODO: slows entities, the further they move from their original spot the more damage they take
    public static final Spell SCREAM = new Scream(21, "scream", SpellTier.PRACTITIONER);
    public static final Spell INSPIRATION = new Inspiration(22, "inspiration", SpellTier.PRACTITIONER);
    public static final Spell PLACATE = new Pacify(23, "placate", SpellTier.MASTERY);
    public static final Spell DARK_SNARE = new Snare(24, "dark_snare", SpellTier.MASTERY);
    public static final Spell LIFE_STEAL = new LifeSteal(25, "life_steal", SpellTier.MASTERY);
    public static final Spell SOULBIND = new Binding(26, "soulbind", SpellTier.MASTERY); //TODO: binds and entity with a magical lead. can be attached to a fence, wall or alter (for ritual purposes)
    public static final Spell ENDER_RIFT = new EnderRift(27, "ender_rift", SpellTier.MASTERY, SBBlocks.ENDER_RIFT); //TODO: creates linked portals (portal gun), maybe can make more than one pair??
    public static final Spell RIFT_OF_DARKNESS = new DarkRift(28, "rift_of_darkness", SpellTier.MASTERY, SBBlocks.BLACK_HOLE); //TODO: sucks entities into a specific spot
    public static final Spell TIME_SPELL = new Time(29, "time_spell", SpellTier.MASTERY, SBBlocks.TIMER); //TODO: can permanently de-age mobs
    public static final Spell OBJECT_ENCHANTMENT = new Spell(30, "object_enchantment", SpellTier.PRACTITIONER); //TODO: enchants crafting table for auto-crafting, enchants hoppers to make super fast hoppers, enchants enchanting table for auto-enchanting
    //TODO: shapeshifting?

    //TODO: change to be a block
    public static final Spell ACTION_SPELL = new Spell(31, "action_spell", SpellTier.PRACTITIONER); //TODO: performs a given spell (requires marked spell paper) as specified by a command spell
    public static final Spell DETECTOR_SPELL = new Spell(32, "detector_spell", SpellTier.PRACTITIONER); //TODO: detects when entities are in a region, or detects redstone state changes
    public static final Spell COMMAND_SPELL = new Spell(33, "command_spell", SpellTier.PRACTITIONER); //TODO: informs an action spell when to act

    //Mob only
    public static final Spell IGNITE = new Ignite(34, "ignite", SpellTier.MASTERY);
}