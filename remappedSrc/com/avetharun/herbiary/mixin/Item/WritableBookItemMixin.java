package com.avetharun.herbiary.mixin.Item;

import com.avetharun.herbiary.block.BookMat;
import com.avetharun.herbiary.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WritableBookItem.class)
public class WritableBookItemMixin {
    @Mixin(WrittenBookItem.class)
    public static class WrittenBookItemMixin {
        @Inject(method="useOnBlock", at=@At("HEAD"), cancellable = true)
        public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.LECTERN)) {
                cir.setReturnValue(LecternBlock.putBookIfAbsent(context.getPlayer(), world, blockPos, blockState, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS);
            } else if (blockState.isOf(ModItems.BOOK_MAT.getLeft())) {
                cir.setReturnValue(BookMat.putBookIfAbsent(context.getPlayer(), world, blockPos, blockState, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS);
            }
            else {
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }
    @Inject(method="useOnBlock", at=@At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.LECTERN)) {
            cir.setReturnValue(LecternBlock.putBookIfAbsent(context.getPlayer(), world, blockPos, blockState, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS);
        } else if (blockState.isOf(ModItems.BOOK_MAT.getLeft())) {
            cir.setReturnValue(BookMat.putBookIfAbsent(context.getPlayer(), world, blockPos, blockState, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS);
        }
        else {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
