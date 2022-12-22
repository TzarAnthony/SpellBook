package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public interface SilverBasedTool {
    default float getExtraAttackDmg(Player player, LivingEntity target) {
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