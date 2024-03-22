package com.avetharun.herbiary.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PartiallySubmergedBlock extends TallPlantBlock implements Waterloggable {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape SHAPE;

    public PartiallySubmergedBlock(Settings settings) {
        super(settings);
    }


    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, WATERLOGGED);
    }
    static {
        HALF = TallPlantBlock.HALF;
        WATERLOGGED = Properties.WATERLOGGED;
        SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER || !state.get(WATERLOGGED)) {
            return Fluids.EMPTY.getDefaultState();
        }
        return Fluids.WATER.getStill(false);
    }

    @Override
    public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return (!(Boolean)state.get(Properties.WATERLOGGED) && fluid == Fluids.WATER) && state.get(HALF) == DoubleBlockHalf.LOWER;
    }
}
