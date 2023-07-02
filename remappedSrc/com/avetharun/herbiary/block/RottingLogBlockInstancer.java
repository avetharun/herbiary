package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.block.TreeMountedBlock;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RottingLogBlockInstancer extends PillarBlock {
    public List<Block> Growables;
    public RottingLogBlockInstancer(Settings settings, List<Block> growables) {
        super(settings);
        this.Growables = growables;
    }

    PlayerEntity breaker;
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (player.isCreative()) {
            breaker = player;
            super.onBreak(world, pos, state, player);
            return;
        }
        int drop_count = world.getRandom().nextInt(3);
        // always drop at LEAST 2 sticks. Chance of dropping 3 more.
        for (int i = 0; i < drop_count + 2; i++) {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), Items.STICK.getDefaultStack()));
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> out = new ArrayList<>();
        int drop_count = builder.getWorld().random.nextInt(3);
        // always drop at LEAST 2 sticks. Chance of dropping 3 more.
        for (int i = 0; i < drop_count + 2; i++) {
            out.add(Items.STICK.getDefaultStack());
        }
        return out;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (breaker != null && breaker.isCreative()) {
            return; // don't break unless not creative. This is to allow building with this, while not having it break in a chain.
        }
        if (world.getBlockState(sourcePos).isAir() && world.getBlockState(pos.down()).isAir()) {
            world.breakBlock(pos, false);
            int drop_count = world.getRandom().nextInt(3);
            // always drop at LEAST 2 sticks. Chance of dropping 3 more.
            for (int i = 0; i < drop_count + 2; i++) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), Items.STICK.getDefaultStack()));
            }
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    public int ticks_since_last_growth;
    boolean isAir(BlockPos p, ServerWorld w) {
        return w.isAir(p);
    }
    public Pair<BlockPos, Direction> findRandomHorizontalAirBlock(BlockPos pos, BlockState _state, ServerWorld world, Random random) {
        List<Direction> list = new LinkedList<Direction>();
        if (world.isAir(pos.east())) list.add(Direction.EAST);
        if (world.isAir(pos.west())) list.add(Direction.WEST);
        if (world.isAir(pos.north())) list.add(Direction.NORTH);
        if (world.isAir(pos.south())) list.add(Direction.SOUTH);
        if (list.size() == 0) return new Pair<>(pos, Direction.UP);
        int rnd = random.nextInt(list.size());
        return new Pair<> (pos.offset(list.get(rnd)), list.get(rnd));
    }
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (--ticks_since_last_growth <= 0 && Growables.size() > 0) {
            ticks_since_last_growth = 6;
            if (random.nextBetweenExclusive(0,4) == 1) {return;}
            Pair<BlockPos, Direction> _grown_pos = findRandomHorizontalAirBlock(pos, state, world, random);
            if (_grown_pos.getLeft() != pos) {
                Block b = Growables.get(random.nextBetween(0, Growables.size() - 1));
                if (b instanceof TreeMountedBlock summerfall) {
                    if (summerfall.canGrowInSeason(Herbiary.getCurrentSeasonWorld(world).ordinal())) {
                        world.setBlockState(_grown_pos.getLeft(), b.getDefaultState().with(Properties.HORIZONTAL_FACING, _grown_pos.getRight()));
                    }
                } else {
                    if (b != null) {
                        world.setBlockState(_grown_pos.getLeft(), b.getDefaultState().with(Properties.HORIZONTAL_FACING, _grown_pos.getRight()));
                    }
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }
}
