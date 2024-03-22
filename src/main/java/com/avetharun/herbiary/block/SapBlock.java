package com.avetharun.herbiary.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// Summer-fall preferred tree mounted mushroom
public class SapBlock extends Block {
    public SapBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.getBlockState(pos.offset(state.get(Properties.FACING).getOpposite())).isAir()) {world.breakBlock(pos, false);}
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextBetween(0,100) > 99) {
            Vec3d off = switch (state.get(Properties.FACING)) {
                case DOWN -> pos.toCenterPos().add(0, 0.35, 0);
                case UP -> Vec3d.ZERO;
                case EAST, WEST, NORTH, SOUTH -> pos.toCenterPos().offset(state.get(Properties.FACING), -0.45);
            };
            if (state.get(Properties.FACING) == Direction.UP) {
                return;
            }
            world.addParticle(ParticleTypes.DRIPPING_HONEY, true, off.x, off.y, off.z, 0, 0, 0);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(Properties.FACING));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(Properties.FACING);
        return switch (dir) {
            case SOUTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1f);
            case NORTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.9f, 1.0f, 1.0f, 1.0f);
            case WEST ->  VoxelShapes.cuboid(0.9f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST ->  VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 0.1f, 1.0f, 1.0f);
            case DOWN -> VoxelShapes.cuboid(0, 0.6875, 0, 1, 1, 1);
            case UP -> VoxelShapes.cuboid(0, 0, 0, 1, 0.0625, 1);
        };
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.FACING, ctx.getSide());
    }

}
