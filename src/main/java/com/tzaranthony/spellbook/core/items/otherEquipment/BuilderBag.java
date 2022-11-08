package com.tzaranthony.spellbook.core.items.otherEquipment;

import com.tzaranthony.spellbook.core.items.SBItemProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BuilderBag extends Item {
    public BuilderBag() {
        super(SBItemProperties.Standard(Rarity.RARE, 1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack bag = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCrouching()) {
                return InteractionResultHolder.success(player.getItemInHand(hand));
            } else {
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}