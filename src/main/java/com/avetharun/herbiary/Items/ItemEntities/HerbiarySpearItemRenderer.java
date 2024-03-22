package com.avetharun.herbiary.Items.ItemEntities;

import com.avetharun.herbiary.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HerbiarySpearItemRenderer extends GeoEntityRenderer<HerbiarySpearItemEntity> {
    public static final GeoModel<HerbiarySpearItemEntity> MODEL = new GeoModel<HerbiarySpearItemEntity>() {
        @Override
        public Identifier getModelResource(HerbiarySpearItemEntity animatable) {
            return Identifier.of("al_herbiary", "geo/spear.json");
        }

        @Override
        public Identifier getTextureResource(HerbiarySpearItemEntity animatable) {
            return Identifier.of("al_herbiary", "textures/item/spear_in_hand.png");
        }

        @Override
        public Identifier getAnimationResource(HerbiarySpearItemEntity animatable) {
            return null;
        }
    };

    public HerbiarySpearItemRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, MODEL);
    }
    @Override
    public void render(HerbiarySpearItemEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light) {
        matrixStack.push();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) - 90.0F)));
        matrixStack.translate(0,-1.45,0);
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumers, light);
        matrixStack.pop();
    }
}
