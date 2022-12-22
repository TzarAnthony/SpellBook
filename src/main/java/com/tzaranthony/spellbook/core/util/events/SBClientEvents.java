package com.tzaranthony.spellbook.core.util.events;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.spells.Binding;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = SpellBook.MOD_ID, value = Dist.CLIENT)
public class SBClientEvents {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPostRenderEntity(RenderLivingEvent.Post event) {
        LivingEntity le = event.getEntity();
        if (Binding.isBound(le) && le instanceof Mob bound) {
            Entity owner = Binding.isBoundBy(le);
            if (owner instanceof LivingEntity leOwner) {
//                double d0 = Mth.lerp(event.getPartialTick(), le.xOld, le.getX());
//                double d1 = Mth.lerp(event.getPartialTick(), le.yOld, le.getY());
//                double d2 = Mth.lerp(event.getPartialTick(), le.zOld, le.getZ());
//                event.getPoseStack().pushPose();
//                event.getPoseStack().translate(-d0, -d1, -d2);
                Binding.renderSoulBind(bound, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), leOwner);
//                event.getPoseStack().popPose();
            }
        }
    }
}