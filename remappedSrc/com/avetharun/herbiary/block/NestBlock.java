package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class NestBlock extends Block{//implements BlockEntityProvider {
    public NestBlock(Settings settings) {
        super(settings);
    }


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockState(pos.down()).isAir() || state.isOf(Blocks.MOVING_PISTON)) {world.breakBlock(pos, false);}
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        return checkType(type, ModItems.BIRD_NEST_BLOCK_ENTITY, NestBlockEntity::tick);
//    }

//    @Nullable
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new NestBlockEntity(pos, state);
//    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.25, 0.875);
    }
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return alib.isBlockIn(world.getBlockState(pos.down()), Herbiary.BIRD_NEST_PLACEABLE);
    }
}
