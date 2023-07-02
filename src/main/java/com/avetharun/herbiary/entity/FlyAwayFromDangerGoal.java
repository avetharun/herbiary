package com.avetharun.herbiary.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class FlyAwayFromDangerGoal extends FlyGoal {
    public static final int RANGE_Y = 1;
    protected final PathAwareEntity mob;
    protected final double speed;
    protected Vec3d targetPosition;
    protected boolean active;
    public FlyAwayFromDangerGoal(PathAwareEntity pathAwareEntity, double d) {
        super(pathAwareEntity, d);
        mob = pathAwareEntity;
        speed = d;
    }

    @Nullable
    @Override
    protected Vec3d getWanderTarget() {
        return null;
    }

    @Override
    public boolean canStart() {

        if (!this.isInDanger()) {
            return false;
        } else {
            if (this.mob.isOnFire()) {
                BlockPos blockPos = this.locateClosestWater(this.mob.getWorld(), this.mob);
                if (blockPos != null) {
                    this.targetX = (double)blockPos.getX();
                    this.targetY = (double)blockPos.getY();
                    this.targetZ = (double)blockPos.getZ();
                    return true;
                }
            }

            return this.findTarget();
        }
    }
    protected boolean isInDanger() {
        return this.mob.getAttacker() != null || this.mob.shouldEscapePowderSnow() || this.mob.isOnFire();
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = mob.getTarget();
        assert livingEntity != null;
        targetPosition = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5D), livingEntity.getZ());

    }

    protected boolean findTarget() {
        Vec3d vec3d = NoPenaltyTargeting.find(this.mob, 5, 4);
        if (vec3d == null) {
            return false;
        } else {
            this.targetX = vec3d.x;
            this.targetY = vec3d.y;
            this.targetZ = vec3d.z;
            return true;
        }
    }
    @Nullable
    protected BlockPos locateClosestWater(BlockView world, Entity entity) {
        BlockPos blockPos = entity.getBlockPos();
        return !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty() ? null : BlockPos.findClosest(entity.getBlockPos(), 5, 1, (pos) -> world.getFluidState(pos).isIn(FluidTags.WATER)).orElse(null);
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue();
    }
}
