package com.avetharun.herbiary.entity.block;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.FieldMouseEntity;
import com.avetharun.herbiary.entity.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class FieldMouseBurrowBlockEntity extends BlockEntity {
    public UUID ownerMouse;
    public int ticksSincePing = 0;
    public static void tick(World world1, BlockPos pos, BlockState state1, FieldMouseBurrowBlockEntity be) {
        if (be.ticksSincePing > 30 * 20) {
            var mice = world1.getEntitiesByClass(FieldMouseEntity.class, Box.of(pos.toCenterPos(), 30, 30, 30), fieldMouseEntity -> fieldMouseEntity.burrow == null && fieldMouseEntity.ticksSinceBurrowLeft > 20 * 120);
            int len = mice.size();
            if (len > 0) {
                FieldMouseEntity m = mice.get(world1.getRandom().nextBetween(0, len - 1));
                m.tryClaimBurrow(pos, be);
            }
        }
        be.ticksSincePing++;
    }

    public boolean hasOwner() {
        if (ownerMouse == null) { return false;}
        assert this.world != null;
        var mice = this.world.getEntitiesByClass(FieldMouseEntity.class, Box.of(this.pos.toCenterPos(), 30, 15, 30), fieldMouseEntity -> ownerMouse.equals(fieldMouseEntity.getUuid()));
        return mice.size() != 0;
    }
    private final NbtCompound heldMouse = new NbtCompound();
    public void setHeldMouse(FieldMouseEntity e) {
        for (String key : heldMouse.getKeys()) {
            heldMouse.remove(key);
        }
        e.writeNbt(heldMouse);
        e.writeCustomDataToNbt(heldMouse);
        ownerMouse = e.getUuid();
    }
    public @NotNull FieldMouseEntity spawnHeldMouse () {
        assert heldMouse.contains("UUID") && world != null;
        var m = new FieldMouseEntity(ModEntityTypes.FIELD_MOUSE_ENTITY_TYPE, getWorld());
        m.setPosition(this.pos.toCenterPos());
        m.readNbt(heldMouse);
        m.readCustomDataFromNbt(heldMouse);
        world.spawnEntity(m);
        ownerMouse = null;
        m.burrow = null;
        m.ticksSinceBurrowLeft = 0;
        return m;
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("heldMouse", heldMouse);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    public FieldMouseBurrowBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public FieldMouseBurrowBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.FIELD_MOUSE_BURROW_BLOCK_ENTITY, pos, state);
    }
}
