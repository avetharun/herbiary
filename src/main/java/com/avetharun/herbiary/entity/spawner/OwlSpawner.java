package com.avetharun.herbiary.entity.spawner;

import com.avetharun.herbiary.entity.ModEntityTypes;
import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.spawner.Spawner;

public class OwlSpawner implements Spawner {
    public static final int SPAWN_RADIUS = 100;
    private int ticksUntilNextSpawn = 600;
    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (spawnAnimals && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            if (--this.ticksUntilNextSpawn < 0) {
                this.ticksUntilNextSpawn = 300;
                if (!world.isNight()) return 0;
                Random random = world.random;
                if (random.nextInt(30) > 24) {
                    world.getPlayers().forEach(serverPlayerEntity -> {
                        int x = (8 + random.nextInt(32)) * (random.nextBoolean() ? -1 : 1);
                        int y = (random.nextInt(4)) * (random.nextBoolean() ? -1 : 1);
                        int z = (8 + random.nextInt(32)) * (random.nextBoolean() ? -1 : 1);
                        BlockPos blockPos = serverPlayerEntity.getBlockPos().add(x, y, z);
                        BlockPos villagePos = world.locateStructure(StructureTags.VILLAGE, blockPos, 5, false);
                        if (villagePos != null) {
                            summon(world, blockPos.add(0, random.nextBetween(5, 8), 0));
                        } else {
                            if (random.nextInt(32) > 28) {
                                OwlEntity e = summon(world, blockPos.add(0, random.nextBetween(6, 8), 0));
                                if (e != null) {
                                    return;
                                }
                            }
                        }
                    });
                }
            }
        }
        return 1;
    }
    private OwlEntity summon(ServerWorld world, BlockPos blockPos) {
        OwlEntity e = ModEntityTypes.OWL_ENTITY_TYPE.create(world);
        if (e == null) return null;
        e.initialize(world, world.getLocalDifficulty(blockPos), SpawnReason.NATURAL, null, null);
        e.refreshPositionAndAngles(blockPos, 0, 0);
        world.spawnEntityAndPassengers(e);
        return e;
    }
}
