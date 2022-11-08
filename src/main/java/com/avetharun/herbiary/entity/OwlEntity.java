package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.spawner.Spawner;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.*;

public class OwlEntity extends TameableShoulderEntity implements Angerable, IAnimatable, Flutterer {
    private UUID targetUuid;

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
                .specificSpawnBlocks(Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.SPRUCE_LEAVES,
                        Blocks.OAK_LOG,
                        Blocks.SPRUCE_LOG)
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
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> FLYING = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FLYING_AWAY = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IDLE = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public boolean isInAir() {
        return !this.onGround;
    }
    public static boolean canMobSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        return spawnReason == SpawnReason.SPAWNER || world.getBlockState(blockPos).isIn(BlockTags.LEAVES) || world.getBlockState(blockPos).isIn(BlockTags.OVERWORLD_NATURAL_LOGS);
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

    private static enum OwlMovementType {
        CIRCLE,
        SWOOP,
        PERCH,
        ROOST,
        HUNT;

        private OwlMovementType() {
        }
    }
    OwlMovementType movementType;

    abstract class MovementGoal extends FlyGoal {
        public MovementGoal() {
            super(OwlEntity.this, 1);
            this.setControls(EnumSet.of(Control.MOVE));
        }

        protected boolean isNearTarget() {
            return OwlEntity.this.targetPosition.squaredDistanceTo(OwlEntity.this.getX(), OwlEntity.this.getY(), OwlEntity.this.getZ()) < 4.0;
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

    protected OwlEntity(EntityType<? extends TameableShoulderEntity> entityType, World world) {
        super(entityType, world);
        this.lookControl = new OwlLookControl(this);
        this.moveControl = new FlightMoveControl(this, 15, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));


        this.goalSelector.add(3, new FlyOntoTreeGoal(this, 5.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 32));
        this.goalSelector.add(10, new LookAroundGoal(this));
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
        return target.damage(DamageSource.mob(this), 3.0F);
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
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isProjectile() && source.getSource() instanceof HerbiarySpearItemEntity) {
            this.kill();
            return false;
        }
        return super.damage(source, amount);
    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 4;
        if (this.isFlying() || this.isFlyingAway() || this.isInAir()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.owl.fly", true));
            return PlayState.CONTINUE;
        } else if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.owl.idle", true));
            return PlayState.CONTINUE;
        } else if (this.moveControl.isMoving()) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
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

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }


    private class SwoopMovementGoal extends OwlEntity.MovementGoal {
        SwoopMovementGoal() {
            super();
        }

        public boolean canStart() {
            return OwlEntity.this.getTarget() != null && OwlEntity.this.movementType == OwlMovementType.PERCH;
        }

        public boolean shouldContinue() {
            LivingEntity livingEntity = OwlEntity.this.getTarget();
            if (livingEntity == null) {
                return false;
            } else return livingEntity.isAlive();
        }

        public void start() {
        }

        public void stop() {
            OwlEntity.this.setTarget((LivingEntity) null);
            OwlEntity.this.movementType = OwlMovementType.PERCH;
        }

        public void tick() {
            LivingEntity livingEntity = OwlEntity.this.getTarget();
            if (livingEntity != null) {
                OwlEntity.this.targetPosition = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5) + 5, livingEntity.getZ());
                if (OwlEntity.this.getBoundingBox().expand(0.20000000298023224).intersects(livingEntity.getBoundingBox())) {
                    OwlEntity.this.tryAttack(livingEntity);
                }
            }
        }
    }
    private static class FlyOntoTreeGoal extends FlyGoal {
        public FlyOntoTreeGoal(PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }
        @Override
        public boolean canStart() {
            return this.mob.world.isDay() || !this.mob.world.getBlockState(this.mob.getBlockPos().down()).isIn(BlockTags.LEAVES);
        }

        @Nullable
        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (this.mob.isTouchingWater()) {
                vec3d = FuzzyTargeting.find(this.mob, 15, 64);
            }

            if (this.mob.getRandom().nextFloat() >= 0.4f) {
                vec3d = this.locateTree();
            }
            if (vec3d != null) {
                this.mob.getWorld().addParticle(ParticleTypes.FLAME, vec3d.x, vec3d.y, vec3d.z, 0, 0, 0);
            }
            return vec3d;

        }

        @Nullable
        private Vec3d locateTree() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();
            Iterable<BlockPos> iterable = BlockPos.iterate(blockPos.add(-16,-16,-16), blockPos.add(16,16,16));
            Iterator<BlockPos> var5 = iterable.iterator();

            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!var5.hasNext()) {
                        return null;
                    }

                    blockPos2 = var5.next();
                } while(blockPos.equals(blockPos2));
                BlockState blockState = this.mob.world.getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                bl = blockState.isIn(BlockTags.LEAVES) || blockState.isIn(BlockTags.LOGS);
            } while(!bl || !this.mob.world.isAir(blockPos2) || !this.mob.world.isAir(mutable.set(blockPos2, Direction.UP)));
            return Vec3d.of(blockPos2.subtract(blockPos.subtract(blockPos2).multiply(4)));
        }
    }
}
