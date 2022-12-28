package com.tzaranthony.spellbook.core.util.events;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.spells.Binding;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpellBook.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SBServerEvents {
    @SubscribeEvent
    //TODO: move the modifiers to here instead of the items
    static void onBreakEvent(BlockEvent.BreakEvent event) {
        System.out.println("Just broke a block...");
//        ItemStack stack = event.getPlayer().getMainHandItem();
//        if (stack.getItem() instanceof MiningModeTool mmItem && !event.getPlayer().level.isClientSide()) {
//            ServerPlayer player = (ServerPlayer) event.getPlayer();
//            ServerLevel level = (ServerLevel) event.getPlayer().level;
//            event.getPos();
//            mmItem.mineBlocks(stack, level, player, event.getPos());
//        }
    }


    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity le) {
            if (!event.getEntity().isShiftKeyDown() && Binding.isBound(le)) {
                Binding.unbindEntity(le);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        LivingEntity le = event.getEntityLiving();
        if (Binding.isBound(le)) {
            Binding.tickSoulBind(le);
        }
    }
}