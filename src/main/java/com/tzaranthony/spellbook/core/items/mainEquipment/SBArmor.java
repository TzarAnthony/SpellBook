package com.tzaranthony.spellbook.core.items.mainEquipment;

import com.tzaranthony.spellbook.core.items.SBArmorMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SBArmor extends ArmorItem {
    public SBArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (((ArmorItem) stack.getItem()).getMaterial() == SBArmorMaterial.CURSED) {
            tooltip.add((new TranslatableComponent("tooltip.spellbook.necro_armor")));
            tooltip.add((new TranslatableComponent("tooltip.spellbook.necro_set")));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}