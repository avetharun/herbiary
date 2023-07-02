package com.avetharun.herbiary.mixin.Entity;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method="damage", at=@At("HEAD"))
    void damageHook(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

    }
}
