package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.block.FieldMouseBurrowBlockEntity;
import com.avetharun.herbiary.hUtil.alib;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FieldMouseEntity  extends PathAwareEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public BlockPos wantedBurrow, ownedBurrow;
    public boolean isScared;
    public FieldMouseBurrowBlockEntity burrow;
    public void tryClaimBurrow(BlockPos pos, FieldMouseBurrowBlockEntity burrow) {
        if (random.nextBetween(0, 100) > 40) {
            this.burrow = burrow;
            this.ownedBurrow = pos;
        }
    }

    public static EntityType<FieldMouseEntity> getEntityType() {
        return  FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FieldMouseEntity::new).
                dimensions(EntityDimensions.fixed(0.2f,0.2f))
                .spawnableFarFromPlayer()
                .spawnGroup(SpawnGroup.CREATURE)
                .build();
    }

    public FieldMouseEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.lookControl = new LookControl(this);
        this.moveControl = new MoveControl(this);
    }
    static List<EntityType<? extends LivingEntity>> entitiesToRunFrom = new ArrayList<>();
    static{
        entitiesToRunFrom.add(EntityType.CAT);
        entitiesToRunFrom.add(EntityType.WOLF);
        entitiesToRunFrom.add(EntityType.PIG);
        entitiesToRunFrom.add(EntityType.COW);
        entitiesToRunFrom.add(EntityType.HORSE);
        entitiesToRunFrom.add(EntityType.PLAYER);
        entitiesToRunFrom.add(EntityType.FOX);
        entitiesToRunFrom.add(ModEntityTypes.OWL_ENTITY_TYPE);
    }
    private static class RunFromDangerGoal extends EscapeDangerGoal {
        public RunFromDangerGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        protected boolean isInDanger() {
            Vec3d pos = this.mob.getPos();
            boolean hasEntityNearby = !this.mob.method_48926().getOtherEntities(this.mob, Box.of(pos, 15, 4, 15), entity -> {
                boolean bl = false;
                if (entity instanceof PlayerEntity pe) {
                    bl = pe.isCreative();
                }
                return FieldMouseEntity.entitiesToRunFrom.contains(entity.getType()) && !bl;
            }).isEmpty();
            return super.isInDanger() || hasEntityNearby;
        }

        @Override
        public boolean canStart() {
            return isInDanger() || super.canStart();
        }

        @Override
        public void tick() {
            var m = ((FieldMouseEntity)this.mob);
            if (m.burrow != null) {
                this.mob.getNavigation().startMovingAlong(this.mob.getNavigation().findPathTo(m.burrow.getPos(), 10), 1.5f);
            }
            super.tick();
        }
    }
    public int ticksSinceBurrowLeft = 0;
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new RunFromDangerGoal(this, 1.2f));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1f, .5f));
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    @Override
    public JumpControl getJumpControl() {
        return super.getJumpControl();
    }

    private static final TrackedData<Boolean> BURROWING = DataTracker.registerData(FieldMouseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 18)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .5d);
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BURROWING, false);
    }
    public boolean isDigging() { return dataTracker.get(BURROWING);}
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        long[] owned_BurrowL = nbt.getLongArray("ownedBurrow");
        long[] wanted_BurrowL = nbt.getLongArray("wantedBurrow");
        this.ownedBurrow = alib.getBlockPosFromArray(owned_BurrowL);
        this.wantedBurrow = alib.getBlockPosFromArray(wanted_BurrowL);
        this.isScared = nbt.getBoolean("isScared");
    }
    public int ticksSinceBegunSearchingForBurrow = 0;
    private int ticks = 0;
    @Override
    public void tick() {
        if (burrow == null) {}
        super.tick();
        ticks++;
        ticksSinceBurrowLeft++;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.ticksSinceBurrowLeft = 999999;
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    // Digs a burrow to hide in at the current position
    public void digOwnBurrow() {}
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putLongArray("wantedBurrow",alib.getBlockPosAsArray(wantedBurrow));
        nbt.putLongArray("ownedBurrow",alib.getBlockPosAsArray(ownedBurrow));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.field_mouse.run", Animation.LoopType.LOOP));
        } else {
            if (!isDigging()) {
                if (ticks % (20 * 30) == 0) {
                    tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.field_mouse.sniff", Animation.LoopType.PLAY_ONCE).thenPlay("animation.field_mouse.idle"));
                } else {
                    tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.field_mouse.idle", Animation.LoopType.LOOP));
                }
            } else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.field_mouse.burrow", Animation.LoopType.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
