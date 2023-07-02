package com.avetharun.herbiary.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.GravelBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SiftableBlock_GRAVEL extends GravelBlock {
    public SiftableBlock_GRAVEL(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SiftableBlock.onUseExt(state, world, pos, player, hand, hit);
    }
}
