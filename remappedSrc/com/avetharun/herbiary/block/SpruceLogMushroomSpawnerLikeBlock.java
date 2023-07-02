package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class SpruceLogMushroomSpawnerLikeBlock extends RottingLogBlockInstancer {
    public SpruceLogMushroomSpawnerLikeBlock(Settings settings) {
        super(settings, null);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (Growables == null) {Growables = ModItems.SpruceLogGrowables;}
        // if still null, then don't tick.
        if (Growables == null) {return;}
        if (--ticks_since_last_growth <= 0 && Growables.size() > 0) {
            ticks_since_last_growth = 8;
            if (random.nextBetweenExclusive(0,4) == 1) {return;}
            Pair<BlockPos, Direction> _grown_pos = findRandomHorizontalAirBlock(pos, state, world, random);
            if (_grown_pos.getLeft() != pos) {
                Block b = Growables.get(random.nextBetween(1, Growables.size()) - 1);
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
