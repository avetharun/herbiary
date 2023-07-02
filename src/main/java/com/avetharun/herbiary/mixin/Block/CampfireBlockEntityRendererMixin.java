package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.CampfireBlockEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CampfireBlockEntityRenderer.class)
public class CampfireBlockEntityRendererMixin{
    @Inject(at=@At("HEAD"), method="render(Lnet/minecraft/block/entity/CampfireBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
    void renderInject(CampfireBlockEntity campfireBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        CampfireBlockEntityRenderer _this = (CampfireBlockEntityRenderer) (Object)this;
        ItemStack s = alib.getMixinField(campfireBlockEntity, "occupation");
        if (s != null) {
            Identifier id = Registries.ITEM.getId(s.getItem());
            Optional<Block> bO = Registries.BLOCK.getOrEmpty(id);
            if (bO.isEmpty()) {return;}
            Block b = bO.get();
            BlockState sT = b.getStateWithProperties(campfireBlockEntity.getCachedState());
            MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(id, "occupation"));
            assert campfireBlockEntity.getWorld() != null;
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(sT, campfireBlockEntity.getPos(), campfireBlockEntity.getWorld(), matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(b.getDefaultState())), true, campfireBlockEntity.getWorld().random);
        }


    }

}