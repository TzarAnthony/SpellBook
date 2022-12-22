package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum SBToolMaterial implements Tier {
    // basic
    SILVER(2, 195, 9.5F, 0.0F, 20, () -> {
        return Ingredient.of(SBItems.SILVER_INGOT.get());
            }),
    SILVER_STEEL(2, 195, 9.5F, 3.0F, 20, () -> {
        return Ingredient.of(SBItems.SILVER_INGOT.get());
    }),

    // Intermediate
    // Necromancer Robes
    CURSED(5, 3524, 12.0F, 8.0F, 35, () -> {
        return Ingredient.of(SBItems.CURSED_SILVER_STEEL_INGOT.get());
    }),
    // Witch Robes
    ENCHANTED(5, 3524, 14.0F, 6.0F, 35, () -> {
        return Ingredient.of(SBItems.ENCHANTED_SILVER_STEEL_INGOT.get());
    }),

    // Master
    // Battlemaster Robes
    PHOENIX(6, 5863, 17.0F, 12.0F, 35, () -> {
        return Ingredient.of(SBItems.PHOENIX_METAL_INGOT.get());
    }),
    // Robes of Light
    LIGHT(8, 356, 25.0F, 9.5F, 40, () -> {
        return Ingredient.of(SBItems.ESSENCE_OF_LIGHT.get());
    }),
    // Crystal Sorcerer Robes
    CRYSTALAN(6, 5863, 17.0F, 12.0F, 35, () -> {
        return Ingredient.of(SBItems.CRYSTALAN_MATRIX.get());
    }),

    // Advanced
    // Archmage Robes
    ARCH(6, 50863, 20.0F, 20.0F, 50, () -> {
        return Ingredient.of(SBItems.DYE_BASE.get());
            });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    SBToolMaterial(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}