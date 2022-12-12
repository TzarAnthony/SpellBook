package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBToolMaterial;
import com.tzaranthony.spellbook.core.util.events.SBToolUtils;
import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SBSilverPickaxe extends PickaxeItem {
    public SBSilverPickaxe(SBToolMaterial tier, Properties properties) {
        super(tier, 1, -2.8F, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        if (target.getType().is(SBEntityTags.SILVER_VULNERABLE)) {
            target.hurt(DamageSource.playerAttack((Player) user), SBToolUtils.getExtraAttackDmg((Player) user, target));
        }
        return super.hurtEnemy(stack, target, user);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add((new TranslatableComponent("tooltip.spellbook.silver_tool")));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}