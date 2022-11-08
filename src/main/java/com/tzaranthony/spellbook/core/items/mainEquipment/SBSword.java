package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBArmorMaterial;
import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SBSword extends SwordItem {
    public SBToolMaterial material;

    public SBSword(SBToolMaterial tier, Properties properties) {
        super(tier, 3, -2.4F, properties);
        this.material = tier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        if (this.material == SBToolMaterial.SILVER) {
            if (target.getType().is(SBEntityTags.SILVER_VULNERABLE) && user instanceof Player) {
                target.hurt(DamageSource.playerAttack((Player) user), getExtraAttackDmg((Player) user, target));
            }
        } else if (this.material == SBToolMaterial.CURSED) {
            float bonusReduction = bonusReductionPercentage(target, SBArmorMaterial.CURSED);
            if (bonusReduction > 0.0F) {
                target.hurt(SBDamageSource.CURSE, (3 + 4) * bonusReduction);
            }
        } else if (this.material == SBToolMaterial.ENCHANTED) {
            float bonusReduction = bonusReductionPercentage(target, SBArmorMaterial.ENCHANTED);
            if (bonusReduction > 0.0F) {
                target.addEffect(new MobEffectInstance(SBEffects.ENDER_INFECTION.get(), (int) Math.floor(120 * bonusReduction), 2));
            }
        }
        return super.hurtEnemy(stack, target, user);
    }

    public float bonusReductionPercentage(LivingEntity target, ArmorMaterial material) {
        float multiplier = 1.0F;
        ItemStack headItem = target.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack bodyItem = target.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legsItem = target.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feetItem = target.getItemBySlot(EquipmentSlot.FEET);

        if (!headItem.isEmpty() && ((ArmorItem) headItem.getItem()).getMaterial().equals(material)) {
            multiplier = multiplier - 0.15F;
        }
        if (!bodyItem.isEmpty() && ((ArmorItem) bodyItem.getItem()).getMaterial().equals(material)) {
            multiplier = multiplier - 0.40F;
        }
        if (!legsItem.isEmpty() && ((ArmorItem) legsItem.getItem()).getMaterial().equals(material)) {
            multiplier = multiplier - 0.30F;
        }
        if (!feetItem.isEmpty() && ((ArmorItem) feetItem.getItem()).getMaterial().equals(material)) {
            multiplier = multiplier - 0.15F;
        }
        return multiplier;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.getItem() == SBItems.SILVER_SWORD.get()) {
            tooltip.add((new TranslatableComponent("tooltip.spellbook.silver_tool")));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public float getExtraAttackDmg(Player player, LivingEntity target) {
        float f = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1;
        f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), target.getMobType());
        float f2 = player.getAttackStrengthScale(0.5F);
        f *= 0.2F + f2 * f2 * 0.8F;
        f1 *= f2;

        if (f > 0.0F || f1 > 0.0F) {
            boolean flag = f2 > 0.9F;

            boolean flag2 = flag && player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && !player.isSprinting();
            net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, target, flag2, flag2 ? 1.5F : 1.0F);
            flag2 = hitResult != null;
            if (flag2) {
                f *= hitResult.getDamageModifier();
            }
            f += f1;
        }
        return f;
    }
}