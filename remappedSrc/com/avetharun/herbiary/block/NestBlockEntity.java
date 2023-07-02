package com.avetharun.herbiary.block;

import com.avetharun.herbiary.entity.OwlEntity;
import com.avetharun.herbiary.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class NestBlockEntity extends BlockEntity {
    public UUID parent_bird;

    public NestBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.BIRD_NEST_BLOCK_ENTITY, pos, state);
    }
    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound nbt) {
        // Save the current value of the number to the nbt
        if (parent_bird != null) {
            nbt.putUuid("owner", parent_bird);
        }
        super.writeNbt(nbt);
    }
    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("owner")) {
            parent_bird = nbt.getUuid("owner");
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, NestBlockEntity be) {
        List<OwlEntity> entities = be.getWorld().getEntitiesByType(TypeFilter.instanceOf(OwlEntity.class), Box.of(Vec3d.ofCenter(pos), 48, 38, 48), owlEntity -> {
            if (owlEntity.getUuid() == be.parent_bird) {
                return true;
            }
            return false;
        });
        if (entities.isEmpty()) {
            be.parent_bird = null;
        }
    }
}
