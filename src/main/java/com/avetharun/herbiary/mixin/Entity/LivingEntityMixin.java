package com.avetharun.herbiary.mixin.Entity;

import com.avetharun.herbiary.entity.ModEntityTypes;
import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract Optional<BlockPos> getSleepingPosition();
    @Inject(method="sleep", at=@At("HEAD"))
    private void sleepInject(BlockPos pos, CallbackInfo ci) {


    }
    @Inject(method="tick", at=@At("TAIL"))
    private void tickPotionEffects(CallbackInfo ci) {
        LivingEntity e = (LivingEntity) (Object)this;
        if (!e.getWorld().isClient) {
            assert e.getWorld().getServer() != null;
            if (e.getWorld().getServer().getTicks() % 5 == 0) {
                e.getWorld().addParticle(ParticleTypes.CHERRY_LEAVES, e.getX(), e.getY(), e.getZ(), 0,0,0);
            }
        }
    }
    @Inject(method="isSleepingInBed", at=@At("HEAD"), cancellable = true)
    private void isSleepingInBed(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity e = (LivingEntity)(Object)this;
        cir.setReturnValue ((Boolean)this.getSleepingPosition().map((pos) -> e.getWorld().getBlockState(pos).getBlock() instanceof BedBlock || alib.getEntitiesOfTypeInRange(e.getWorld(), pos, 3, ModEntityTypes.TENT_ENTITY_TYPE).stream().findAny().isPresent()).orElse(false));
    }
}
