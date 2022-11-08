package com.avetharun.herbiary.client;

import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemModel;
import com.avetharun.herbiary.hUtil.ModItems;
import com.avetharun.herbiary.client.entity.OwlEntityRenderer;
import com.avetharun.herbiary.entity.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HerbiaryClient implements ClientModInitializer {
    BakedModel SPEAR_BAKED_GUI_MODEL;
    public static final EntityModelLayer MODEL_SPEAR_LAYER = new EntityModelLayer(new Identifier("al_herbiary", "spear_entity"), "spear");
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntityTypes.OWL_ENTITY_TYPE, OwlEntityRenderer::new);
        EntityRendererRegistry.register(ModItems.SPEAR_ENTITY_TYPE, HerbiarySpearItemModel::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_SPEAR_LAYER, HerbiarySpearItemModel::getTexturedModelData);
        ModelLoadingRegistry.INSTANCE.registerModelProvider(((manager, out) -> out.accept(new ModelIdentifier(new Identifier("al_herbiary", "spear_inventory"), "inventory"))));
    }
}
