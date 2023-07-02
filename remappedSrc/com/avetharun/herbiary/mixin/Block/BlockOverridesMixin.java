package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.block.SpruceLogBlock;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlockOverridesMixin {
    @Mixin(Blocks.class)
    public static class BlockInitMixin {
        @Shadow @Final public static Block SPRUCE_LOG;

        @Inject(method="createLogBlock", at=@At("HEAD"), cancellable = true)
        private static void createLogBlockOverride(MapColor topMapColor, MapColor sideMapColor, CallbackInfoReturnable<PillarBlock> cir) {
            if (topMapColor.equals(MapColor.SPRUCE_BROWN)) {

                cir.setReturnValue(new SpruceLogBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> {
                            return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor;
                        }).strength(2.0F).sounds(BlockSoundGroup.WOOD))
                );
                cir.cancel();
                return;
            }
            cir.setReturnValue(new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> {
                return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor;
            }).strength(2.0F).sounds(BlockSoundGroup.WOOD))
            );
            cir.cancel();
        }

    }
}
