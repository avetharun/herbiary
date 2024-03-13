package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.function.BiConsumer;

public class HerbBush extends PickableBlock{
    public HerbBush(Settings settings, BiConsumer<BlockPos, World> onPicked) {
        super(settings, onPicked);
    }
    public static BooleanProperty HAS_NEIGHBOR = Properties.ATTACHED;
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        boolean bl1 = world.getBlockState(pos.down()).isIn(BlockTags.DIRT) || world.getBlockState(pos.down()).isIn(Herbiary.FARMLAND);
        for (int x = pos.getX()-1; x <= pos.getX()+1 && !bl1; x++) {
            for (int y = pos.getY()-1; y <= pos.getY()+1 && !bl1; y++) {
                for (int z = pos.getZ()-1; z <= pos.getZ()+1 && !bl1; z++) {
                    BlockPos p = new BlockPos(x,y,z);
                    bl1 = world.getBlockState(p).isOf(this);
                }
            }
        }

        return bl1 && canPlantOnTop(world.getBlockState(pos.down()), world, pos);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.5, 0.75, 0.5));
            if (!world.isClient && (Integer)state.get(AGE) > 0 && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
                double d = Math.abs(entity.getX() - entity.lastRenderX);
                double e = Math.abs(entity.getZ() - entity.lastRenderZ);
                if (d >= 0.003 || e >= 0.003) {
                    entity.damage(world.getDamageSources().sweetBerryBush(), 1.0F);
                }
            }

        }
    }
    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(this) || super.canPlantOnTop(floor, world, pos);
    }
}
