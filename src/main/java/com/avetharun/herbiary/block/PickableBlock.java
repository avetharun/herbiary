package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.function.BiConsumer;

public class PickableBlock extends PlantBlock implements Fertilizable {

    public static final int MAX_AGE = 3;
    public static final IntProperty AGE;
    boolean isSmallHerb = false;
    public BiConsumer<BlockPos, World> OnPicked;
    public PickableBlock(Settings settings, BiConsumer<BlockPos, World> onPicked) {
        super(settings);

        this.OnPicked = onPicked;
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }
    public PickableBlock(Settings settings, BiConsumer<BlockPos, World> onPicked, boolean isSmallHerb) {
        super(settings);
        this.isSmallHerb = isSmallHerb;
        this.OnPicked = onPicked;
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }

    public PickableBlock(Settings settings) {
        super(settings);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == 3) {
            world.setBlockState(pos, state.with(AGE, 0));
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 0.4f, 1.0f);
            OnPicked.accept(pos, world);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    static {
        AGE = Properties.AGE_3;
    }
    int ticksSinceLastBloom = 30;
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        --ticksSinceLastBloom;
        if (ticksSinceLastBloom < 0 && random.nextInt(100) >= 75 && state.get(AGE) < 3) {
            world.setBlockState(pos, state.with(AGE, state.get(AGE)+1));
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return PlantBlock.createCodec(PickableBlock::new);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(Herbiary.FARMLAND) || super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }
    protected static final VoxelShape SHAPE_DEFAULT = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    protected static final VoxelShape SHAPE_HERB = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.isSmallHerb? SHAPE_HERB : SHAPE_DEFAULT;
    }


    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(AGE) != 3;
    }


    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(AGE, state.get(AGE)+1));
    }
}
