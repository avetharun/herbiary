package com.avetharun.herbiary.hUtil;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class VoxelShapeUtils {
    public static VoxelShape Scale(VoxelShape shape, float x, float y, float z) {
        return VoxelShapes.cuboid(shape.getBoundingBox().contract(x,y,z));
    }
    public static VoxelShape Scale(VoxelShape shape, float amt) {
        return VoxelShapes.cuboid(shape.getBoundingBox().contract(amt, amt, amt));
    }
}
