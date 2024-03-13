package com.avetharun.herbiary.mixin.Rendering;

import com.avetharun.dpu.client.ModelOverrides.StringModelOverride;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ModelOverrideList.class)
public abstract class ModelOverrideListMixin {
    @Redirect(method = "<init>(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/List;)V", at=@At(value = "NEW", target = "([Lnet/minecraft/client/render/model/json/ModelOverrideList$InlinedCondition;Lnet/minecraft/client/render/model/BakedModel;)Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;"))
    ModelOverrideList.BakedOverride createCustomInlinedCondition(ModelOverrideList.InlinedCondition[] inlinedConditions, BakedModel model, @Local ModelOverride modelOverride, @Local BakedModel bakedModel, @Local Object2IntMap<Identifier> object2IntMap) {
        inlinedConditions = (ModelOverrideList.InlinedCondition[]) modelOverride.streamConditions().<ModelOverrideList.InlinedCondition>map((condition) -> {
            int i = object2IntMap.getInt(condition.getType());
            if (condition instanceof StringModelOverride.StringModelOverrideCondition sMOC) {
                return (ModelOverrideList.InlinedCondition) new StringModelOverride.InlinedStringModelOverrideCondition(i, sMOC.value);
            } else {
                return new ModelOverrideList.InlinedCondition(i, condition.getThreshold());
            }
        }).toArray(ModelOverrideList.InlinedCondition[]::new);
        return new ModelOverrideList.BakedOverride(inlinedConditions, bakedModel);
    }
//    @Redirect(method="<init>(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/List;)V",
//            at=@At(value = "NEW", target = "([Lnet/minecraft/client/render/model/json/ModelOverrideList$InlinedCondition;Lnet/minecraft/client/render/model/BakedModel;)Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;"))
//    ModelOverrideList.BakedOverride redirectMixin(ModelOverrideList.InlinedCondition[] inlinedConditions, BakedModel bakedModel, @Local(ordinal = 0) ModelOverride override){
//        var bakedOverride = new ModelOverrideList.BakedOverride(inlinedConditions, bakedModel);
//        alib.setMixinField(bakedOverride, "compound", alib.getMixinField(override, "data"));
//        alib.setMixinField(bakedOverride, "isNBTOverride", alib.getMixinField(override, "isNBTOverride"));
//        return bakedOverride;
//    }

//    @Inject(
//            locals = LocalCapture.CAPTURE_FAILEXCEPTION,
//            method="apply", at=@At(shift = At.Shift.BEFORE, value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;test([F)Z"), cancellable = true)
//    private void applyMixin(BakedModel model, ItemStack stack, ClientWorld world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir, Item item, int i, float[] fs, ModelOverrideList.BakedOverride[] var9, int var10, int var11, ModelOverrideList.BakedOverride bakedOverride){
//        boolean bl1 = alib.getMixinField(bakedOverride, "isNBTOverride");
//        if (bl1) {
//            boolean bl2 = alib.runPrivateMixinMethod(bakedOverride, "testNBT", stack);
//            if (bl2) {
//                BakedModel bakedModel = bakedOverride.model;
//                cir.setReturnValue(bakedModel);
//                cir.cancel();
//            }
//        }
//
//    }
}
