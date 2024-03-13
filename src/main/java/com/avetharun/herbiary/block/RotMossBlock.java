package com.avetharun.herbiary.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class RotMossBlock extends MultifaceGrowthBlock {

    public RotMossBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.gC = new GROWER_CHECKER_NULL(this);
        this.gR = new LichenGrower(this.gC);
    }

    @Override
    protected MapCodec<? extends MultifaceGrowthBlock> getCodec() {
        return MultifaceGrowthBlock.createCodec(RotMossBlock::new);
    }

    private static class GROWER_CHECKER_NULL implements  LichenGrower.GrowChecker {
        public GROWER_CHECKER_NULL(MultifaceGrowthBlock growthBlock) {
            this.parent = growthBlock;
        }
        MultifaceGrowthBlock parent;
        @Nullable
        @Override
        public BlockState getStateWithDirection(BlockState state, BlockView world, BlockPos pos, Direction direction) {
            return parent.withDirection(state,world,pos,direction);
        }

        @Override
        public boolean canGrow(BlockView world, BlockPos pos, LichenGrower.GrowPos growPos) {
            return false;
        }
    }
    LichenGrower.GrowChecker gC;
    LichenGrower gR;
    @Override
    public LichenGrower getGrower() {
        return gR;
    }

}