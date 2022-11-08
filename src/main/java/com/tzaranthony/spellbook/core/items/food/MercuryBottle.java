package com.tzaranthony.spellbook.core.items.food;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MercuryBottle extends ThickDrink {
    public MercuryBottle() {
        super((new Item.Properties())
                .tab(SpellBook.TAB)
                .rarity(Rarity.EPIC)
                .craftRemainder(Items.GLASS_BOTTLE)
                .food((new FoodProperties.Builder())
                        .nutrition(6)
                        .saturationMod(1.5F)
                        .effect(new MobEffectInstance(MobEffects.BLINDNESS, 1200), 0.075F)
                        .effect(new MobEffectInstance(MobEffects.POISON, 1200), 0.05F)
                        .effect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 2), 0.05F)
                        .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 2), 0.025F)
                        .alwaysEat()
                        .build()
                )
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.spellbook.mercury_bottle"));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayer) {
            if (entityLiving.hasEffect(SBEffects.MERCURY_POISONING.get())) {
                int amp = entityLiving.getEffect(SBEffects.MERCURY_POISONING.get()).getAmplifier();
                entityLiving.addEffect(new MobEffectInstance(SBEffects.MERCURY_POISONING.get(), 1200, Math.min(amp + 1, 9)));
            } else {
                entityLiving.addEffect(new MobEffectInstance(SBEffects.MERCURY_POISONING.get(), 1200));
            }
        }
        return super.finishUsingItem(stack, level, entityLiving);
    }
}