package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.block.BookMat;
import com.avetharun.herbiary.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.block.entity.LecternBlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternBlockEntityRenderer.class)
public class LecternBlockEntityRendererMixin {

    @Shadow @Final private BookModel book;

    @Inject(at=@At("HEAD"), method="render(Lnet/minecraft/block/entity/LecternBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
    public void render(LecternBlockEntity lecternBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        BlockState blockState = lecternBlockEntity.getCachedState();
        if (blockState.isOf(Blocks.LECTERN) && blockState.get(LecternBlock.HAS_BOOK)) {
            matrixStack.push();
            matrixStack.translate(0.5, 1.0625, 0.5);
            float g = ((Direction)blockState.get(LecternBlock.FACING)).rotateYClockwise().asRotation();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-g));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(67.5F));
            matrixStack.translate(0.0, -0.125, 0.0);
            this.book.setPageAngles(0.0F, 0.1F, 0.9F, 1.2F);
            VertexConsumer vertexConsumer = EnchantingTableBlockEntityRenderer.BOOK_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
            this.book.renderBook(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        } else if (blockState.isOf(ModItems.BOOK_MAT.getLeft()) && blockState.get(BookMat.HAS_BOOK)) {
            matrixStack.push();
            matrixStack.translate(0.5, 1.0625, 0.5);
            float g = ((Direction)blockState.get(LecternBlock.FACING)).rotateYClockwise().asRotation();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-g));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(67.5F));
            matrixStack.translate(0.0, -0.125, 0.0);
            this.book.setPageAngles(0.0F, 0.1F, 0.9F, 1.2F);
            VertexConsumer vertexConsumer = EnchantingTableBlockEntityRenderer.BOOK_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
            this.book.renderBook(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        }
    }
}
