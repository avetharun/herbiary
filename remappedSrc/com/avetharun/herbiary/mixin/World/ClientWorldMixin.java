package com.avetharun.herbiary.mixin.World;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Mixin(BackgroundRenderer.DarknessFogModifier.class)
    public static class DarknessFogMixin {

        @Inject(method = "applyStartEndModifier", at = @At(value = "JUMP", ordinal = 0, shift = At.Shift.AFTER))
        void DarknessFogModifier_applyStartEndModifier(BackgroundRenderer.FogData fogData, LivingEntity entity, StatusEffectInstance effect, float viewDistance, float tickDelta, CallbackInfo ci, @Local float f) {
            if (fogData.)
        }
    }

}
