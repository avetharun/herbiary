package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.entity.block.ToolrackBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class ToolrackBlock extends BlockWithEntity {


    public static final DirectionProperty FACING;

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = (Direction)state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, pos, direction);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    public ToolrackBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return BlockWithEntity.createCodec(ToolrackBlock::new);
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
            ToolrackBlockEntity b = (ToolrackBlockEntity) world.getBlockEntity(pos);
            assert b != null;
            ItemStack s = player.getStackInHand(hand);
            if (s.isEmpty() || s.isIn(Herbiary.ALLOWED_ITEMS_ON_TOOL_RACK)) {
                if (s.isEmpty()) {
                    if (!b.HeldItem.isEmpty()) {
                        player.giveItemStack(b.HeldItem);
                        b.HeldItem = ItemStack.EMPTY;
                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 0.5f, 1);
                    }
                } else {
                    if (b.HeldItem == null || b.HeldItem == ItemStack.EMPTY || b.HeldItem.isEmpty()) {
                        b.HeldItem = s;
                        player.setStackInHand(hand, ItemStack.EMPTY);
                        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 0.5f, 1);
                    }

                }
                b.markDirty();
            }
            if (!world.isClient()) {
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
                b.markDirty();
            }
        return ActionResult.CONSUME;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (world.isClient()) {return;}
        ToolrackBlockEntity e = (ToolrackBlockEntity)world.getBlockEntity(pos);
        assert e != null;
        world.spawnEntity(new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), e.HeldItem));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ToolrackBlockEntity(pos, state);
    }
    static {
        FACING = Properties.HORIZONTAL_FACING;

    }
}
