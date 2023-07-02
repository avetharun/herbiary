package com.avetharun.herbiary.mixin.Other;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public interface BooleanModelPredicateProvider extends ClampedModelPredicateProvider {
    enum BOOLEAN_STATE{
        TRUE(0.07991f),
        FALSE(0f);
        final float value;
        BOOLEAN_STATE(float f) {value = f;}

        public float getValue() { return value; }
    }

    @Override
    default float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        return booleanCall(stack, world, entity, seed) ? 0.07991f : 0;
    }
    default boolean booleanCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        return false;
    }
}
