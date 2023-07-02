package com.avetharun.herbiary.client.entity;

import com.avetharun.herbiary.client.entity.model.OwlEntityModel;
import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OwlEntityRenderer extends GeoEntityRenderer<OwlEntity> {
    // variables needed for later
    private ItemStack itemStack;
    private VertexConsumerProvider vertexConsumerProvider;
    private Identifier ratTexture;

    public OwlEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new OwlEntityModel());
        this.shadowRadius = 0.35f;
    }
}