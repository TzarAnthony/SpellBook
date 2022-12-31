package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum SBArmorMaterial implements ArmorMaterial {
    // basic
    SILVER(SpellBook.MOD_ID + ":silver", 10, new int[]{2, 5, 5, 2}, 20, SoundEvents.ARMOR_EQUIP_GOLD,
            0.0F, 0.0F, () -> {return Ingredient.of(SBItems.SILVER_INGOT.get());
    }),
    SILVER_STEEL(SpellBook.MOD_ID + ":watchman", 20, new int[]{2, 6, 7, 3}, 25, SoundEvents.ARMOR_EQUIP_CHAIN,
            1.0F, 0.0F, () -> {return Ingredient.of(SBItems.SILVER_STEEL_INGOT.get());
    }),
    IMBUED(SpellBook.MOD_ID + ":neophyte", 20, new int[]{3, 6, 7, 3}, 30, SoundEvents.ARMOR_EQUIP_GENERIC,
            1.0F, 0.0F, () -> {return Ingredient.of(SBItems.IMBUED_CLOTH.get());
    }),

    // Intermediate
    CURSED(SpellBook.MOD_ID + ":necromancer", 30, new int[]{6, 9, 11, 6}, 45, SoundEvents.ARMOR_EQUIP_LEATHER,
            5.0F, 0.2F, () -> {return Ingredient.of(SBItems.CURSED_SILVER_STEEL_INGOT.get());
    }),
    MYSTICAL(SpellBook.MOD_ID + ":witch", 30, new int[]{6, 9, 11, 6}, 45, SoundEvents.ARMOR_EQUIP_LEATHER,
            5.0F, 0.2F, () -> {return Ingredient.of(SBItems.MYSTICAL_SILVER_STEEL_INGOT.get());
    }),

    // Master
    PHOENIX(SpellBook.MOD_ID + ":battlemaster", 60, new int[]{10, 15, 18, 9}, 35, SoundEvents.ARMOR_EQUIP_NETHERITE,
            5.0F, 0.2F, () -> {return Ingredient.of(SBItems.PHOENIX_METAL_INGOT.get());
    }),
    LIGHT(SpellBook.MOD_ID + ":light", 60, new int[]{2, 4, 5, 2}, 50, SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F, 0.3F, () -> {return Ingredient.of(SBItems.ESSENCE_OF_LIGHT.get());
    }),
    CRYSTALAN(SpellBook.MOD_ID + ":crystalan", 60, new int[]{8, 10, 13, 8}, 40, SoundEvents.ARMOR_EQUIP_DIAMOND,
            5.0F, 0.3F, () -> {return Ingredient.of(SBItems.CRYSTALAN_MATRIX.get());
    }),

    // Advanced
    ARCH(SpellBook.MOD_ID + ":archmage", 73, new int[]{10, 15, 18, 9}, 60, SoundEvents.ARMOR_EQUIP_NETHERITE,
            5.0F, 0.2F, () -> {return Ingredient.of(SBItems.DIMENSIONAL_ALLOY.get());
    }),

    // Other
    // Elytra
    ELYTRA(SpellBook.MOD_ID + ":elytra", 60, new int[]{3, 3, 3, 3}, 35, SoundEvents.ARMOR_EQUIP_ELYTRA,
            5.0F, 0.2F, () -> {return Ingredient.of(SBItems.PHOENIX_FEATHER.get());
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durability;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    SBArmorMaterial(String name, int durability, int[] damageReductionAmountArray, int enchantability,
                    SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> p_i231593_10_) {
        this.name = name;
        this.durability = durability;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyLoadedValue<>(p_i231593_10_);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slotType) {
        return HEALTH_PER_SLOT[slotType.getIndex()] * this.durability;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slotType) {
        return this.damageReductionAmountArray[slotType.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}