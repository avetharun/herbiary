package com.avetharun.herbiary.hUtil.ModStatusEffects;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class Bleed extends StatusEffect {
    int ticksSinceLastHurt;
    public Bleed() {
        super(StatusEffectCategory.NEUTRAL, 0x5c251e00);
    }
    // This method is called every tick to check whether it should apply the status effect or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        ticksSinceLastHurt++;
        // In our case, we just make it return true so that it applies the status effect every tick.
        return ticksSinceLastHurt > 32 / (((double)amplifier*0.5f) + 1);
    }
    // This method is called when it applies the status effect. We implement custom functionality here.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        ticksSinceLastHurt = 0;
        float dmg_amp = amplifier;
        if (dmg_amp < 1) {dmg_amp = 0.5f;}
        entity.damage(new DamageSource("bleeding"), dmg_amp * 0.75f);
        assert MinecraftClient.getInstance().player != null;
        double x, y, z;

        for (int i = 0; i < 10; i++) {
            Vec3d lookVector = entity.getRotationVector().multiply(0.1f);
            Vec3d eye = entity.getEyePos();
            double rx = lookVector.multiply(entity.getRandom().nextFloat() % 4.0f + 2).getX() ;
            double ry = lookVector.multiply(entity.getRandom().nextFloat() % 4.0f + 2).getY();
            double rz = lookVector.multiply(entity.getRandom().nextFloat() % 4.0f + 2).getZ() ;
            x = (eye.getX() + rx);
            y = (eye.getY() + ry) - 0.2f;
            z = (eye.getZ() + rz);
            MinecraftClient.getInstance().player.clientWorld.addParticle(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK,Blocks.RED_CONCRETE.getDefaultState()),
                    x,
                    y,
                    z,
                    0.0,
                    -10.0,
                    0.0
            );
        }
    }
}
