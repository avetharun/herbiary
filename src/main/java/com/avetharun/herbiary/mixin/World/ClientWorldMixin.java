package com.avetharun.herbiary.mixin.World;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
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
    @Mixin(WorldRenderer.class)
    public static class WorldRendererMixin{
        float delta = 0;
        @WrapWithCondition(method="render", at=@At(value="INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZF)V"))
        boolean renderFogMixin(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta){
            // BackgroundRenderer.applyFog(camera, BackgroundRenderer.FogType.FOG_TERRAIN, Math.max(g, 32.0F), bl2, tickDelta);
//            var type = BackgroundRenderer.FogType.FOG_TERRAIN;
//            if (MinecraftClient.getInstance().player.isSleeping()) {
//                delta += tickDelta * 1.2f;
//                RenderSystem.setShaderFogColor(0, 0, 0);
//                BackgroundRenderer.applyFog(camera, type, viewDistance, true, tickDelta);
//                return false;
//            } else if (delta > 0) {
//
//            }
            return true;
        }
    }

}
