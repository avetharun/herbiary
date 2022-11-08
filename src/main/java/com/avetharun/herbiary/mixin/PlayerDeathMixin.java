package com.avetharun.herbiary.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDeathMixin {
    @Inject(method="onDeath", at=@At("TAIL"))
    void onPlayerDeathMixed(DamageSource damageSource, CallbackInfo ci) {}
}
