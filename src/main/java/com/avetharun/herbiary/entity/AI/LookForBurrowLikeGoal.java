package com.avetharun.herbiary.entity.AI;

import com.avetharun.herbiary.block.NestBlockEntity;
import com.avetharun.herbiary.entity.block.FieldMouseBurrowBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LookForBurrowLikeGoal extends MoveToTargetPosGoal {
    PointOfInterestType poiType = null;
    Predicate<BlockEntity> consumerBlockEntity = null;
    Function<BlockPos, BlockPos> posHolder = null;
    public LookForBurrowLikeGoal(PathAwareEntity mob, double speed, int range, PointOfInterestType poiType, Function<BlockPos, BlockPos> posHolder, Predicate<BlockEntity> consumerPredicate) {
        super(mob, speed, range);
        this.poiType = poiType;
        this.consumerBlockEntity = consumerPredicate;
    }
    public LookForBurrowLikeGoal(PathAwareEntity mob, double speed, int range, PointOfInterestType poiType, Function<BlockPos, BlockPos> posHolder) {
        this(mob,speed,range,poiType,posHolder, null);

    }
    BlockPos wantedPos = null;

    public boolean doesHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.mob.getWorld().getBlockEntity(pos);
        if (blockEntity instanceof NestBlockEntity nE) {
            return nE.parent_bird == null;
        } else if (blockEntity instanceof FieldMouseBurrowBlockEntity bE){
            return bE.hasOwner();
        } else if (consumerBlockEntity != null) {
            return consumerBlockEntity.test(blockEntity);
        }
        else {
            return false;
        }
    }

    private List<BlockPos> getNearbyFreeBurrows() {
        PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) this.mob.getWorld()).getPointOfInterestStorage();
        var stream = pointOfInterestStorage.getInSquare(pointOfInterestTypeRegistryEntry -> true, this.mob.getBlockPos(), 10, PointOfInterestStorage.OccupationStatus.HAS_SPACE);
        return stream.map(PointOfInterest::getPos).filter(this::doesHaveSpace).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.getSquaredDistance(this.mob.getBlockPos()))).collect(Collectors.toList());
    }
    public List<BlockPos> nearbyBurrows = null;
    public BlockPos wantedBurrow = null;
    // If true, allow goal to search for a new burrow (if wantedBurrow is taken!)
    public boolean allowRelook = true;
    int ticks = 0;
    @Override
    public void tick() {
        if (nearbyBurrows == null || ticks % 10 == 0 && !doesHaveSpace(nearbyBurrows.get(0))) {
            nearbyBurrows = getNearbyFreeBurrows();
        }
        super.tick();
        ticks ++;
    }
    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return pos == wantedPos;
    }
}
