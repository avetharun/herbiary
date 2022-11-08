package com.avetharun.herbiary.Items.ItemEntities;

import com.avetharun.herbiary.client.HerbiaryClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

// Made with Blockbench 4.4.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class HerbiarySpearItemModel extends EntityRenderer<HerbiarySpearItemEntity> {
	public static Identifier TEXTURE;
	private final ModelPart bb_main;
	public HerbiarySpearItemModel(EntityRendererFactory.Context context) {
		super(context);
		TEXTURE  = new Identifier("al_herbiary", "textures/entity/spear.png");
		this.bb_main = context.getPart(HerbiaryClient.MODEL_SPEAR_LAYER).getChild("bb_main");
	}

	@Override
	public Identifier getTexture(HerbiarySpearItemEntity entity) {
		return TEXTURE;
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 1).cuboid(-0.5F, -25.0F, -0.5F, 1.0F, 28.0F, 1.0F, new Dilation(0.0F))
				.uv(4, 4).cuboid(-1.5F, -29.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(10, 8).cuboid(-1.0F, -31.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(10, 0).cuboid(-0.5F, -32.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(4, 0).cuboid(-1.0F, -28.5F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
				.uv(4, 8).cuboid(-0.5F, -30.5F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 30).cuboid(-1.0F, -26.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(HerbiarySpearItemEntity tridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, tridentEntity.prevYaw, tridentEntity.getYaw()) - 90.0F));
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, tridentEntity.prevPitch, tridentEntity.getPitch()) + 90.0F));
		bb_main.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(TEXTURE)), i, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		matrixStack.pop();
	}
}