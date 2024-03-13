package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.hUtil.alib;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractTrapEntity extends Entity {
    public AbstractTrapEntity(EntityType<?> type, World world) {
        this(type, world, BlockPos.ORIGIN);
    }

    private static final TrackedData<NbtCompound> HELD_ENTITY_DATA = DataTracker.registerData(AbstractTrapEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    private static final TrackedData<Boolean> IS_TRIGGERED = DataTracker.registerData(AbstractTrapEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CAN_TRAP = DataTracker.registerData(AbstractTrapEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Optional<UUID>> TRAPPER_UUID = DataTracker.registerData(AbstractTrapEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public abstract BlockPos getTrapperPosition(BlockPos parentTrapPosition);

    @Override
    public boolean canHit() {
        return super.canHit();
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        this.getDataTracker().set(TRAPPER_UUID, Optional.empty());
        // next tick will destroy and spawn an item for this.
        return super.handleAttack(attacker);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    public AbstractTrapEntity(EntityType<?> type, World world, BlockPos pos) {
        super(type, world);
        var trapper = createTrapper();
        trapper.setPosition(Vec3d.ofCenter(getTrapperPosition(pos)));
        world.spawnEntity(trapper);
        this.setPosition(Vec3d.ofCenter(pos));
        this.getDataTracker().set(TRAPPER_UUID, Optional.of(trapper.getUuid()));
    }
    public final TrapperPart getTrapper(){
        var es = this.getWorld().getEntitiesByClass(TrapperPart.class, Box.of(Vec3d.ofCenter(getTrapperPosition(this.getBlockPos())), 5, 5, 5), trapperPart -> {
            return trapperPart.getUuid().compareTo(this.getDataTracker().get(TRAPPER_UUID).orElse(new UUID(0,0))) == 0;
        });
        return es.stream().findFirst().orElse(null);
    }
    public abstract ItemStack getDroppedStack();
    @MustBeInvokedByOverriders
    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() instanceof ServerWorld sW) {
            if (this.getTrapper() == null) {
                var p = this.getPos();
                sW.spawnEntity(new ItemEntity(sW, p.x, p.y, p.z, getDroppedStack()));
                this.discard();
            } else {
                this.getTrapper().setPosition(getTrapperPosition(this.getBlockPos()).toCenterPos());
                if (this.heldEntity != null) {
                    this.heldEntity.setPosition(getTrapper().getBlockPos().toCenterPos());
                }
            }
        }
    }
    public abstract TrapperPart createTrapper();
    private LivingEntity heldEntity = null;
    @MustBeInvokedByOverriders
    public void onTriggered(LivingEntity ent, BlockPos pos, ServerWorld world){
        this.getDataTracker().set(IS_TRIGGERED, true);
        this.getDataTracker().set(CAN_TRAP, false);
        this.getDataTracker().set(HELD_ENTITY_DATA, ent.writeNbt(new NbtCompound()));
        heldEntity = ent;
        ent.setVelocity(Vec3d.ZERO);
    }
    @MustBeInvokedByOverriders
    public void onPreyReleased() {
        this.getDataTracker().set(HELD_ENTITY_DATA, new NbtCompound());
        this.getDataTracker().set(IS_TRIGGERED, false);
    }
    @MustBeInvokedByOverriders
    public void onPrimed(BlockPos pos, ServerWorld world) {
        this.getDataTracker().set(CAN_TRAP, true);
        this.getDataTracker().set(IS_TRIGGERED, false);
    }
    @Override
    @MustBeInvokedByOverriders
    protected void initDataTracker() {
        this.getDataTracker().startTracking(IS_TRIGGERED, false);
        this.getDataTracker().startTracking(CAN_TRAP, true);
        this.getDataTracker().startTracking(HELD_ENTITY_DATA, new NbtCompound());
        this.getDataTracker().startTracking(TRAPPER_UUID, Optional.empty());
    }
    public static abstract class TrapperPart extends alib.EntityPart<AbstractTrapEntity> {
        public TrapperPart(EntityType<?> type, AbstractTrapEntity parent, World world) {
            super(type, parent, world);
        }

        @Override
        protected void initDataTracker() {

        }

        @Override
        protected void writeCustomDataToNbt(NbtCompound nbt) {

        }

        @Override
        protected void readCustomDataFromNbt(NbtCompound nbt) {

        }

        public abstract Box getTrapperHitbox();

        @Override
        public Box getVisibilityBoundingBox() {
            return getTrapperHitbox().expand(5);
        }

        @Override
        protected Box calculateBoundingBox() {
            return getTrapperHitbox();
        }

        @MustBeInvokedByOverriders
        @Override
        public void tick() {
            super.tick();
            if (this.getWorld() instanceof ServerWorld sW) {
                var es = sW.getOtherEntities(this, getTrapperHitbox(), entity -> entity instanceof LivingEntity);
                if (!es.isEmpty()) {
                    this.parent.onTriggered((LivingEntity) es.stream().findFirst().get(), this.getBlockPos(), sW);
                }
            }
        }
    }
}
