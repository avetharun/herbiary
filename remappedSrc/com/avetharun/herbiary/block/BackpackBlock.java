package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Items.SmallBackpackItem;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.block.BackpackBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class BackpackBlock extends BlockWithEntity {
    public BackpackBlock(Settings settings) {
        super(settings);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BackpackBlockEntity(pos,state);
    }
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient() && (world.getBlockEntity(pos, ModItems.BACKPACK_BLOCK_ENTITY)).isPresent()) {
            var bp = world.getBlockEntity(pos, ModItems.BACKPACK_BLOCK_ENTITY).get();
            SmallBackpackItem bpi = (SmallBackpackItem) bp.backpack.getItem();
            world.spawnEntity(new ItemEntity((World)world, pos.getX(), pos.getY(), pos.getZ(), bp.backpack));
        } else if (!world.isClient()) {
            world.spawnEntity(new ItemEntity((World)world, pos.getX(), pos.getY(), pos.getZ(), ModItems.SMALL_BACKPACK.getRight().getDefaultStack()));
        }
        super.onBroken(world, pos, state);
        world.breakBlock(pos, false);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.1875, 0, 0.125, 0.8125, 0.625, 0.6875);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            //noinspection OptionalGetWithoutIsPresent
            var bp = world.getBlockEntity(pos, ModItems.BACKPACK_BLOCK_ENTITY).get();
            SmallBackpackItem bpi = (SmallBackpackItem) bp.backpack.getItem();
            if (player.isSneaking()) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), bp.backpack.copy()));
                world.breakBlock(pos, false);
            } else {
                bp.useExt(world,player,hand);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
