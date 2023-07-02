package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import com.avetharun.herbiary.block.NestBlockEntity;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.hUtil.alib;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class OwlEntity extends TameableShoulderEntity implements Angerable, GeoEntity, Flutterer {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public UUID targetUuid;
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public static EntityType<OwlEntity> getEntityType() {
        return  FabricEntityTypeBuilder.create(SpawnGroup.MISC, OwlEntity::new).
                dimensions(EntityDimensions.fixed(0.35f,0.9f))
                .spawnableFarFromPlayer()
                .spawnGroup(SpawnGroup.MONSTER)
                .specificSpawnBlocks(
                        ModItems.BIRD_NEST_BLOCK.getLeft()
                )
                .build();
    }
    public Goal action;

    public static DefaultAttributeContainer.Builder createEntityAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 18)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0d)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 5)
                ;
    }

    public void setAction(Goal action) {
        this.removeCurrentActionGoal();
        this.action = action;
        this.goalSelector.add(4, action);
    }

    public void removeCurrentActionGoal() {
        this.goalSelector.remove(action);
        this.action = null;
    }
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FLYING_AWAY = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IDLE = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }
    public static boolean canMobSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        return spawnReason == SpawnReason.SPAWNER || world.getBlockState(blockPos).isIn(BlockTags.LEAVES) || world.getBlockState(blockPos).isIn(BlockTags.OVERWORLD_NATURAL_LOGS);
    }

    @Override
    public EntityView method_48926() {
        return null;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return super.getOwner();
    }

    class OwlLookControl extends LookControl {
        public OwlLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            if (!OwlEntity.this.isFlying()) {
                super.tick();
            }
        }
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(FLYING, false);
        this.dataTracker.startTracking(FLYING_AWAY, false);
        this.dataTracker.startTracking(SITTING, true);
        this.dataTracker.startTracking(IDLE, true);
        this.dataTracker.startTracking(ANGER_TIME, 10);
    }

    Vec3d targetPosition;
    @Override
    public int getAngerTime() {
        return this.dataTracker.get(ANGER_TIME);
    }

    @Override
    public void setAngerTime(int angerTime) {

    }

    @Override
    protected void mobTick() {

    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false; // ignore fall damage!
    }
    public BlockPos nest;

    protected OwlEntity(EntityType<? extends TameableShoulderEntity> entityType, World world) {
        super(entityType, world);
        this.lookControl = new OwlLookControl(this);
        this.moveControl = new FlightMoveControl(this, 15, false);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(false);
        }
        if (this.nest == null || this.nest == this.getBlockPos()) { this.nest = BlockPos.ORIGIN; }
        // 0-20 ticks (or 0 - 1 second)
        return super.initialize(world, difficulty, spawnReason, (EntityData)entityData, entityNbt);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putLongArray("Nest", alib.getBlockPosAsArray(this.nest));
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.nest = alib.getBlockPosFromArray(nbt.getLongArray("Nest"));
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new OwlEntityAIGoal(this, 1));
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return super.canTarget(target);
    }

    @Override
    public boolean shouldAngerAt(LivingEntity entity) {
        return TargettableEntities.contains(entity.getType());
    }

    public boolean tryAttack(Entity target) {
        return target.damage(this.getDamageSources().thrown(target, this), 1f);
    }

    private static final Set<EntityType> TargettableEntities = Set.of(
            EntityType.CAT,
            EntityType.FROG,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.CHICKEN,
            EntityType.TROPICAL_FISH
    );

    @Override
    public boolean canTarget(EntityType<?> type) {
        return TargettableEntities.contains(type);
    }
    @Override
    public UUID getAngryAt() {
        return this.targetUuid;
    }

    @Override
    public void setAngryAt(@Nullable UUID uuid) {
        this.targetUuid = uuid;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(this.getRandom().nextBetween(40,120));
    }

    public boolean isFlying() {
        return this.dataTracker.get(FLYING);
    }
    public boolean isFlyingAway() {
        return this.dataTracker.get(FLYING_AWAY);
    }
    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void setSitting(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
    }
    public void setFlying(boolean flying) {
        this.dataTracker.set(FLYING, flying);
    }
    public void setFlyingAway(boolean state) {
        this.dataTracker.set(FLYING_AWAY, state);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof HerbiarySpearItemEntity) {
            this.kill();
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    protected void pushAway(Entity entity) {
        if (this.isSitting()) {return;}
    }
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public boolean isAnotherOwlOnNest() {
        return this.getWorld().getEntitiesByClass(OwlEntity.class, Box.of(Vec3d.ofCenter(this.nest), 1, 18, 1), entity -> entity.nest != null).size() > 0;
    }

    public boolean isAnotherOwlNearNestPos(Vec3d pos, int horizDist) {
        return this.getWorld().getEntitiesByClass(OwlEntity.class, Box.of(pos, horizDist, 18, horizDist), entity -> entity.nest != null).size() > 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    RawAnimation anim = RawAnimation.begin();
    private PlayState predicate(AnimationState<OwlEntity> event) {
        if (this.isFlying() || this.isFlyingAway() || this.isInAir()) {
            event.getController().setAnimation(anim.thenLoop("animation.owl.fly"));
            return PlayState.CONTINUE;
        } else if (this.isSitting()) {
            event.getController().setAnimation(anim.thenLoop("animation.owl.idle"));
            return PlayState.CONTINUE;
        } else if (this.moveControl.isMoving()) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    int blinkStep = 32;
    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getWorld().isClient()) {
        }
    }

    private static class OwlEntityAIGoal extends Goal {
        PathAwareEntity mob;
        public OwlEntityAIGoal(PathAwareEntity pathAwareEntity, double d) {
            super();
            this.mob = pathAwareEntity;
        }

        @Nullable
        private BlockPos locateNestPos() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();
            Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.mob.getX() - 3.0), MathHelper.floor(this.mob.getY() - 6.0), MathHelper.floor(this.mob.getZ() - 3.0), MathHelper.floor(this.mob.getX() + 3.0), MathHelper.floor(this.mob.getY() + 6.0), MathHelper.floor(this.mob.getZ() + 3.0));
            Iterator<BlockPos> var5 = iterable.iterator();

            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!var5.hasNext()) {
                        return null;
                    }

                    blockPos2 = (BlockPos)var5.next();
                } while(blockPos.equals(blockPos2));

                BlockState blockState = this.mob.getWorld().getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                BlockState _s = this.mob.getWorld().getBlockState(blockPos2);
                if (_s.isOf(ModItems.BIRD_NEST_BLOCK.getLeft())) {
                    NestBlockEntity _e = ((NestBlockEntity) this.mob.getWorld().getBlockEntity(blockPos2));
                    boolean blockHasParentBird = _e != null && _e.parent_bird != null;
                    bl = blockState.isIn(Herbiary.BIRD_NEST_PLACEABLE) || _s.isOf(ModItems.BIRD_NEST_BLOCK.getLeft()) && !blockHasParentBird;
                }
                bl=true;
            } while(!bl || !this.mob.getWorld().isAir(blockPos2) || !this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP)));

            return blockPos2;
        }

        @Nullable
        private BlockPos locateHarvestables() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();
            Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.mob.getX() - 3.0), MathHelper.floor(this.mob.getY() - 6.0), MathHelper.floor(this.mob.getZ() - 3.0), MathHelper.floor(this.mob.getX() + 3.0), MathHelper.floor(this.mob.getY() + 6.0), MathHelper.floor(this.mob.getZ() + 3.0));
            Iterator<BlockPos> var5 = iterable.iterator();

            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!var5.hasNext()) {
                        return null;
                    }

                    blockPos2 = (BlockPos)var5.next();
                } while(blockPos.equals(blockPos2));

                BlockState blockState = this.mob.getWorld().getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                bl = blockState.isIn(Herbiary.NEST_COLLECTABLES);
            } while(!bl || !this.mob.getWorld().isAir(blockPos2) || !this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP)));

            return blockPos2.add(0,1,0);
        }
        @Override
        public boolean canStart() {
            return true;
        }

        @Override
        public boolean canStop() {
            return false;
        }
        int ticks_since_last_nav = 0;
        boolean isFirstReNavTick = true;
        boolean isFindingNestSpot = false;
        BlockPos collectable_position;
        boolean wants_collectable = true;
        BlockPos wanted_nest_position;
        public boolean idle = false;
        Vec3d goal_pos = Vec3d.ZERO;

        @Override
        public void tick() {
            OwlEntity e = ((OwlEntity)this.mob);
            if (e.nest == null || e.nest.equals(BlockPos.ORIGIN)) {
                // Need to build and find a nest. Wander around for a couple seconds, or until we find a tree, then make a nest.
                if (--ticks_since_last_nav <= 0) {
                    // try to find an empty nest first.
                    BlockPos np = locateNestPos();
                    if (np != null) {
                        e.nest = np;
                    }
                    if (e.nest == null || e.nest.equals(BlockPos.ORIGIN) && !isFindingNestSpot) {
                        collectable_position = this.locateHarvestables();
                        wants_collectable = true;
                        ticks_since_last_nav = 6;
                        goal_pos = Vec3d.ofCenter(e.getBlockPos());
                    }
                    if (wants_collectable && collectable_position != null) {
                        collectable_position = this.locateHarvestables();
                        assert collectable_position != null;
                        goal_pos = Vec3d.of(collectable_position).add(
                                e.random.nextBetweenExclusive(0, 10) * (e.random.nextBoolean() ? -1 : 1),
                                1,
                                e.random.nextBetweenExclusive(0, 10) * (e.random.nextBoolean() ? -1 : 1)
                        );
                        ticks_since_last_nav = 4;
                    }
                    if (e.getWorld().getBlockState(e.getBlockPos().down()).isIn(Herbiary.NEST_COLLECTABLES) && wants_collectable) {
                        isFindingNestSpot = true;
                        wants_collectable = false;
                        ticks_since_last_nav = 30;
                    }
                    if (this.isFindingNestSpot && !wants_collectable){
                        // Random wander if no collectable is found or is searching for a nest
                        BlockPos v = this.locateNestPos();
                        if (v == null) {
                            // No suitable nest position found, search around.
                            goal_pos = Vec3d.of(collectable_position).add(
                                    e.random.nextBetweenExclusive(5, 16) * (e.random.nextBoolean() ? -1 : 1),
                                    1,
                                    e.random.nextBetweenExclusive(5, 16) * (e.random.nextBoolean() ? -1 : 1)
                            );
                        } else {
                            goal_pos = Vec3d.of(v).add(
                                    e.random.nextBetweenExclusive(1, 4) * (e.random.nextBoolean() ? -1 : 1),
                                    1,
                                    e.random.nextBetweenExclusive(1, 4) * (e.random.nextBoolean() ? -1 : 1)
                            );
                        }
                        ticks_since_last_nav = 15;
                    }
                    if (this.isFindingNestSpot && e.getWorld().getBlockState(e.getBlockPos().down()).isIn(Herbiary.BIRD_NEST_PLACEABLE)) {
                        e.nest = e.getBlockPos();
                        // set nest block and set owner
                        e.getWorld().setBlockState(e.getBlockPos(), ModItems.BIRD_NEST_BLOCK.getLeft().getDefaultState());
                        NestBlockEntity nestBlockEntity = ((NestBlockEntity)e.getWorld().getBlockEntity(e.getBlockPos()));
                        if (nestBlockEntity == null) {
                            return;
                        }
                        nestBlockEntity.parent_bird = e.getUuid();
                    }
                    isFirstReNavTick = false;
                }
            }
            if (e.nest != null){
                BlockState NEST_STATE = e.getWorld().getBlockState(e.nest);
                if (e.getWorld().isNight() || e.getWorld().isRaining()) {
                    Vec3d _d = Vec3d.ofCenter(e.nest);
                    BlockState d = this.mob.getWorld().getBlockState(this.mob.getBlockPos().down());
                    if (!this.mob.getBlockPos().isWithinDistance(_d, 2) || d.isAir() || !d.isIn(Herbiary.BIRD_NEST_PLACEABLE)) {
                        goal_pos = _d.add(0,1,0);
                    }
                } else {
                    goal_pos.add(e.random.nextBetweenExclusive(10, 50) * (e.random.nextBoolean() ? -1 : 1), 0, e.random.nextBetweenExclusive(10, 50) * (e.random.nextBoolean() ? -1 : 1));

                }
            }
            TICK:
            if (idle) {
                super.tick();
            } else {
                this.mob.getNavigation().startMovingTo(goal_pos.x, goal_pos.y, goal_pos.z, .3);
            }
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }
    }

}
