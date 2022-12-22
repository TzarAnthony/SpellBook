package com.tzaranthony.spellbook.core.items.equipment.equipmentOther;

import com.tzaranthony.spellbook.core.items.SBItemProperties;
import com.tzaranthony.spellbook.core.spells.Spell;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SpellPaper extends Item {
    public Spell spell = null;

    public SpellPaper() {
        super(SBItemProperties.Standard(16));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
         if (this.spell == null) {
             tooltip.add((new TranslatableComponent("tooltip.spellbook.spell_paper")).append("NONE"));
        } else {
             tooltip.add((new TranslatableComponent("tooltip.spellbook.spell_paper")).append(spell.getName()));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell(Spell spell) {
        return this.spell;
    }
}