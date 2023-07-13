package com.avetharun.herbiary.mixin.Rendering;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.PigEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PigEntityModel.class)
public class PigEntityModelMixin {
    @ModifyArg(method="<init>", at=@At(value="INVOKE", target = "Lnet/minecraft/client/render/entity/model/QuadrupedEntityModel;<init>(Lnet/minecraft/client/model/ModelPart;ZFFFFI)V"), index = 1)
    private static boolean modifyChildHeadScale(boolean root){
        return true;
    }
}
