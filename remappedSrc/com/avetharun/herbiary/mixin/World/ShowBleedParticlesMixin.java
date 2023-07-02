package com.avetharun.herbiary.mixin.World;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class ShowBleedParticlesMixin {
    @Shadow @Final private StatusEffect type;

    @Shadow private boolean showParticles;

    @Shadow public abstract StatusEffect getEffectType();

    @Inject(method = "shouldShowParticles()Z", at=@At("HEAD"), cancellable = true)
    void _shouldShowParticles(CallbackInfoReturnable<Boolean> cir) {
        int _1 = StatusEffect.getRawId(this.getEffectType());
        int _2 = StatusEffect.getRawId(Herbiary.EFFECT_BLEED);
        if (_1 == _2){
            this.showParticles = false;
            cir.setReturnValue(false);
        }
        cir.setReturnValue(this.showParticles);
        cir.cancel();
    }
};
