package com.avetharun.herbiary.client.entity;

import com.avetharun.herbiary.client.entity.model.OwlEntityModel;
import com.avetharun.herbiary.client.entity.model.TentEntityModel;
import com.avetharun.herbiary.entity.OwlEntity;
import com.avetharun.herbiary.entity.TentEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TentEntityRenderer extends GeoEntityRenderer<TentEntity> {
    // variables needed for later
    private ItemStack itemStack;
    private VertexConsumerProvider vertexConsumerProvider;
    private Identifier ratTexture;

    public TentEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TentEntityModel());
        this.shadowRadius = 0f;
        this.shadowOpacity = 0f;
    }

    @Override
    public void render(TentEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}