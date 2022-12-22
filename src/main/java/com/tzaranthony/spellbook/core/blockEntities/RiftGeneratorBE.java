package com.tzaranthony.spellbook.core.blockEntities;

import com.mojang.logging.LogUtils;
import com.tzaranthony.spellbook.core.items.items.CoordinationTome;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import com.tzaranthony.spellbook.registries.SBBlocks;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.Optional;

public class RiftGeneratorBE extends SingleItemBE {
    protected BlockPos tpPos;
    protected ResourceKey<Level> tpDim;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String POS = "BlockPos";
    private static final String DIM = "BlockDimension";

    public RiftGeneratorBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.RIFT_GENERATOR.get(), pos, state);
        this.tpPos = this.getBlockPos();
        this.tpDim = Level.OVERWORLD;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(POS, NbtUtils.writeBlockPos(this.tpPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.tpDim).resultOrPartial(LOGGER::error).ifPresent((dimId) -> {
            tag.put(DIM, dimId);
        });
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.tpPos = NbtUtils.readBlockPos(tag.getCompound(POS));
        this.tpDim = (Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get(DIM)).result()).get();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put(POS, NbtUtils.writeBlockPos(this.tpPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.tpDim).resultOrPartial(LOGGER::error).ifPresent((dimId) -> {
            tag.put(DIM, dimId);
        });
        return tag;
    }

    @Override
    public void playAddSound() {
        this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void playRemoveSound() {
        this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public boolean changeItem(Player player, ItemStack stack) {
        if (stack.is(SBItems.TOME_OF_COORDINATION.get()) || (player.isShiftKeyDown() && stack.isEmpty())) {
            boolean changed = super.changeItem(player, stack);
            setData();
            return changed;
        }
        return false;
    }

    protected void setData() {
        CompoundTag tag = this.getItem().getOrCreateTag();
        if (CoordinationTome.isLinked(tag)) {
            this.tpPos = CoordinationTome.getTPPos(tag);
            this.tpDim = CoordinationTome.getTPDim(tag).get();
        }
    }

    public boolean updatePortals(boolean activate) {
        if (activate) {
            this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
            createPortals();
            return true;
        } else {
            this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, 1.0F, 1.0F);
            removePortals();
            return false;
        }
    }

    protected void createPortals() {
        this.level.setBlock(this.getBlockPos().above(1), SBBlocks.RIFT.get().defaultBlockState(), 1);

        if (this.level instanceof ServerLevel) {
            setPortalData((ServerLevel) this.level, this.getBlockPos().above(1), this.tpDim, this.tpPos);
        }

        if(this.tpDim != this.getLevel().dimension() && this.level instanceof ServerLevel) {
            ServerLevel level1 = ((ServerLevel) this.level).getServer().getLevel(this.tpDim);
            level1.setBlock(this.tpPos, SBBlocks.RIFT.get().defaultBlockState(), 1);
            setPortalData(level1, this.tpPos, this.level.dimension(), this.getBlockPos().above());
        } else {
            this.level.setBlock(this.tpPos, SBBlocks.RIFT.get().defaultBlockState(), 1);
            if (this.level instanceof ServerLevel) {
                setPortalData((ServerLevel) this.level, this.tpPos, this.level.dimension(), this.getBlockPos().above());
            }
        }
    }

    public void removePortals() {
        this.level.setBlock(this.getBlockPos().above(1), Blocks.AIR.defaultBlockState(), 1);

        CompoundTag tag = this.getItem().getOrCreateTag();
        if (CoordinationTome.isLinked(tag)) {
            BlockPos pos = CoordinationTome.getTPPos(tag);
            Optional<ResourceKey<Level>> dim = CoordinationTome.getTPDim(tag);

            if(dim.get() != this.getLevel().dimension() && this.level instanceof ServerLevel) {
                ServerLevel level1 = ((ServerLevel) this.level).getServer().getLevel(dim.get());
                level1.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
            } else {
                this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
            }
        }
    }

    protected void setPortalData(ServerLevel beLevel, BlockPos bePos, ResourceKey<Level> tpDim, BlockPos tpPos) {
        BlockEntity blockentity = beLevel.getBlockEntity(bePos);
        if (blockentity instanceof RiftBE rift) {
            rift.setTpDim(tpDim);
            rift.setTpPos(tpPos);
        }
    }
}