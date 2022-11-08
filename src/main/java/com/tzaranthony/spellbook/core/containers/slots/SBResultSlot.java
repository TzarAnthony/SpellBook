package com.tzaranthony.spellbook.core.containers.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SBResultSlot extends SlotItemHandler {
    private final Player player;
    private int removeCount;

    public SBResultSlot(Player player, IItemHandler itemHandler, int id, int x, int y) {
        super(itemHandler, id, x, y);
        this.player = player;
    }

    public boolean mayPlace(ItemStack p_39553_) {
        return false;
    }

    public ItemStack remove(int amt) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amt, this.getItem().getCount());
        }

        return super.remove(amt);
    }

    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
    }

    protected void onQuickCraft(ItemStack stack, int amt) {
        this.removeCount += amt;
    }
}