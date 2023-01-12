package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;

import java.util.UUID;

public class SBGlaive extends SwordItem {
    private final float attackDamage;
    protected static final UUID BASE_ATTACK_REACH_UUID = UUID.fromString("1ba8181e-10be-4c5b-8782-cb9458ff5af4");

    public SBGlaive(Tier tier, Item.Properties properties) {
        super(tier, 0, 0.0F, properties);
        this.attackDamage = 6.0F + tier.getAttackDamageBonus();
    }

    @Override
    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }

    public boolean isCorrectToolForDrops(BlockState state) {
        return false;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity user, LivingEntity target) {
        if (target instanceof Player ptgt) {
            maybeDisableShield(user, ptgt);
        }
        return super.hurtEnemy(stack, user, target);
    }

    public static void maybeDisableShield(LivingEntity user, Player ptgt) {
        ItemStack stack1 = ptgt.getUseItem();
        if (!stack1.isEmpty() && stack1.is(Items.SHIELD)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(user) * 0.05F;
            if (user.getRandom().nextFloat() < f) {
                ptgt.getCooldowns().addCooldown(Items.SHIELD, 100);
                user.level.broadcastEntityEvent(ptgt, (byte)30);
            }
        }
    }

    //TODO: still doesn't extend player attack reach? Only block place/break reach?
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.2D, AttributeModifier.Operation.ADDITION));
        if (ForgeMod.REACH_DISTANCE.isPresent()) {
            builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(BASE_ATTACK_REACH_UUID, "Weapon modifier", 4.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        Multimap<Attribute, AttributeModifier> defaultModifiers = builder.build();

        return slot == EquipmentSlot.MAINHAND ? defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return ToolActions.SWORD_SWEEP == (toolAction);
    }
}