package com.tzaranthony.spellbook.core.items.equipment.equipmentMain;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class SBGlaive extends TieredItem implements Vanishable {
    private final float attackDamage;
//    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    protected static final UUID BASE_ATTACK_REACH_UUID = UUID.fromString("1ba8181e-10be-4c5b-8782-cb9458ff5af4");

    public SBGlaive(Tier tier, Item.Properties properties) {
        super(tier, properties);
        this.attackDamage = 6.0F + tier.getAttackDamageBonus();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return !p_43294_.isCreative();
    }

    public float getDestroySpeed(ItemStack p_43288_, BlockState p_43289_) {
        Material material = p_43289_.getMaterial();
        return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !p_43289_.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity user, LivingEntity target) {
        stack.hurtAndBreak(1, target, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
        if (p_43284_.getDestroySpeed(p_43283_, p_43285_) != 0.0F) {
            p_43282_.hurtAndBreak(2, p_43286_, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

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
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }
}