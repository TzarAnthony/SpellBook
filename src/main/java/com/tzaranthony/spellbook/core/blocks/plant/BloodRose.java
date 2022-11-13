package com.tzaranthony.spellbook.core.blocks.plant;

import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BloodRose extends FlowerBlock {
    public BloodRose() {
        super(MobEffects.WITHER, 8, BlockBehaviour.Properties
                .of(Material.PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
        );
    }

    protected boolean mayPlaceOn(BlockState block, BlockGetter reader, BlockPos pos) {
        return super.mayPlaceOn(block, reader, pos) || block.is(Blocks.NETHERRACK) || block.is(Blocks.SOUL_SAND) || block.is(Blocks.SOUL_SOIL);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, Random offset) {
        float f = offset.nextFloat();
        if (!(f > 0.12F)) {
            Vec3 vec3 = state.getOffset(level, pos);
            double d1 = (double) pos.getX() + 0.5D + vec3.x + ((offset.nextDouble() - 0.5) / 10.0D);
            double d2 = (double) ((float)(pos.getY() + 1) - 0.6875F);
            double d3 = (double) pos.getZ() + 0.5D + vec3.z + ((offset.nextDouble() - 0.5) / 10.0D);
            level.addParticle(SBParticleTypes.BLOOD_DRIP.get(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && level.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof LivingEntity) {

                LivingEntity livingentity = (LivingEntity) entity;
                if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {
                    livingentity.addEffect(new MobEffectInstance(MobEffects.WITHER, 40));
                }
                if (!livingentity.isInvulnerableTo(SBDamageSource.BLEED)) {
                    livingentity.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 40));
                }
            }
        }
    }
}