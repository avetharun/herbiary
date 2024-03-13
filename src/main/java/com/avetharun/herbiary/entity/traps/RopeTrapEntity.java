package com.avetharun.herbiary.entity.traps;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.AbstractTrapEntity;
import com.avetharun.herbiary.entity.ModEntityTypes;
import com.avetharun.herbiary.entity.OwlEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.World;

public class RopeTrapEntity extends AbstractTrapEntity {
    public RopeTrapEntity(World world, BlockPos pos) {
        super(ModEntityTypes.ROPE_TRAP_ENTITY_TYPE, world, pos);
    }
    public RopeTrapEntity(EntityType<?> type, World world) {
        super(type, world);
    }
    public static EntityType<RopeTrapEntity> getEntityType() {
        return FabricEntityTypeBuilder.<RopeTrapEntity>create().
                dimensions(EntityDimensions.fixed(0.35f,0.9f))
                .entityFactory(RopeTrapEntity::new)
                .build();
    }
    @Override
    public BlockPos getTrapperPosition(BlockPos parentTrapPosition) {
        return getWorld().raycast(new BlockStateRaycastContext(Vec3d.ofCenter(parentTrapPosition), Vec3d.ofCenter(parentTrapPosition.down(3)), AbstractBlock.AbstractBlockState::isSolid)).getBlockPos();
    }

    @Override
    public ItemStack getDroppedStack() {
        return Items.ACACIA_BOAT.getDefaultStack();
    }

    @Override
    public TrapperPart createTrapper() {
        return new TrapperPart(this.getType(), this, this.getWorld()) {
            @Override
            public Box getTrapperHitbox() {
                return Box.of(Vec3d.ZERO, 0.5,0.25, 0.5);
            }
        };
    }
}
