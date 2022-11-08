package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBArmorMaterial;
import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class SBAxe extends AxeItem {
    public SBToolMaterial material;

    public SBAxe(SBToolMaterial tier, Properties properties) {
        super(tier, 6, -3.0F, properties);
        this.material = tier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        if (this.material == SBToolMaterial.SILVER && user instanceof Player) {
            if (target.getType().is(SBEntityTags.SILVER_VULNERABLE)) {
                target.hurt(DamageSource.playerAttack((Player) user), this.getAttackDamage());
            }
        } else if (this.material == SBToolMaterial.CURSED) {
            float bonusReduction = bonusReductionPercentage(target, SBArmorMaterial.CURSED);
            if (bonusReduction > 0.0F) {
                target.hurt(SBDamageSource.CURSE, 3 * bonusReduction * 1.1F);
            }
        } else if (this.material == SBToolMaterial.ENCHANTED) {
            float bonusReduction = bonusReductionPercentage(target, SBArmorMaterial.ENCHANTED);
            if (bonusReduction > 0.0F) {
                target.addEffect(new MobEffectInstance(SBEffects.ENDER_INFECTION.get(), (int) Math.floor(120 * bonusReduction), 1));
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
}