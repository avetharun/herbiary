package com.avetharun.herbiary.block;

import com.avetharun.herbiary.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LogBlock extends PillarBlock {
    public static class SapProducingLog extends LogBlock{
        public SapProducingLog(Settings settings) {
            super(settings);
        }

        @Override
        public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            super.randomTick(state, world, pos, random);
            if (random.nextBetween(0,100) > 95) {
                var d = Direction.random(random);
                var pos1 = pos.offset(d);
                var state1 = world.getBlockState(pos1);
                if (state1.isAir()) {
                    world.setBlockState(pos1, ModItems.PINE_SAP.getLeft().getDefaultState().with(Properties.FACING, d));
                }
            }
        }
    }
    public LogBlock(Settings settings) {
        super(settings);
    }
    Block getBlockForReplacement(Block existing) {

        return switch (existing.getRegistryEntry().getKey().get().getValue().toString()) {
            case "minecraft:oak_log" -> Blocks.STRIPPED_OAK_LOG;
            case "minecraft:spruce_log" -> Blocks.STRIPPED_SPRUCE_LOG;
            case "minecraft:acacia_log" -> Blocks.STRIPPED_ACACIA_LOG;
            case "minecraft:birch_log" -> Blocks.STRIPPED_BIRCH_LOG;
            case "minecraft:jungle_log" -> Blocks.STRIPPED_JUNGLE_LOG;
            case "minecraft:dark_oak_log" -> Blocks.STRIPPED_DARK_OAK_LOG;
            case "minecraft:cherry_log" -> Blocks.STRIPPED_CHERRY_LOG;
            case "minecraft:mangrove_log" -> Blocks.STRIPPED_MANGROVE_LOG;


            case "minecraft:oak_wood" -> Blocks.STRIPPED_OAK_WOOD;
            case "minecraft:spruce_wood" -> Blocks.STRIPPED_SPRUCE_WOOD;
            case "minecraft:acacia_wood" -> Blocks.STRIPPED_ACACIA_WOOD;
            case "minecraft:birch_wood" -> Blocks.STRIPPED_BIRCH_WOOD;
            case "minecraft:jungle_wood" -> Blocks.STRIPPED_JUNGLE_WOOD;
            case "minecraft:dark_oak_wood" -> Blocks.STRIPPED_DARK_OAK_WOOD;
            case "minecraft:cherry_wood" -> Blocks.STRIPPED_CHERRY_WOOD;
            case "minecraft:mangrove_wood" -> Blocks.STRIPPED_MANGROVE_WOOD;

            default -> throw new IllegalStateException("Unexpected value: " + existing.getRegistryEntry().getKey().get().getValue());
        };
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (world instanceof ServerWorld sw) {
//            sw.setBlockState(pos, Blocks.STONE.getDefaultState());
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
//        System.out.println("abc");
        if (!player.hasStackEquipped(EquipmentSlot.MAINHAND) || !player.getStackInHand(Hand.MAIN_HAND).isIn(ItemTags.AXES) && !player.isCreative() && !world.isClient) {
            this.spawnBreakParticles(world, player, pos, state);
            var s = getBlockForReplacement(state.getBlock()).getDefaultState().with(Properties.AXIS, state.get(Properties.AXIS));
            world.setBlockState(pos, s, 35);
            super.afterBreak(world, player, pos, state, blockEntity, tool);
            return;
        }
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
        super.afterBreak(world, player, pos, state, blockEntity, tool);
    }
}
