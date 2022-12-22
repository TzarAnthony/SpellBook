package com.tzaranthony.spellbook.core.items.equipment.equipmentOther;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DeathBouquet extends Item {
    public static final String ACTIVE = "UltraDeathActive";
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public DeathBouquet() {
        super(new Item.Properties().durability(10).rarity(Rarity.RARE).setNoRepair().tab(SpellBook.TAB));

        this.attackDamage = 6.0F;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.0D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        stack.hurtAndBreak(1, user, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        if (checkActive(stack)) {
            target.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 400, 1));
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 1));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 1));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1));
        } else {
            target.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 200, 0));
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0));
        }
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, user, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stackB = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack stackF = player.getItemInHand(InteractionHand.OFF_HAND);
        if (!level.isClientSide() && stackB.getItem() == SBItems.DEATH_BOUQUET.get() && stackF.getItem() == Items.FLINT_AND_STEEL) {
            player.getCooldowns().addCooldown(this, 20);
            level.playSound((Player) null, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            stackF.hurtAndBreak(1, player, (entity) -> {
                entity.broadcastBreakEvent(InteractionHand.OFF_HAND);
            });
            stackB.setDamageValue(Math.max(stackB.getDamageValue() - 9,9));
            setActive(stackB);
            return InteractionResultHolder.sidedSuccess(stackB, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(stackB);
        }
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot hand) {
        return hand == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(hand);
    }

    public void setActive(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putBoolean(ACTIVE, true);
    }

    public boolean checkActive(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        if (compoundtag.contains(ACTIVE)) {
            return compoundtag.getBoolean(ACTIVE);
        }
        compoundtag.putBoolean(ACTIVE, false);
        return false;
    }
}