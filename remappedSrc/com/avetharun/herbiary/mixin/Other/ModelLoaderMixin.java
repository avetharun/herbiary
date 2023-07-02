package com.avetharun.herbiary.mixin.Other;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(targets = "net.minecraft.client.render.model.json.ModelOverride$Deserializer")
public class ModelLoaderMixin {
    @Inject(method="deserializeMinPropertyValues", at=@At("HEAD"), cancellable = true)
    private void onDeserializeMinPropertyValues(JsonObject object, CallbackInfoReturnable<List<ModelOverride.Condition>> cir) {
        Map<Identifier, Float> map = Maps.newLinkedHashMap();
        JsonObject jsonObject = JsonHelper.getObject(object, "predicate");

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
            if (JsonHelper.isBoolean((JsonElement) stringJsonElementEntry.getValue())) {
                boolean s = JsonHelper.asBoolean((JsonElement) stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey());
                float v = s ? 0.07991f : 0;
                map.put(new Identifier((String) stringJsonElementEntry.getKey()), v);
                continue;
            }
            map.put(new Identifier((String) stringJsonElementEntry.getKey()), JsonHelper.asFloat((JsonElement) stringJsonElementEntry.getValue(), (String) stringJsonElementEntry.getKey()));
        }

        cir.setReturnValue (map.entrySet().stream().map((entry) -> {
            return new ModelOverride.Condition((Identifier)entry.getKey(), (Float)entry.getValue());
        }).collect(ImmutableList.toImmutableList()));
    }
}
