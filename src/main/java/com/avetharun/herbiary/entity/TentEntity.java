package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.block.NestBlockEntity;
import com.avetharun.herbiary.client.entity.TentEntityRenderer;
import com.avetharun.herbiary.hUtil.alib;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.entity.BatEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class TentEntity extends Entity implements GeoEntity, Attackable {
    public static class TentStorageEntity extends Entity {
        TentEntity mainEntity;
        public Vec3d offset;
        public ItemStack stack = ItemStack.EMPTY;
        public TentStorageEntity(EntityType<?> type, World world) {
            super(type, world);
        }
        public TentStorageEntity(EntityType<?> type, World world, TentEntity baseEntity) {
            super(type, world);
            this.mainEntity = baseEntity;
            baseEntity.storage = this;
        }
        public void subTick() {
            // Update the entity's position to follow the main entity's position at the offset
            Vec3d pos = this.mainEntity.getPos().add(this.offset);
            this.updatePosition(pos.x, pos.y, pos.z);
        }

        @Override
        protected void initDataTracker() {
        }

        @Override
        protected void readCustomDataFromNbt(NbtCompound nbt) {

        }

        @Override
        protected void writeCustomDataToNbt(NbtCompound nbt) {

        }

        @Override
        public boolean shouldRender(double distance) {
            return true;
        }

        @Override
        public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
            return true;
        }
    }
    public TentEntity(EntityType<? extends Entity> type, World world) {
        super( type, world);
        this.getWorld().spawnEntity(new TentStorageEntity(ModEntityTypes.TENT_STORAGE_ENTITY_ENTITY_TYPE, world, this));
    }

    public TentEntity(World world, double x, double y, double z, Direction direction) {
        this(ModEntityTypes.TENT_ENTITY_TYPE, world);
        this.setPosition(x, y, z);
        this.setFacing(direction);
        this.getWorld().spawnEntity(new TentStorageEntity(ModEntityTypes.TENT_STORAGE_ENTITY_ENTITY_TYPE, world, this));
    }
    private static final TrackedData<ItemStack> CURRENT_STORAGE = DataTracker.registerData(TentEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Boolean> HAS_BED = DataTracker.registerData(TentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_TABLE = DataTracker.registerData(TentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Direction> DIRECTION = DataTracker.registerData(TentEntity.class, TrackedDataHandlerRegistry.FACING);
    public TentStorageEntity storage;
    @Override
    public void remove(RemovalReason reason) {
        storage.remove(reason);
        super.remove(reason);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(HAS_BED, false);
        this.dataTracker.startTracking(HAS_TABLE, false);
        this.dataTracker.startTracking(CURRENT_STORAGE, ItemStack.EMPTY);
        this.dataTracker.startTracking(DIRECTION, Direction.NORTH);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
    }
    Vec3d storageBoxHitboxOffset = new Vec3d(0,0,0);
    @Override
    public void tick() {
        super.tick();
        storage.offset = storageBoxHitboxOffset.offset(this.dataTracker.get(DIRECTION), 0.5f).add(0,0.25f,0);
        storage.subTick();
    }

    public void kill() {
        this.remove(RemovalReason.KILLED);
        this.emitGameEvent(GameEvent.ENTITY_DIE);
    }
    Box DIMENSIONS =new Box(-0.5, 0, -0.5, 0.5, 1.8, 0.5);

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (player instanceof ServerPlayerEntity entity) {
            if (this.dataTracker.get(HAS_BED)) {
                if (entity.isSneaking()) {
                    BlockPos itemPosToSpawn = this.getBlockPos().up();
                    this.getWorld().spawnEntity(new ItemEntity(getWorld(), itemPosToSpawn.getX(), itemPosToSpawn.getY(), itemPosToSpawn.getZ(), ModItems.SLEEPING_BAG.getDefaultStack()));
                    this.dataTracker.set(HAS_BED, false);
                    return ActionResult.CONSUME;
                } else if (entity.getWorld().isNight()){
                    BlockPos pos = this.getBlockPos();
                    entity.sleep(pos);
                    return ActionResult.PASS;
                }
            } else if (entity.getStackInHand(hand).isOf(ModItems.SLEEPING_BAG) && !this.dataTracker.get(HAS_BED)) {
                this.dataTracker.set(HAS_BED, true);
                entity.getStackInHand(hand).decrement(1);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }


    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.PLAYER_ATTACK)) {
            BlockPos itemPosToSpawn = this.getBlockPos().up();
            if (this.hasSleepingBag()) {
                this.getWorld().spawnEntity(new ItemEntity(getWorld(), itemPosToSpawn.getX(), itemPosToSpawn.getY() - 0.5f, itemPosToSpawn.getZ(), ModItems.SLEEPING_BAG.getDefaultStack()));
            }
            this.getWorld().spawnEntity(new ItemEntity(getWorld(), itemPosToSpawn.getX(), itemPosToSpawn.getY(), itemPosToSpawn.getZ(), ModItems.TENT_SPAWNER.getDefaultStack()));
            this.kill();
        }
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return new ArrayList<>(0);
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public boolean canHit() {
        return true;
    }
    private PlayState predicate(AnimationState<TentEntity> event) {
        return PlayState.STOP;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));

    }
    public void setFacing(Direction direction) {
        this.dataTracker.set(DIRECTION, direction);
    }
    public Direction getFacing() {return this.dataTracker.get(DIRECTION);}
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    public boolean hasSleepingBag() {
        return this.dataTracker.get(HAS_BED);
    }
    public boolean hasStand() {
        return this.dataTracker.get(HAS_TABLE);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(HAS_BED, nbt.getBoolean("has_bed"));
        this.dataTracker.set(DIRECTION, Direction.byName(nbt.getString("direction")));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("has_bed", this.dataTracker.get(HAS_BED));
        nbt.putString("direction", this.dataTracker.get(DIRECTION).getName());
    }

    @Nullable
    @Override
    public LivingEntity getLastAttacker() {
        return null;
    }
}
