package com.tzaranthony.spellbook.client.itemRender;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.items.equipment.equipmentOther.ResearchBook;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.client.color.item.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SpellBook.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SBItemColorRender extends ItemColors {

    @SubscribeEvent
    public static void renderColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, layer) -> {return layer > 0 ? -1 : ResearchBook.getColor(stack);}, SBItems.RESEARCH_BOOK.get());
    }
}