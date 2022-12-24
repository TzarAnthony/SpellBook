package com.tzaranthony.spellbook.core.items.equipment.equipUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

public class UOC extends UseOnContext {
    private BlockPos pos;

    public UOC(UseOnContext uoc, BlockPos pos) {
        super(uoc.getLevel(), uoc.getPlayer(), uoc.getHand(), uoc.getItemInHand(), new BlockHitResult(uoc.getClickLocation(), uoc.getClickedFace(), uoc.getClickedPos(), uoc.isInside()));
        this.pos = pos;

    }

    @Override
    public BlockPos getClickedPos() {
        return this.pos;
    }
}