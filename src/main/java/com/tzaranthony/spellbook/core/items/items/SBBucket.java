package com.tzaranthony.spellbook.core.items.items;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class SBBucket extends BucketItem {
    public int color;

    public SBBucket(Supplier<? extends Fluid> supplier, int color) {
        super(supplier, new Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(SpellBook.TAB));
        this.color = color;
    }

//    public int getcolor() {
//        return this.color;
//    }
}