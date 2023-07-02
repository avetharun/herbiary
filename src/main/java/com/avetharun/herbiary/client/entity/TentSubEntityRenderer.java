package com.avetharun.herbiary.client.entity;

import com.avetharun.herbiary.client.entity.model.TentEntityModel;
import com.avetharun.herbiary.entity.TentEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TentSubEntityRenderer extends EntityRenderer<TentEntity.TentStorageEntity> {
    public TentSubEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(TentEntity.TentStorageEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public Identifier getTexture(TentEntity.TentStorageEntity entity) {
        return null;
    }
}