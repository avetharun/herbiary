package com.avetharun.herbiary.mixin.Entity;

import com.avetharun.herbiary.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin {
    @Shadow @Nullable public abstract UUID getAngryAt();

    @Shadow public abstract void setAngryAt(@Nullable UUID angryAt);
    @Inject(method="onDeath", at=@At("HEAD"))
    public void onDeathMixin(DamageSource damageSource, CallbackInfo ci) {
        WolfEntity e = (WolfEntity) (Object)this;
        e.getWorld().spawnEntity(new ItemEntity(e.getWorld(),e.getX(), e.getY(), e.getZ(), ModItems.WOLF_HIDE.getDefaultStack()));
    }

    @Inject(method="interactMob", at=@At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
       cir.cancel();
    }
    int ticksSinceLastGrowl = 0;
    @Inject(method="tick", at=@At("TAIL"))
    public void tickMethod(CallbackInfo ci) {
        WolfEntity e = (WolfEntity) (Object)this;
        PlayerEntity p = e.getWorld().getClosestPlayer(e.getX(), e.getY(), e.getZ(), 16, true);
        if (p == null) {
            return;
        } else {
            float d = (float) p.squaredDistanceTo(e);
            if (ticksSinceLastGrowl > 60 + e.getRandom().nextInt(20) && e.getAngryAt() == null) {
                e.playSound(SoundEvents.ENTITY_WOLF_GROWL, 0.5f, 1f);
                ticksSinceLastGrowl = 0;
            }
            if (d < 8 * 8) {
                Objects.requireNonNull(e.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(.5f);
                e.setAngryAt(p.getUuid());
            }
        }
        if (e.getAngryAt() != null && e.getWorld().getPlayerByUuid(e.getAngryAt()).isDead()) {
            e.setAngryAt(null);
        }
        if (e.getAngryAt() == null) {Objects.requireNonNull(e.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.30000001192092896f);}
        ticksSinceLastGrowl++;
    }
}
