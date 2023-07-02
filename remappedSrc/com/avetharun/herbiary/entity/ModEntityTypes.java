package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import software.bernie.geckolib.GeckoLib;

public class ModEntityTypes {

    public static final EntityType<OwlEntity> OWL_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier("al_herbiary", "owl"), OwlEntity.getEntityType());
    public static final EntityType<FieldMouseEntity> FIELD_MOUSE_ENTITY_TYPE = registerMob("field_mouse", FieldMouseEntity::new, .25f, .25f);
    public static final EntityType<TentEntity> TENT_ENTITY_TYPE = registerMob("tent", TentEntity::new, 2, 0.5f);
    public static final EntityType<TentEntity.TentStorageEntity> TENT_STORAGE_ENTITY_ENTITY_TYPE = registerMobHidden("tent_storage", TentEntity.TentStorageEntity::new, 1, 1);
    public static <T extends Entity> EntityType<T> registerMob(String name, EntityType.EntityFactory<T> entity,
                                                                  float width, float height) {
        return Registry.register(Registries.ENTITY_TYPE,
                new Identifier("al_herbiary", name),FabricEntityTypeBuilder.create(SpawnGroup.MISC, entity).dimensions(EntityDimensions.changing(width, height)).build());
    }public static <T extends Entity> EntityType<T> registerMobHidden(String name, EntityType.EntityFactory<T> entity,
                                                                float width, float height) {
        return Registry.register(Registries.ENTITY_TYPE,
                new Identifier("al_herbiary", name),FabricEntityTypeBuilder.create(SpawnGroup.MISC, entity).dimensions(EntityDimensions.changing(width, height)).disableSummon().build());
    }
    public static void InitializeModEntityTypes(){
        FabricDefaultAttributeRegistry.register(OWL_ENTITY_TYPE, OwlEntity.createEntityAttributes());
        FabricDefaultAttributeRegistry.register(FIELD_MOUSE_ENTITY_TYPE, FieldMouseEntity.setAttributes());
    }
    static {
    };
}
