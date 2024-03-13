package com.avetharun.herbiary.mixin.Rendering;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ClientTimerMixin {
    @Inject(method="render", at=@At("HEAD"))
    void renderMixin(boolean tick, CallbackInfo ci){
        // Overflow check!
        if (Herbiary.clientRuntime < 0) {Herbiary.clientRuntime=0;}
        Herbiary.clientRuntime += MinecraftClient.getInstance().getTickDelta();
    }
}
