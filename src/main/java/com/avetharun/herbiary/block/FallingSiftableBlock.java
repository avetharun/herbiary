package com.avetharun.herbiary.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingSiftableBlock extends FallingBlock {
    public FallingSiftableBlock(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SiftableBlock.onUseExt(state, world, pos, player, hand, hit);
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return Block.createCodec(FallingSiftableBlock::new);
    }
}
