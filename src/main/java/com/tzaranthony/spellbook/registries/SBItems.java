package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.items.*;
import com.tzaranthony.spellbook.core.items.creative.SpellBookCreative;
import com.tzaranthony.spellbook.core.items.creative.SpellDebug;
import com.tzaranthony.spellbook.core.items.food.*;
import com.tzaranthony.spellbook.core.items.arrows.CrystalArrowItem;
import com.tzaranthony.spellbook.core.items.arrows.GhostlyArrowItem;
import com.tzaranthony.spellbook.core.items.arrows.SilverArrowItem;
import com.tzaranthony.spellbook.core.items.items.PhoenixAshes;
import com.tzaranthony.spellbook.core.items.mainEquipment.*;
import com.tzaranthony.spellbook.core.items.SBArmorMaterial;
import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import com.tzaranthony.spellbook.core.items.otherEquipment.*;
import com.tzaranthony.spellbook.core.items.spellBooks.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBItems {
    public static final DeferredRegister<Item> reg = DeferredRegister.create(ForgeRegistries.ITEMS, SpellBook.MOD_ID);

    // Food
    public static final RegistryObject<Item> NYMPHA_BERRIES = reg.register("nympha_berries", () -> new SBBerries(SBBlocks.NYMPH_BUSH.get(), SBItemProperties.Berries(MobEffects.WATER_BREATHING, MobEffects.DOLPHINS_GRACE)));
    public static final RegistryObject<Item> INFERNUM_BERRIES = reg.register("infernum_berries", () -> new SBBerries(SBBlocks.NETHER_BUSH.get(), SBItemProperties.UnburnableBerries(MobEffects.FIRE_RESISTANCE, MobEffects.DAMAGE_BOOST)));
    public static final RegistryObject<Item> TERRENUS_BERRIES = reg.register("terrenus_berries", () -> new SBBerries(SBBlocks.TERRAN_BUSH.get(), SBItemProperties.Berries(MobEffects.DAMAGE_RESISTANCE, MobEffects.DIG_SPEED)));
    public static final RegistryObject<Item> CAELUM_BERRIES = reg.register("caelum_berries", () -> new SBBerries(SBBlocks.NUBIBUS_BUSH.get(), SBItemProperties.Berries(MobEffects.SLOW_FALLING, 0, MobEffects.MOVEMENT_SPEED, 1)));
    public static final RegistryObject<Item> SMOOTHIE = reg.register("smoothie", () -> new ThickDrink(SBItemProperties.Smoothie(), 30));
    public static final RegistryObject<Item> ELiXIR_OF_LIFE = reg.register("elixir_of_life", () -> new LifeBottle());
    public static final RegistryObject<Item> VAMPIRE_BLOOD = reg.register("vampire_blood", () -> new Drink(SBItemProperties.VampireBlood(Rarity.COMMON, 10, 1, 0.25f, 1, 0.9f), false));
    public static final RegistryObject<Item> HIGHER_VAMPIRE_BLOOD = reg.register("higher_vampire_blood", () -> new Drink(SBItemProperties.VampireBlood(Rarity.RARE, 20, 3, 0.35f, 4, 0.75f), true));
    public static final RegistryObject<Item> BOTTLE_OF_MERCURY = reg.register("bottle_of_mercury", () -> new MercuryBottle());

    // Materials
    public static final RegistryObject<Item> CINNABAR_FRAGMENT = reg.register("cinnabar_fragment", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> RAW_SILVER = reg.register("raw_silver", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> RAW_NETHERITE = reg.register("raw_netherite", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> DRACONITE = reg.register("draconite", () -> new Item(SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PHOENIX_FEATHER = reg.register("phoenix_feather", () -> new Item(SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> PHOENIX_ASHES = reg.register("phoenix_ashes", () -> new PhoenixAshes(SBItemProperties.Unburnable(Rarity.EPIC, 1)));
    public static final RegistryObject<Item> CURSED_SILK = reg.register("cursed_silk", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> ENCHANTED_SILK = reg.register("enchanted_silk", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> DYE_BASE = reg.register("dye_base", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> ECTOPLASM = reg.register("ectoplasm_vial", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> ALCHEMICAL_RESIDUE =reg.register("alchemical_residue", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> FLAMING_PEARL = reg.register("flaming_pearl", () -> new Item(SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DIAMOND_DUST = reg.register("diamond_dust", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> EMERALD_DUST = reg.register("emerald_dust", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> AMETHYST_DUST = reg.register("amethyst_dust", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> QUARTZ_DUST = reg.register("quartz_dust", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> CRYSTALAN_SEED = reg.register("crystalan_seed", () -> new ItemNameBlockItem(SBBlocks.CRYSTALAN_BUD_SMALL.get(), SBItemProperties.Standard(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CRYSTALAN_SHARD = reg.register("crystalan_shard", () -> new Item(SBItemProperties.Standard(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CRYSTALLIZED_ENDER =reg.register("crystallized_ender", () -> new Item(SBItemProperties.Standard(Rarity.RARE)));

    // misc tools
    public static final RegistryObject<Item> SPELL_PAPER = reg.register("spell_paper", () -> new SpellPaper());
    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR =reg.register("netherite_horse_armor", () -> new SBHorseArmor(15, "netherite", SBItemProperties.Unburnable(1)));
    public static final RegistryObject<Item> OLD_KEY = reg.register("old_key", () -> new Item(SBItemProperties.Standard(Rarity.RARE, 1)));
    public static final RegistryObject<Item> BOOKMARK = reg.register("bookmark", () -> new Item(SBItemProperties.Standard(Rarity.RARE, 1)));
    public static final RegistryObject<Item> TOTEM_OF_AVENGING = reg.register("totem_of_avenging", () -> new Item(SBItemProperties.Standard(Rarity.RARE, 1)));
    public static final RegistryObject<Item> TAROT_CARD = reg.register("tarot_card", () -> new TarotCard());
    public static final RegistryObject<Item> DEATH_BOUQUET = reg.register("death_bouquet", () -> new DeathBouquet());
    public static final RegistryObject<Item> BUILDER_BAG = reg.register("builder_bag", () -> new BuilderBag());
    public static final RegistryObject<Item> ARCHITECT_BAG = reg.register("architect_bag", () -> new ArchitectBag());
    public static final RegistryObject<Item> XP_BOOK = reg.register("xp_book", () -> new XPBook());

    // register utility books
    public static final RegistryObject<Item> TOME_OF_COORDINATION = reg.register("tome_of_coordination", () -> new Item(SBItemProperties.Standard(Rarity.RARE, 1))); //TODO: Use on a Lectern to make a Dimensional Directory
    public static final RegistryObject<Item> DIMENSIONAL_CODEX = reg.register("dimensional_codex", () -> new Item(SBItemProperties.Standard(Rarity.UNCOMMON, 1))); //TODO: Used to set the storage limit of a bookshelf -- stores 64 types and 2048 max items?
    public static final RegistryObject<Item> RESEARCH_BOOK = reg.register("research_book", () -> new ResearchBook(SBItemProperties.Standard(Rarity.UNCOMMON, 1))); //TODO: Provides some research points

    // spell books
    public static final RegistryObject<Item> LIBER_EXPONENTIA = reg.register("liber_exponentia", () -> new SpellBookNovice(Rarity.UNCOMMON));
    public static final RegistryObject<Item> LIBER_EXPONENTIA_INTER = reg.register("liber_exponentia_int", () -> new SpellBookInter(Rarity.RARE));
    public static final RegistryObject<Item> LIBER_EXPONENTIA_ADV = reg.register("liber_exponentia_adv", () -> new SpellBookAdv(Rarity.RARE));
    public static final RegistryObject<Item> LIBER_EXPONENTIA_EXP = reg.register("liber_exponentia_exp", () -> new SpellBookExp(Rarity.EPIC));
    public static final RegistryObject<Item> LIBER_EXPONENTIA_CREATIVE = reg.register("liber_exponentia_creative", () -> new SpellBookCreative());

    // Tool & Armor mats
    public static final RegistryObject<Item> SILVER_INGOT = reg.register("silver_ingot", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_NUGGET = reg.register("silver_nugget", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_STEEL_INGOT = reg.register("silver_steel_ingot", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_STEEL_NUGGET = reg.register("silver_steel_nugget", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> IMBUED_CLOTH = reg.register("imbued_cloth", () -> new Item(SBItemProperties.Standard()));
    public static final RegistryObject<Item> CURSED_SILVER_STEEL_INGOT = reg.register("cursed_silver_ingot", () -> new Item(SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSED_SILVER_STEEL_NUGGET = reg.register("cursed_silver_nugget", () -> new Item(SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_SILVER_STEEL_INGOT = reg.register("enchanted_steel_ingot", () -> new Item(SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_SILVER_STEEL_NUGGET = reg.register("enchanted_steel_nugget", () -> new Item(SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PHOENIX_METAL_INGOT = reg.register("phoenix_metal_ingot", () -> new Item(SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> PHOENIX_METAL_NUGGET = reg.register("phoenix_metal_nugget", () -> new Item(SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_MATRIX = reg.register("crystalan_matrix", () -> new Item(SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> ESSENCE_OF_LIGHT = reg.register("essence_of_light", () -> new Item(SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> ENDER_INFUSED_CLOTH = reg.register("ender_infused_cloth", () -> new Item(SBItemProperties.Unburnable(Rarity.EPIC)));
    public static final RegistryObject<Item> DIMENSIONAL_ALLOY = reg.register("dimensional_alloy", () -> new Item(SBItemProperties.Unburnable(Rarity.EPIC)));

    // armor and tools
    // tier 0
    public static final RegistryObject<Item> SILVER_HELMET = reg.register("silver_helmet", () -> new ArmorItem(SBArmorMaterial.SILVER, EquipmentSlot.HEAD, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_CHESTPLATE = reg.register("silver_chestplate", () -> new ArmorItem(SBArmorMaterial.SILVER, EquipmentSlot.CHEST, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_LEGGINGS = reg.register("silver_leggings", () -> new ArmorItem(SBArmorMaterial.SILVER, EquipmentSlot.LEGS, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_BOOTS = reg.register("silver_boots", () -> new ArmorItem(SBArmorMaterial.SILVER, EquipmentSlot.FEET, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_AXE = reg.register("silver_axe", () -> new SBAxe(SBToolMaterial.SILVER, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_HOE = reg.register("silver_hoe", () -> new SBHoe(SBToolMaterial.SILVER, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_PICKAXE = reg.register("silver_pickaxe", () -> new SBPickaxe(SBToolMaterial.SILVER, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_SHOVEL = reg.register("silver_shovel", () -> new SBShovel(SBToolMaterial.SILVER, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_SWORD = reg.register("silver_sword", () -> new SBSword(SBToolMaterial.SILVER, SBItemProperties.Standard()));
    public static final RegistryObject<Item> SILVER_ARROW = reg.register("silver_arrow", () -> new SilverArrowItem(SBItemProperties.Standard()));

    // tier 1
    public static final RegistryObject<Item> WATCHMAN_HELMET = reg.register("watchman_helmet", () -> new ArmorItem(SBArmorMaterial.SILVER_STEEL, EquipmentSlot.HEAD, SBItemProperties.Standard()));
    public static final RegistryObject<Item> WATCHMAN_CHESTPLATE = reg.register("watchman_chestplate", () -> new ArmorItem(SBArmorMaterial.SILVER_STEEL, EquipmentSlot.CHEST, SBItemProperties.Standard()));
    public static final RegistryObject<Item> WATCHMAN_LEGGINGS = reg.register("watchman_leggings", () -> new ArmorItem(SBArmorMaterial.SILVER_STEEL, EquipmentSlot.LEGS, SBItemProperties.Standard()));
    public static final RegistryObject<Item> WATCHMAN_BOOTS = reg.register("watchman_boots", () -> new ArmorItem(SBArmorMaterial.SILVER_STEEL, EquipmentSlot.FEET, SBItemProperties.Standard()));

    public static final RegistryObject<Item> NEOPHYTE_HELMET = reg.register("neophyte_helmet", () -> new SBDyeableArmorItem(SBArmorMaterial.IMBUED, EquipmentSlot.HEAD, SBItemProperties.Standard()));
    public static final RegistryObject<Item> NEOPHYTE_CHESTPLATE = reg.register("neophyte_chestplate", () -> new SBDyeableArmorItem(SBArmorMaterial.IMBUED, EquipmentSlot.CHEST, SBItemProperties.Standard()));
    public static final RegistryObject<Item> NEOPHYTE_PANTS = reg.register("neophyte_pants", () -> new SBDyeableArmorItem(SBArmorMaterial.IMBUED, EquipmentSlot.LEGS, SBItemProperties.Standard()));
    public static final RegistryObject<Item> NEOPHYTE_BOOTS = reg.register("neophyte_boots", () -> new SBDyeableArmorItem(SBArmorMaterial.IMBUED, EquipmentSlot.FEET, SBItemProperties.Standard()));

    public static final RegistryObject<Item> NECROMANCER_HELMET = reg.register("necromancer_helmet", () -> new ArmorItem(SBArmorMaterial.CURSED, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> NECROMANCER_CHESTPLATE = reg.register("necromancer_chestplate", () -> new ArmorItem(SBArmorMaterial.CURSED, EquipmentSlot.CHEST, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> NECROMANCER_LEGGINGS = reg.register("necromancer_leggings", () -> new ArmorItem(SBArmorMaterial.CURSED, EquipmentSlot.LEGS, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> NECROMANCER_BOOTS = reg.register("necromancer_boots", () -> new ArmorItem(SBArmorMaterial.CURSED, EquipmentSlot.FEET, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSED_AXE = reg.register("cursed_axe", () -> new SBAxe(SBToolMaterial.CURSED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSED_HOE = reg.register("cursed_hoe", () -> new SBHoe(SBToolMaterial.CURSED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSED_PICKAXE = reg.register("cursed_pickaxe", () -> new SBPickaxe(SBToolMaterial.CURSED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSED_SHOVEL = reg.register("cursed_shovel", () -> new SBShovel(SBToolMaterial.CURSED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSED_SWORD = reg.register("cursed_sword", () -> new SBSword(SBToolMaterial.CURSED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> GHOSTLY_ARROW = reg.register("ghostly_arrow", () -> new GhostlyArrowItem(SBItemProperties.Standard()));

    // tier 2
    public static final RegistryObject<Item> WITCH_HELMET = reg.register("witch_helmet", () -> new ArmorItem(SBArmorMaterial.ENCHANTED, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WITCH_CHESTPLATE = reg.register("witch_chestplate", () -> new ArmorItem(SBArmorMaterial.ENCHANTED, EquipmentSlot.CHEST, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WITCH_LEGGINGS = reg.register("witch_leggings", () -> new ArmorItem(SBArmorMaterial.ENCHANTED, EquipmentSlot.LEGS, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WITCH_BOOTS = reg.register("witch_boots", () -> new ArmorItem(SBArmorMaterial.ENCHANTED, EquipmentSlot.FEET, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_AXE = reg.register("enchanted_axe", () -> new SBAxe(SBToolMaterial.ENCHANTED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_HOE = reg.register("enchanted_hoe", () -> new SBHoe(SBToolMaterial.ENCHANTED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_PICKAXE = reg.register("enchanted_pickaxe", () -> new SBPickaxe(SBToolMaterial.ENCHANTED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_SHOVEL = reg.register("enchanted_shovel", () -> new SBShovel(SBToolMaterial.ENCHANTED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENCHANTED_SWORD = reg.register("enchanted_sword", () -> new SBSword(SBToolMaterial.ENCHANTED, SBItemProperties.Unburnable(Rarity.UNCOMMON)));

    // tier 3
    public static final RegistryObject<Item> BATTLEMASTER_HELMET = reg.register("battlemaster_helmet", () -> new SBDyeableArmorItem(SBArmorMaterial.PHOENIX, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> BATTLEMASTER_CHESTPLATE = reg.register("battlemaster_chestplate", () -> new SBDyeableArmorItem(SBArmorMaterial.PHOENIX, EquipmentSlot.CHEST, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> BATTLEMASTER_LEGGINGS = reg.register("battlemaster_leggings", () -> new SBDyeableArmorItem(SBArmorMaterial.PHOENIX, EquipmentSlot.LEGS, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> BATTLEMASTER_BOOTS = reg.register("battlemaster_boots", () -> new SBDyeableArmorItem(SBArmorMaterial.PHOENIX, EquipmentSlot.FEET, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CHANNELED_AXE = reg.register("channeled_axe", () -> new ChanneledAxe(SBToolMaterial.PHOENIX, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CHANNELED_SWORD = reg.register("channeled_sword", () -> new ChanneledSword(SBToolMaterial.PHOENIX, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CHANNELED_BOW = reg.register("channeled_bow", () -> new ChanneledBow(SBItemProperties.Unburnable(Rarity.RARE, 1)));

    public static final RegistryObject<Item> LIGHT_HELMET = reg.register("light_helmet", () -> new ArmorItem(SBArmorMaterial.LIGHT, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> LIGHT_CHESTPLATE = reg.register("light_chestplate", () -> new ArmorItem(SBArmorMaterial.LIGHT, EquipmentSlot.CHEST, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> LIGHT_LEGGINGS = reg.register("light_leggings", () -> new ArmorItem(SBArmorMaterial.LIGHT, EquipmentSlot.LEGS, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> LIGHT_BOOTS = reg.register("light_boots", () -> new ArmorItem(SBArmorMaterial.LIGHT, EquipmentSlot.FEET, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> LIGHT_SWORD = reg.register("light_sword", () -> new SBSword(SBToolMaterial.LIGHT, SBItemProperties.Unburnable(Rarity.RARE)));

    public static final RegistryObject<Item> CRYSTALAN_HELMET = reg.register("crystalan_helmet", () -> new ArmorItem(SBArmorMaterial.CRYSTALAN, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_CHESTPLATE = reg.register("crystalan_chestplate", () -> new ArmorItem(SBArmorMaterial.CRYSTALAN, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_LEGGINGS = reg.register("crystalan_leggings", () -> new ArmorItem(SBArmorMaterial.CRYSTALAN, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_BOOTS = reg.register("crystalan_boots", () -> new ArmorItem(SBArmorMaterial.CRYSTALAN, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_HOE = reg.register("crystalan_hoe", () -> new SBHoe(SBToolMaterial.CRYSTALAN, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_PICKAXE = reg.register("crystalan_pickaxe", () -> new SBPickaxe(SBToolMaterial.CRYSTALAN, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_SHOVEL = reg.register("crystalan_shovel", () -> new SBShovel(SBToolMaterial.CRYSTALAN, SBItemProperties.Unburnable(Rarity.RARE)));
    public static final RegistryObject<Item> CRYSTALAN_ARROW = reg.register("crystalan_arrow", () -> new CrystalArrowItem(SBItemProperties.Unburnable(Rarity.RARE)));

    public static final RegistryObject<Item> ARCHMAGE_HELMET = reg.register("archmage_helmet", () -> new ArmorItem(SBArmorMaterial.ARCH, EquipmentSlot.HEAD, SBItemProperties.Unburnable(Rarity.EPIC)));
    public static final RegistryObject<Item> ARCHMAGE_CHESTPLATE = reg.register("archmage_chestplate", () -> new ArmorItem(SBArmorMaterial.ARCH, EquipmentSlot.CHEST, SBItemProperties.Unburnable(Rarity.EPIC)));
    public static final RegistryObject<Item> ARCHMAGE_LEGGINGS = reg.register("archmage_leggings", () -> new ArmorItem(SBArmorMaterial.ARCH, EquipmentSlot.LEGS, SBItemProperties.Unburnable(Rarity.EPIC)));
    public static final RegistryObject<Item> ARCHMAGE_BOOTS = reg.register("archmage_boots", () -> new ArmorItem(SBArmorMaterial.ARCH, EquipmentSlot.FEET, SBItemProperties.Unburnable(Rarity.EPIC)));

    // BlockItems
    public static final RegistryObject<Item> END_TORCH = reg.register("end_torch", () -> new StandingAndWallBlockItem(SBBlocks.END_TORCH.get(), SBBlocks.END_WALL_TORCH.get(), SBItemProperties.Standard()));

    // spell debug items
    public static final RegistryObject<Item> GROWTH_DEBUG = reg.register("growth_debug", () -> new SpellDebug(SBSpellRegistry.GROWTH));
    public static final RegistryObject<Item> SUMMONING_0_DEBUG = reg.register("animal_summoning_debug", () -> new SpellDebug(SBSpellRegistry.ANIMAL_SUMMONING));
    public static final RegistryObject<Item> SUMMONING_1_DEBUG = reg.register("zombie_summoning_debug", () -> new SpellDebug(SBSpellRegistry.NECROMANCY_1));
    public static final RegistryObject<Item> SUMMONING_2_DEBUG = reg.register("skeleton_summoning_debug", () -> new SpellDebug(SBSpellRegistry.NECROMANCY_2));
    public static final RegistryObject<Item> SUMMONING_3_DEBUG = reg.register("wither_skeleton_summoning_debug", () -> new SpellDebug(SBSpellRegistry.NECROMANCY_3));
    public static final RegistryObject<Item> ALCHEMICAL_SUMMONING_DEBUG = reg.register("alchemical_summoning_debug", () -> new SpellDebug(SBSpellRegistry.ALCHEMICAL_SUMMONING));
    public static final RegistryObject<Item> SWINEONING_DEBUG = reg.register("swineoning_debug", () -> new SpellDebug(SBSpellRegistry.SWINEONING));
    public static final RegistryObject<Item> NEKOMANCY_DEBUG = reg.register("nekomancy_debug", () -> new SpellDebug(SBSpellRegistry.NEKOMANCY));

    public static final RegistryObject<Item> FIREWALL_DEBUG = reg.register("firewall_debug", () -> new SpellDebug(SBSpellRegistry.FIREWALL));
    public static final RegistryObject<Item> ICE_SPIKES_DEBUG = reg.register("ice_spikes_debug", () -> new SpellDebug(SBSpellRegistry.FROST_SPIKES));
    public static final RegistryObject<Item> STONE_ARMOR_DEBUG = reg.register("stone_armor_debug", () -> new SpellDebug(SBSpellRegistry.STONE_REINFORCEMENT));
    public static final RegistryObject<Item> WIND_DEBUG = reg.register("wind_debug", () -> new SpellDebug(SBSpellRegistry.OUTWARD_WINDS));
    public static final RegistryObject<Item> JUMP_DEBUG = reg.register("jump_debug", () -> new SpellDebug(SBSpellRegistry.LONG_JUMP));
    public static final RegistryObject<Item> LEVITATE_DEBUG = reg.register("levitation_debug", () -> new SpellDebug(SBSpellRegistry.LEVITATE));
    public static final RegistryObject<Item> EXPLOSION_DEBUG = reg.register("explosion_debug", () -> new SpellDebug(SBSpellRegistry.EXPLOSION));
    public static final RegistryObject<Item> CHAIN_LIGHTNING_DEBUG = reg.register("chain_lightning_debug", () -> new SpellDebug(SBSpellRegistry.CHAIN_LIGHTNING));
    public static final RegistryObject<Item> BARRIER_DEBUG = reg.register("psychic_barrier_debug", () -> new SpellDebug(SBSpellRegistry.PSYCHIC_BARRIER));
    public static final RegistryObject<Item> ILLUSION_DEBUG = reg.register("illusion_debug", () -> new SpellDebug(SBSpellRegistry.ILLUSIONS));
    public static final RegistryObject<Item> PLACATE_DEBUG = reg.register("placate_debug", () -> new SpellDebug(SBSpellRegistry.PLACATE));
    public static final RegistryObject<Item> REVELATION_DEBUG = reg.register("revelation_debug", () -> new SpellDebug(SBSpellRegistry.REVELATION));
    public static final RegistryObject<Item> SNARE_DEBUG = reg.register("snare_debug", () -> new SpellDebug(SBSpellRegistry.DARK_SNARE));
    public static final RegistryObject<Item> LETHARGY_DEBUG = reg.register("lethargy_debug", () -> new SpellDebug(SBSpellRegistry.INSATIABLE_LETHARGY));
    public static final RegistryObject<Item> SCREAM_DEBUG = reg.register("scream_debug", () -> new SpellDebug(SBSpellRegistry.SCREAM));
    public static final RegistryObject<Item> INSPIRATION_DEBUG = reg.register("inspiration_debug", () -> new SpellDebug(SBSpellRegistry.INSPIRATION));
    public static final RegistryObject<Item> LIFE_STEAL_DEBUG = reg.register("life_steal_debug", () -> new SpellDebug(SBSpellRegistry.LIFE_STEAL, 8));
    public static final RegistryObject<Item> SOULBIND_DEBUG = reg.register("soulbind_debug", () -> new SpellDebug(SBSpellRegistry.SOULBIND));
    public static final RegistryObject<Item> ENDER_RIFT_DEBUG = reg.register("ender_rift_debug", () -> new SpellDebug(SBSpellRegistry.ENDER_RIFT));
    public static final RegistryObject<Item> RIFT_OF_DARKNESS_DEBUG = reg.register("dark_rift_debug", () -> new SpellDebug(SBSpellRegistry.RIFT_OF_DARKNESS));
    public static final RegistryObject<Item> TIME_DEBUG = reg.register("time_debug", () -> new SpellDebug(SBSpellRegistry.TIME_SPELL));
    public static final RegistryObject<Item> MACHINE_ENCHANTMENT_DEBUG = reg.register("machine_enchantment_debug", () -> new SpellDebug(SBSpellRegistry.OBJECT_ENCHANTMENT));
    public static final RegistryObject<Item> ACTION_DEBUG = reg.register("action_spell_debug", () -> new SpellDebug(SBSpellRegistry.ACTION_SPELL));
    public static final RegistryObject<Item> DETECTOR_DEBUG = reg.register("detector_spell_debug", () -> new SpellDebug(SBSpellRegistry.DETECTOR_SPELL));
    public static final RegistryObject<Item> COMMAND_DEBUG = reg.register("command_spell_debug", () -> new SpellDebug(SBSpellRegistry.COMMAND_SPELL));

    static {
        initSpawnEggs();
    }

    public static void initSpawnEggs() {
        reg.register("spawn_egg_shade", () -> new ForgeSpawnEggItem(SBEntities.SHADE, 8032420, 6989796, new Item.Properties().tab(SpellBook.TAB)));
        reg.register("spawn_egg_poltergeist", () -> new ForgeSpawnEggItem(SBEntities.POLTERGEIST, 8032420, 3127967, new Item.Properties().tab(SpellBook.TAB)));
        reg.register("spawn_egg_wraith", () -> new ForgeSpawnEggItem(SBEntities.WRAITH, 8032420, 5057091, new Item.Properties().tab(SpellBook.TAB)));
        reg.register("spawn_egg_banshee", () -> new ForgeSpawnEggItem(SBEntities.BANSHEE, 8032420, 12147289, new Item.Properties().tab(SpellBook.TAB)));
        reg.register("spawn_egg_yurei", () -> new ForgeSpawnEggItem(SBEntities.YUREI, 8032420, 3100319, new Item.Properties().tab(SpellBook.TAB)));

        reg.register("spawn_egg_higher_vampire", () -> new ForgeSpawnEggItem(SBEntities.HIGHVAMP0, 7739154, 12751440, new Item.Properties().tab(SpellBook.TAB)));
        reg.register("spawn_egg_vampire", () -> new ForgeSpawnEggItem(SBEntities.LOWVAMP, 7739154, 15106100, new Item.Properties().tab(SpellBook.TAB)));

        reg.register("spawn_egg_necrotic_spider", () -> new ForgeSpawnEggItem(SBEntities.NECROTIC_SPIDER, 2039583, 5382018, new Item.Properties().tab(SpellBook.TAB)));
    }
}