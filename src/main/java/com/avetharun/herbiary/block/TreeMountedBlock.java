package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class TreeMountedBlock extends HorizontalFacingBlock {
    public TreeMountedBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case SOUTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.4f);
            case NORTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.6f, 1.0f, 1.0f, 1.0f);
            case WEST ->  VoxelShapes.cuboid(0.6f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST ->  VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 0.4f, 1.0f, 1.0f);
            default -> VoxelShapes.empty();
        };
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isAir()) {world.breakBlock(pos, false);}
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }


    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = (Direction)state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isIn(BlockTags.OVERWORLD_NATURAL_LOGS) || blockState.isIn(Herbiary.MUSHROOM_PLACEABLE);
    }

    public boolean canGrowInSeason(int season) {
        return true;
    }
    public boolean growMoreInRain() {return false;}

}
