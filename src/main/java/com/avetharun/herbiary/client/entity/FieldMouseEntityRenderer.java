package com.avetharun.herbiary.client.entity;

import com.avetharun.herbiary.client.entity.model.FieldMouseEntityModel;
import com.avetharun.herbiary.client.entity.model.OwlEntityModel;
import com.avetharun.herbiary.entity.FieldMouseEntity;
import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FieldMouseEntityRenderer extends GeoEntityRenderer<FieldMouseEntity> {
    // variables needed for later
    private ItemStack itemStack;
    private VertexConsumerProvider vertexConsumerProvider;
    private Identifier ratTexture;

    public FieldMouseEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FieldMouseEntityModel());
        this.shadowRadius = 0.05f;
    }
}