package com.avetharun.herbiary.client.particle;

import com.avetharun.herbiary.Herbiary;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;

@Environment(EnvType.CLIENT)
public class FlintSparkParticle extends AnimatedParticle {
    FlintSparkParticle(ClientWorld clientWorld, double d, double e, double f, double vx, double vy, double vz,SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, spriteProvider, -0.2f);
        this.gravityStrength = 1.75F;
        this.maxAge = 4;
        this.velocityX += vx;
        this.velocityY += vy;
        this.velocityZ += vz;
        this.velocityX *= (double)(this.random.nextFloat() * 0.4F + 0.05F);
        this.velocityY *= (double)(this.random.nextFloat() * 0.4F + 0.05F);
        this.velocityZ *= (double)(this.random.nextFloat() * 0.4F + 0.05F);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public int getBrightness(float tint) {
        int i = super.getBrightness(tint);
        int k = i >> 16 & 255;
        return 240 | k << 16;
    }

    public float getSize(float tickDelta) {
        float f = ((float)this.age + tickDelta) / (float)this.maxAge;
        return .2f;
    }

    public void tick() {
        super.tick();
        this.setSpriteForAge(spriteProvider);
        if (!this.dead) {
            float f = (float)this.age / (float)this.maxAge;
            if (this.random.nextFloat() > f) {
                this.world.addParticle(Herbiary.FLINT_SPARK_SMOKE, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
            }
        }

    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            FlintSparkParticle lavaEmberParticle = new FlintSparkParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            lavaEmberParticle.setSprite(this.spriteProvider);
            return lavaEmberParticle;
        }
    }
    @Environment(EnvType.CLIENT)
    public static class FlintSmokeParticle extends AscendingParticle {
        protected FlintSmokeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
            super(world, x, y, z, 0.1F, 0.1F, 0.1F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0.3F, 2, -0.1F, true);
        }

        @Override
        public float getSize(float tickDelta) {
            return 0.005f;
        }

        @Environment(EnvType.CLIENT)
        public static class Factory implements ParticleFactory<DefaultParticleType> {
            private final SpriteProvider spriteProvider;

            public Factory(SpriteProvider spriteProvider) {
                this.spriteProvider = spriteProvider;
            }

            public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
                return new FlintSmokeParticle(clientWorld, d, e, f, g, h, i, 1.0F, this.spriteProvider);
            }
        }
    }
}
