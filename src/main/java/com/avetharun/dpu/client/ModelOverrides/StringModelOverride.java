package com.avetharun.dpu.client.ModelOverrides;

import com.avetharun.herbiary.hUtil.alib;
import com.mojang.datafixers.util.Function4;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StringModelOverride implements ClampedModelPredicateProvider {
    public static class StringModelOverrideCondition extends ModelOverride.Condition {
        public final String value;
        public StringModelOverrideCondition(Identifier type, String value) {
            super(type, 0.01f);
            this.value = value;
        }
    }
    public static class InlinedStringModelOverrideCondition extends ModelOverrideList.InlinedCondition {
        public final String value;
        public InlinedStringModelOverrideCondition(int type, String value) {
            super(type, 0.01f);
            this.value = value;
        }
    }
    public static int offset = 1;
    int thisOffset = 0;
    public StringModelOverride(Function4<ItemStack, ClientWorld, LivingEntity, Integer, String> p) {
        this.predicate = p;
        this.thisOffset = offset++;
    }
    public float execute(ItemStack s, ClientWorld w, LivingEntity e, int seed) {
        if (predicate == null) {return 0;}
        return MathHelper.clamp(alib.getHash64(predicate.apply(s,w,e,seed)), 0,1) * 71;
    }
    public final Function4<ItemStack, ClientWorld, LivingEntity, Integer, String> predicate;

    @Override
    public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        return execute(stack, world, entity, seed);
    }
}
