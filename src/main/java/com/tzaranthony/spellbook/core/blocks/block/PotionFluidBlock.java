package com.tzaranthony.spellbook.core.blocks.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;

import java.util.List;
import java.util.function.Supplier;

public class PotionFluidBlock extends LiquidBlock {
    public List<MobEffectInstance> EFFECTS;
    private final FlowingFluid fluid;

    public PotionFluidBlock(Supplier<? extends FlowingFluid> supplier, List<MobEffectInstance> effectsIn) {
        super(supplier, Properties.of(Material.WATER)
                .noCollission()
                .strength(100.0F)
                .noDrops());
        this.fluid = null;
        this.EFFECTS = ImmutableList.copyOf(effectsIn);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (!level.isClientSide) {
            if (entityIn instanceof LivingEntity livingentity) {
                if (state.getFluidState().isSource()) {
                    // Make fluid block give all effects of potion
                    for (MobEffectInstance effect : this.EFFECTS)
                        livingentity.addEffect(effect);
                } else {
                    for (MobEffectInstance effect : this.EFFECTS)
                        livingentity.addEffect(new MobEffectInstance(effect.getEffect(), Math.round(effect.getDuration() * 0.01F), Math.max(effect.getAmplifier() - 2, 0)));
                }

                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            }
        }
    }
}