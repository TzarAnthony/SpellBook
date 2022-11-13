package com.tzaranthony.spellbook.core.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockCreateSpell extends ProjectileSpell{
    private final RegistryObject<Block> block;

    public BlockCreateSpell(int id, String name, SpellTier tier, RegistryObject<Block> block) {
        super(id, name, tier);
        this.block = block;
    }

    public Block getBlock() {
        return  this.block.get();
    }

    public boolean perform_spell(Entity user, Entity target) {
        BlockPos pos = getOpenFace(target.getLevel(), target.getOnPos(), 1);
        return target.level.setBlock(pos, getBlock().defaultBlockState(), 2);
    }

    public boolean perform_spell(Entity user, Level level, BlockPos pos) {
        BlockPos pos2 = getOpenFace(level, pos, 1);
        if (pos == pos2) {
            return false;
        }
        return level.setBlock(pos2, getBlock().defaultBlockState(), 2);
    }

    protected BlockPos getOpenFace(Level level, BlockPos pos, int sizeReq) {
        BlockPos tmpPos = pos.above();
        if (hasRadialSpace(level, tmpPos, sizeReq)) {
            return tmpPos;
        }
        tmpPos = pos.north();
        if (hasRadialSpace(level, tmpPos, sizeReq)) {
            return tmpPos;
        }
        tmpPos = pos.south();
        if (hasRadialSpace(level, tmpPos, sizeReq)) {
            return tmpPos;
        }
        tmpPos = pos.east();
        if (hasRadialSpace(level, tmpPos, sizeReq)) {
            return tmpPos;
        }
        tmpPos = pos.west();
        if (hasRadialSpace(level, tmpPos, sizeReq)) {
            return tmpPos;
        }
        tmpPos = pos.below();
        if (hasRadialSpace(level, tmpPos, sizeReq)) {
            return tmpPos;
        }
        return pos;
    }

    protected boolean hasRadialSpace(Level level, BlockPos pos, int sizeReq) {
        if (!level.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        } else if (sizeReq == 1) {
            return true;
        }

        boolean a = false;
        boolean n = false;
        boolean s = false;
        boolean e = false;
        boolean w = false;
        boolean b = false;
        for (int i = 1; i < sizeReq; ++i) {
            b = b || level.getBlockState(pos.above(i)).getMaterial().isReplaceable();
            n = n || level.getBlockState(pos.north(i)).getMaterial().isReplaceable();
            s = s || level.getBlockState(pos.south(i)).getMaterial().isReplaceable();
            e = e || level.getBlockState(pos.east(i)).getMaterial().isReplaceable();
            w = w || level.getBlockState(pos.west(i)).getMaterial().isReplaceable();
            b = b || level.getBlockState(pos.below(i)).getMaterial().isReplaceable();
        }
        return a || n || s || e || w || b;
    }
}