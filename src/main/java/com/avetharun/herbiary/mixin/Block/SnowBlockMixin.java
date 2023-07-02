package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.hUtil.Season;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {
    @Shadow @Final public static IntProperty LAYERS;

    @Inject(method = "randomTick", at=@At("HEAD"))
    void randomTickMixin(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
        if (!Herbiary.CURRENT_SEASON.equals(Season.WINTER)) {
            int _layers = state.get(LAYERS);
            if (_layers <= 0) {
                world.removeBlock(pos, false);
            }
        }

    }
}
