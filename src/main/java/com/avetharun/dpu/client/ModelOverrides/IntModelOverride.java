package com.avetharun.dpu.client.ModelOverrides;

import com.mojang.datafixers.util.Function4;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class IntModelOverride implements ClampedModelPredicateProvider {
    public static int offset = 1;
    int thisOffset = 0;
    public IntModelOverride(Function4<ItemStack, ClientWorld, LivingEntity, Integer, Integer> p) {
        this.predicate = p;
        this.thisOffset = offset++;
    }
    public float execute(ItemStack s, ClientWorld w, LivingEntity e, int seed) {
        if (predicate == null) {return 0;}
        return predicate.apply(s,w,e,seed);
    }
    public final Function4<ItemStack, ClientWorld, LivingEntity, Integer, Integer> predicate;

    @Override
    public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        return execute(stack, world, entity, seed);
    }
}
