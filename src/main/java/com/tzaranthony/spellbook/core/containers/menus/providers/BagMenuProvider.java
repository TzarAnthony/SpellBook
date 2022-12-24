package com.tzaranthony.spellbook.core.containers.menus.providers;

import com.tzaranthony.spellbook.core.containers.menus.ArchitectBagMenu;
import com.tzaranthony.spellbook.core.containers.menus.BuilderBagMenu;
import com.tzaranthony.spellbook.core.items.equipment.equipmentOther.ArchitectBag;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BagMenuProvider implements MenuProvider, Container {
    protected final ServerPlayer player;
    protected final ItemStack bag;
    protected final int size;
    protected NonNullList<ItemStack> items;
    private static final String P1 = "Probability1";
    private static final String P2 = "Probability2";
    public int p1;
    public int p2;
    protected final ContainerData data = new ContainerData() {
        public int get(int id) {
            switch(id) {
                case 0:
                    return BagMenuProvider.this.p1;
                case 1:
                    return BagMenuProvider.this.p2;
                default:
                    return 0;
            }
        }

        public void set(int id, int pct) {
            if (90 > (pct + get((id + 1) % 2))) {
                switch(id) {
                    case 0:
                        BagMenuProvider.this.p1 = pct;
                        break;
                    case 1:
                        BagMenuProvider.this.p2 = pct;
                }
                setChanged();
            }
        }

        public int getCount() {
            return 2;
        }
    };

    public BagMenuProvider(ServerPlayer player, ItemStack bag, int size) {
        this.player = player;
        this.bag = bag;
        this.size = size;
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
        this.p1 = 15;
        this.p2 = 15;
        fillContainerFromBagStack();
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.spellbook.builder_bag");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        if (bag.getItem() instanceof ArchitectBag) {
            return new ArchitectBagMenu(id, inv, this, this.data, this.bag);
        }
        return new BuilderBagMenu(id, inv, this, this.data, this.bag);
    }

    public void fillContainerFromBagStack() {
        CompoundTag tag = this.bag.getOrCreateTag();
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amt) {
        return ContainerHelper.removeItem(this.items, slot, amt);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public void setChanged() {
        if (!isEmpty()) {
            for (int i = 0; i < items.size(); ++i) {
                if (this.items.get(i).isEmpty()) {
                    for (int j = i; j < items.size(); ++j) {
                        if (!this.items.get(j).isEmpty()) {
                            this.items.set(i, items.get(j));
                            this.items.set(j, ItemStack.EMPTY);
                            break;
                        }
                    }
                }
            }
        }
        CompoundTag tag = this.bag.getOrCreateTag();
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt(P1, p1);
        tag.putInt(P2, p2);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().contains(this.bag);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    //TODO: upgrade stack limit to 1024? Maybe more?
//    @Override
//    public int getMaxStackSize() {
//        return 1024;
//    }
}