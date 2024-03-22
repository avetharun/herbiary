package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.hUtil.ModBlockItem;
import com.avetharun.herbiary.hUtil.alib;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlockPlaceOnMixins {
    @Mixin(CropBlock.class)
    public static class CropBlockMixin{}
    @Mixin(PlantBlock.class)
    public static class PlantBlockMixin{}
}
