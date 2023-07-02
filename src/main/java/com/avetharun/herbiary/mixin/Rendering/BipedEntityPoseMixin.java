package com.avetharun.herbiary.mixin.Rendering;

import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityPoseMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Inject(at=@At("HEAD"), method = "positionRightArm", cancellable = true)
    void posRightArmMixin(T entity, CallbackInfo ci) {
        if (entity.isPlayer() && (boolean) alib.getMixinField(entity, "isSifting")) {
            this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.62831855F;
            this.rightArm.yaw = (float) (Math.sin(MinecraftClient.getInstance().getRenderTime() * 1d) * 0.25f);
        };
    }
}
