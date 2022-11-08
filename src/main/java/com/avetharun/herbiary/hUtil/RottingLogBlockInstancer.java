package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class RottingLogBlockInstancer extends PillarBlock {
    public List<Block> Growables;
    public RottingLogBlockInstancer(Settings settings, List<Block> growables) {
        super(settings);
        this.Growables = growables;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), Items.STICK.getDefaultStack()));
        if (world.getRandom().nextBetween(0, 3) == 1) {
            world.breakBlock(pos, false);
            BlockPos _pos = pos.up();
            while (world.getBlockState(_pos).isOf(ModItems.ROT_LOG.block)) {
                world.breakBlock(_pos, false);
                world.setBlockState(_pos, Blocks.AIR.getDefaultState());
                world.spawnEntity(new ItemEntity(world, _pos.getX(), _pos.getY(), _pos.getZ(), Items.STICK.getDefaultStack()));
                _pos = _pos.up();
            }
        }
    }
    int ticks_since_last_growth;
    boolean isAir(BlockPos p, ServerWorld w) {
        return w.isAir(p);
    }
    Pair<BlockPos, Direction> findRandomHorizontalAirBlock(BlockPos pos, BlockState _state, ServerWorld world, Random random) {
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
                if (b != null) {
                    world.setBlockState(_grown_pos.getLeft(), b.getDefaultState().with(Properties.HORIZONTAL_FACING, _grown_pos.getRight()));
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }
}
