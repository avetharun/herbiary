package com.avetharun.herbiary.entity;

import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class ModEntityTypes {

    public static final EntityType<OwlEntity> OWL_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier("al_herbiary", "owl"), OwlEntity.getEntityType());

    public static void InitializeModEntityTypes(){
        FabricDefaultAttributeRegistry.register(OWL_ENTITY_TYPE, OwlEntity.createEntityAttributes());

    }
    static {
    };
}
