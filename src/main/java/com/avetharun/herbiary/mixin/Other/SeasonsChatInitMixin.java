package com.avetharun.herbiary.mixin.Other;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// \       FRT RQRSEWS,  K,,,,,,,,,,,,V,,VVVVVVVDF  - Ramen, my cute rat. I love him!
@Pseudo
@Mixin(targets = "io.github.lucaargolo.seasons.utils.CompatWarnState", remap = false)
public class SeasonsChatInitMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method="join()V", at=@At("HEAD"), cancellable = true)
    private void joinRedirect(CallbackInfo ci) {
        // Don't show the "Fabric seasons extra" message.
        ci.cancel();
    }
}
