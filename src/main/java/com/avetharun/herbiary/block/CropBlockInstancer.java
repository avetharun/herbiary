package com.avetharun.herbiary.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;

public class CropBlockInstancer extends CropBlock {
    int maxGrowth, minSeeds;
    private final ArrayList<VoxelShape> AGE_TO_SHAPE = new ArrayList<VoxelShape>();
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int o = (Integer)state.get(this.getAgeProperty());
        if (o < AGE_TO_SHAPE.size()) {
            return AGE_TO_SHAPE.get(o);
        }
        return AGE_TO_SHAPE.get(AGE_TO_SHAPE.size() - 1);
    }
    public CropBlockInstancer(AbstractBlock.Settings settings, boolean transparent,
                              int stages, double size_start, double size_end,
        int min_seeds_on_break
    ) {
        super(settings);
        minSeeds = min_seeds_on_break;
        maxGrowth = stages;
        if (transparent) {
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), this);
        }
        double additive = stages / ( size_end - size_start);
        double amt = size_start + additive;
        AGE_TO_SHAPE.ensureCapacity(stages);
        for (int i = 0; i < stages; i++) {
            AGE_TO_SHAPE.add(Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, amt, 16.00));
            amt += additive;
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(this.getAgeProperty()) < maxGrowth) {
            super.grow(world, random, pos, state);
        }
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        if (state.get(this.getAgeProperty()) < maxGrowth) {
            return super.canGrow(world, random, pos, state);
        }
        return false;
    }

    public ItemConvertible getSeedsItem() {
        return BlockItem.fromBlock(this);
    }
}