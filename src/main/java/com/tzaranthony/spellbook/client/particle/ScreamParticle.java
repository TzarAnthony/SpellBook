package com.tzaranthony.spellbook.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreamParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected ScreamParticle(ClientLevel p_234028_, double p_234029_, double p_234030_, double p_234031_, double p_234032_, SpriteSet p_234033_) {
        super(p_234028_, p_234029_, p_234030_, p_234031_, 0.0D, 0.0D, 0.0D);
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        this.sprites = p_234033_;

        this.lifetime = 16;
        this.quadSize = 1.5F;
        this.setSpriteFromAge(p_234033_);
    }

    public int getLightColor(float p_106921_) {
        return 15728880;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_234036_) {
            this.sprites = p_234036_;
        }

        public Particle createParticle(SimpleParticleType p_234047_, ClientLevel p_234048_, double p_234049_, double p_234050_, double p_234051_, double p_234052_, double p_234053_, double p_234054_) {
            return new ScreamParticle(p_234048_, p_234049_, p_234050_, p_234051_, p_234052_, this.sprites);
        }
    }
}