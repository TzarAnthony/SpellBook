package com.tzaranthony.spellbook.client.particle;

import com.tzaranthony.spellbook.registries.SBFluids;
import com.tzaranthony.spellbook.registries.SBParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BloodDripParticle extends TextureSheetParticle {
    private final Fluid type;
    protected boolean isGlowing;

    BloodDripParticle(ClientLevel p_106051_, double p_106052_, double p_106053_, double p_106054_, Fluid p_106055_) {
        super(p_106051_, p_106052_, p_106053_, p_106054_);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.type = p_106055_;
    }

    protected Fluid getType() {
        return this.type;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getLightColor(float p_106065_) {
        return this.isGlowing ? 240 : super.getLightColor(p_106065_);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= (double)0.98F;
                this.yd *= (double)0.98F;
                this.zd *= (double)0.98F;
                BlockPos blockpos = new BlockPos(this.x, this.y, this.z);
                FluidState fluidstate = this.level.getFluidState(blockpos);
                if (fluidstate.getType() == this.type && this.y < (double)((float)blockpos.getY() + fluidstate.getHeight(this.level, blockpos))) {
                    this.remove();
                }

            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }

    }

    protected void postMoveUpdate() {
    }

    // Providers
    // Drip
    @OnlyIn(Dist.CLIENT)
    static class DripHangParticle extends BloodDripParticle {
        private final ParticleOptions fallingParticle;

        DripHangParticle(ClientLevel p_106085_, double p_106086_, double p_106087_, double p_106088_, Fluid p_106089_, ParticleOptions p_106090_) {
            super(p_106085_, p_106086_, p_106087_, p_106088_, p_106089_);
            this.fallingParticle = p_106090_;
            this.gravity *= 0.02F;
            this.lifetime = 40;
        }

        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }

        }

        protected void postMoveUpdate() {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class DryingDripHangParticle extends BloodDripParticle.DripHangParticle {
        DryingDripHangParticle(ClientLevel p_106068_, double p_106069_, double p_106070_, double p_106071_, Fluid p_106072_, ParticleOptions p_106073_) {
            super(p_106068_, p_106069_, p_106070_, p_106071_, p_106072_, p_106073_);
        }

        protected void preMoveUpdate() {
            this.rCol = 25.57377049F / (float)(40 - this.lifetime + 25.57377049F);
            this.gCol = 5.312971246F / (float)(40 - this.lifetime + 64.79233227F);
            this.bCol = 5.312971246F / (float)(40 - this.lifetime + 64.79233227F);
            super.preMoveUpdate();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class BloodFlowerHangProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodFlowerHangProvider(SpriteSet p_171960_) {
            this.sprite = p_171960_;
        }

        public Particle createParticle(SimpleParticleType p_171971_, ClientLevel p_171972_, double p_171973_, double p_171974_, double p_171975_, double p_171976_, double p_171977_, double p_171978_) {
            BloodDripParticle dripparticle = new BloodDripParticle.DryingDripHangParticle(p_171972_, p_171973_, p_171974_, p_171975_, SBFluids.STRENGTH.fluid.get(), SBParticleTypes.BLOOD_FALL.get());
            dripparticle.setColor(1.0F, 0.082F, 0.082F);
            dripparticle.pickSprite(this.sprite);
            return dripparticle;
        }
    }



    // Falling
    @OnlyIn(Dist.CLIENT)
    static class FallingParticle extends BloodDripParticle {
        FallingParticle(ClientLevel p_106132_, double p_106133_, double p_106134_, double p_106135_, Fluid p_106136_) {
            this(p_106132_, p_106133_, p_106134_, p_106135_, p_106136_, (int)(64.0D / (Math.random() * 0.8D + 0.2D)));
        }

        FallingParticle(ClientLevel p_172022_, double p_172023_, double p_172024_, double p_172025_, Fluid p_172026_, int p_172027_) {
            super(p_172022_, p_172023_, p_172024_, p_172025_, p_172026_);
            this.lifetime = p_172027_;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    static class FallAndLandParticle extends BloodDripParticle.FallingParticle {
        protected final ParticleOptions landParticle;

        FallAndLandParticle(ClientLevel p_106116_, double p_106117_, double p_106118_, double p_106119_, Fluid p_106120_, ParticleOptions p_106121_) {
            super(p_106116_, p_106117_, p_106118_, p_106119_, p_106120_);
            this.landParticle = p_106121_;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    static class DripstoneFallAndLandParticle extends BloodDripParticle.FallAndLandParticle {
        DripstoneFallAndLandParticle(ClientLevel p_171930_, double p_171931_, double p_171932_, double p_171933_, Fluid p_171934_, ParticleOptions p_171935_) {
            super(p_171930_, p_171931_, p_171932_, p_171933_, p_171934_, p_171935_);
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
                SoundEvent soundevent = this.getType() == Fluids.LAVA ? SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA : SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
                float f = Mth.randomBetween(this.random, 0.3F, 1.0F);
                this.level.playLocalSound(this.x, this.y, this.z, soundevent, SoundSource.BLOCKS, f, 1.0F, false);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class BloodFlowerFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodFlowerFallProvider(SpriteSet p_171939_) {
            this.sprite = p_171939_;
        }

        public Particle createParticle(SimpleParticleType p_171950_, ClientLevel p_171951_, double p_171952_, double p_171953_, double p_171954_, double p_171955_, double p_171956_, double p_171957_) {
            BloodDripParticle dripparticle = new BloodDripParticle.DripstoneFallAndLandParticle(p_171951_, p_171952_, p_171953_, p_171954_, SBFluids.STRENGTH.fluid.get(), SBParticleTypes.BLOOD_LAND.get());
            dripparticle.setColor(0.390F, 0.0507F, 0.0507F);
            dripparticle.pickSprite(this.sprite);
            return dripparticle;
        }
    }


    // Landing
    @OnlyIn(Dist.CLIENT)
    static class DripLandParticle extends BloodDripParticle {
        DripLandParticle(ClientLevel p_106102_, double p_106103_, double p_106104_, double p_106105_, Fluid p_106106_) {
            super(p_106102_, p_106103_, p_106104_, p_106105_, p_106106_);
            this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class BloodFlowerLandProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;

        public BloodFlowerLandProvider(SpriteSet p_106268_) {
            this.sprite = p_106268_;
        }

        public Particle createParticle(SimpleParticleType p_106279_, ClientLevel p_106280_, double p_106281_, double p_106282_, double p_106283_, double p_106284_, double p_106285_, double p_106286_) {
            BloodDripParticle dripparticle = new BloodDripParticle.DripLandParticle(p_106280_, p_106281_, p_106282_, p_106283_, SBFluids.STRENGTH.fluid.get());
            dripparticle.setColor(0.390F, 0.0507F, 0.0507F);
            dripparticle.pickSprite(this.sprite);
            return dripparticle;
        }
    }
}