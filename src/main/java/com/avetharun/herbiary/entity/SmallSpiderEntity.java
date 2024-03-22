package com.avetharun.herbiary.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;

public class SmallSpiderEntity extends SpiderEntity {
    public SmallSpiderEntity(EntityType<? extends SpiderEntity> entityType, World world) {
        super(entityType, world);
    }
}
