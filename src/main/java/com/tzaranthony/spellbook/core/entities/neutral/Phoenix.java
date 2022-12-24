package com.tzaranthony.spellbook.core.entities.neutral;

import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.Nullable;

//TODO: create -- 3 to 5 blocks wingspawn
public class Phoenix extends TamableAnimal implements Shearable, IForgeShearable {
    private static final EntityDataAccessor<Integer> DATA_FEATHER_COUNT = SynchedEntityData.defineId(Phoenix.class, EntityDataSerializers.INT);

    protected Phoenix(EntityType<? extends Phoenix> phoenix, Level Level) {
        super(phoenix, Level);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.SHEARS) {
            if (!this.level.isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                itemstack.hurtAndBreak(1, player, (playerEntity) -> {
                    playerEntity.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public void shear(SoundSource source) {
        this.level.playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.iterateFeatherCount();
        ItemEntity itementity = this.spawnAtLocation(SBItems.PHOENIX_FEATHER.get(), 1);
        itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
    }

    public void iterateFeatherCount() {
        int i = this.entityData.get(DATA_FEATHER_COUNT);
        this.entityData.set(DATA_FEATHER_COUNT, --i);
    }

    public boolean readyForShearing() {
        return this.isAlive() && this.entityData.get(DATA_FEATHER_COUNT) > 0;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }
}