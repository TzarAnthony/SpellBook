package com.tzaranthony.spellbook.core.entities.other;

import com.tzaranthony.spellbook.core.entities.friendly.SummonedVex;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;

public class PhoenixAshesEntity extends ItemEntity {
    public int lavaTime = 0;

    public PhoenixAshesEntity(EntityType<? extends PhoenixAshesEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void tick() {
        super.tick();

        BlockPos pos = this.blockPosition();
        if (this.level.dimension() == Level.NETHER || this.level.getBlockState(pos).is(Blocks.LAVA) || this.level.getBlockState(pos).is(Blocks.LAVA_CAULDRON)) {
            ++this.lavaTime;
        }

        ItemStack item = this.getItem();
        if (!this.level.isClientSide && this.lavaTime >= 4800) {
            int hook = net.minecraftforge.event.ForgeEventFactory.onItemExpire(this, item);
            if (hook < 0) this.discard();
            else          this.lifespan += hook;

            EntityType entitytype = SBEntities.SUMMONED_VEX.get();
            SummonedVex phoenix = (SummonedVex) entitytype.spawn((ServerLevel) this.level, item, (Player) null, pos.above(), MobSpawnType.MOB_SUMMONED, false, false);
            if (this.getOwner() != null) {
                phoenix.tame(this.level.getPlayerByUUID(this.getOwner()));
            } else if (this.getThrower() != null) {
                phoenix.tame(this.level.getPlayerByUUID(this.getThrower()));
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("InLavaTime", this.lavaTime);
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.lavaTime = tag.getInt("InLavaTime");
        super.readAdditionalSaveData(tag);
    }
}