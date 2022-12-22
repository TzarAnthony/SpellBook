package com.tzaranthony.spellbook.core.items.equipment.creative;

import com.tzaranthony.spellbook.core.items.SBItemProperties;
import com.tzaranthony.spellbook.core.spells.Spell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SpellDebug extends Item {
    public Spell spell;
    public int cooldown;

    public SpellDebug(Spell spell) {
        super(SBItemProperties.SpellDebug());
        this.spell = spell;
        this.cooldown = 20;
    }

    public SpellDebug(Spell spell, int cooldown) {
        super(SBItemProperties.SpellDebug());
        this.spell = spell;
        this.cooldown = cooldown;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        context.getPlayer().getCooldowns().addCooldown(this, this.cooldown);
        context.getClickedPos();
        if (this.spell.perform_spell(context.getLevel(), context.getPlayer(), context.getHand(), context.getClickedPos())) {
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        } else {
            return super.useOn(context);
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.getCooldowns().addCooldown(this, this.cooldown);
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.spell.perform_spell(level, player, hand, player.blockPosition())) {
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }
}