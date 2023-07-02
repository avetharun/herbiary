package com.avetharun.herbiary.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SiftableBlock_SAND extends SandBlock {
    public SiftableBlock_SAND(int color, Settings settings) {
        super(color, settings);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SiftableBlock.onUseExt(state, world, pos, player, hand, hit);
    }
}
