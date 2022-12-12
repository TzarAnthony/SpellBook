package com.tzaranthony.spellbook.client.screens;

import com.tzaranthony.spellbook.core.containers.menus.SBBagMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class SBBagScreen<T extends SBBagMenu> extends AbstractContainerScreen<T> {
    public SBBagScreen(T menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }
}