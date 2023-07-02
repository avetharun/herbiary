package com.avetharun.herbiary.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.render.block.entity.LecternBlockEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BookMat extends LecternBlock {
    public BookMat(Settings settings) {
        super(settings);
    }
    private static void putBook(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LecternBlockEntity lecternBlockEntity) {
            lecternBlockEntity.setBook(stack.split(1));
            setHasBook(null, world, pos, state, true);
            world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
    }
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LecternBlockEntity(pos, state);
    }
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
