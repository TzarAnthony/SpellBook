package com.tzaranthony.spellbook.core.entities.other;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.tzaranthony.spellbook.registries.SBEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.slf4j.Logger;

import java.util.Set;

public class EffectCarryingArrow extends AbstractArrow {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Set<MobEffectInstance> effects = Sets.newHashSet();
    private static final EntityDataAccessor<Byte> ELEMENT_TYPE = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);

    public EffectCarryingArrow(EntityType<? extends EffectCarryingArrow> entityType, Level level) {
        super(entityType, level);
    }

    public void addEffect(MobEffectInstance effectInstance) {
        this.effects.add(effectInstance);
    }

    public void setElement(byte b) {
        this.entityData.set(ELEMENT_TYPE, b);
    }

    public byte getElement() {
        return this.entityData.get(ELEMENT_TYPE);
    }

    protected void defineSynchedData() {
        this.entityData.define(ELEMENT_TYPE, (byte) 0);
        super.defineSynchedData();
    }

    public ParticleOptions getParticle() {
        if (getElement() == 1) {
            return ParticleTypes.FLAME;
        } else if (getElement() == 2) {
            return ParticleTypes.SNOWFLAKE;
        } else if (getElement() == 3) {
            return ParticleTypes.FIREWORK;
        } else if (getElement() == 4) {
            return ParticleTypes.ASH;
        } else {
            return ParticleTypes.POOF;
        }
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            int i = PotionUtils.getColor(this.effects);
            double d0 = (double)(i >> 16) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;
            for(int j = 0; j < 2; ++j) {
                this.level.addParticle(getParticle(), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
//                this.level.addParticle(getParticle(), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.discard();
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaeffectcloud.setOwner((LivingEntity)entity);
        }

        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(10);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
        for(MobEffectInstance mobeffectinstance : this.effects) {
            areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            areaeffectcloud.setFixedColor(mobeffectinstance.getEffect().getColor());

            if (mobeffectinstance.getEffect() == SBEffects.INCINERATION.get() && entity instanceof Player) {
                createFire(result, (Player) entity);
            }
        }
        this.level.addFreshEntity(areaeffectcloud);
        this.discard();
    }

    protected void createFire(BlockHitResult result, Player player) {
        BlockPos pos = result.getBlockPos();
        BlockState state = this.level.getBlockState(pos);

        if (!CampfireBlock.canLight(state) && !CandleBlock.canLight(state) && !CandleCakeBlock.canLight(state)) {
            BlockPos blockpos1 = pos.relative(result.getDirection());
            if (BaseFireBlock.canBePlacedAt(level, blockpos1, result.getDirection())) {
                level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = BaseFireBlock.getState(level, blockpos1);
                level.setBlock(blockpos1, blockstate1, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
            }
        } else {
            level.playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        }
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }

    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        Entity entity = this.getEffectSource();
        if (!this.effects.isEmpty()) {
            for(MobEffectInstance mobeffectinstance : this.effects) {
                target.addEffect(mobeffectinstance, entity);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("Element", this.getElement());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setElement(tag.getByte("Element"));
    }
}