package com.tzaranthony.spellbook.core.blockEntities;

import com.mojang.logging.LogUtils;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class RiftBE extends BlockEntity {
    protected BlockPos tpPos;
    protected ResourceKey<Level> tpDim;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String POS = "BlockPos";
    private static final String DIM = "BlockDimension";

    public RiftBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.RIFT.get(), pos, state);
        this.tpPos = this.getBlockPos();
        this.tpDim = Level.OVERWORLD;
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(POS, NbtUtils.writeBlockPos(this.tpPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.tpDim).resultOrPartial(LOGGER::error).ifPresent((dimId) -> {
            tag.put(DIM, dimId);
        });
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.tpPos = NbtUtils.readBlockPos(tag.getCompound(POS));
        this.tpDim = (Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get(DIM)).result()).get();
    }

    public void setTpPos(BlockPos pos) {
        this.tpPos = pos;
    }

    public void setTpDim(ResourceKey<Level> dim) {
        this.tpDim = dim;
    }

    public BlockPos getTpPos() {
        return this.tpPos;
    }

    public ResourceKey<Level> getTpDim() {
        return this.tpDim;
    }
}