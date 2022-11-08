package com.avetharun.herbiary.mixin;

import com.avetharun.herbiary.hUtil.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.ItemTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.render.item.ItemRenderer.*;

@Mixin(ItemRenderer.class)
public abstract class HotbarItemRendererMixin {
    @Shadow public abstract ItemModels getModels();

    @Shadow protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);

    @Shadow @Final private BuiltinModelItemRenderer builtinModelItemRenderer;


    @Inject(method="getModel", at = @At(value="HEAD"), cancellable = true)
    public void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        BakedModel bakedModel;
        if (stack.isOf(Items.TRIDENT)) {
            bakedModel = this.getModels().getModelManager().getModel(new ModelIdentifier("minecraft:trident_in_hand#inventory"));
        } else if (stack.isOf(Items.SPYGLASS)) {
            bakedModel = this.getModels().getModelManager().getModel(new ModelIdentifier("minecraft:spyglass_in_hand#inventory"));
        }
        else {
            bakedModel = this.getModels().getModel(stack);
        }

        ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
        BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
        cir.setReturnValue(bakedModel2 == null ? this.getModels().getModelManager().getMissingModel() : bakedModel2);

        cir.cancel();
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
    at=@At(value = "HEAD"),
    cancellable = true)
    public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            matrices.push();
            boolean bl = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;
            if (bl) {
                if (stack.isOf(Items.TRIDENT)) {
                    model = this.getModels().getModelManager().getModel(new ModelIdentifier("minecraft:trident#inventory"));
                } else if (stack.isOf(Items.SPYGLASS)) {
                    model = this.getModels().getModelManager().getModel(new ModelIdentifier("minecraft:spyglass#inventory"));
                } else if (stack.isOf(ModItems.SPEAR_ITEM)) {
                    model = this.getModels().getModelManager().getModel(new ModelIdentifier("al_herbiary:spear_inventory#inventory"));
                }
            }

            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5, -0.5, -0.5);
            if (!model.isBuiltin() && (!stack.isOf(Items.TRIDENT) || bl)) {
                boolean bl2;
                if (renderMode != ModelTransformation.Mode.GUI && !renderMode.isFirstPerson() && stack.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)stack.getItem()).getBlock();
                    bl2 = !(block instanceof TransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    bl2 = true;
                }

                RenderLayer renderLayer = RenderLayers.getItemLayer(stack, bl2);
                VertexConsumer vertexConsumer;
                if (stack.isIn(ItemTags.COMPASSES) && stack.hasGlint()) {
                    matrices.push();
                    MatrixStack.Entry entry = matrices.peek();
                    if (renderMode == ModelTransformation.Mode.GUI) {
                        entry.getPositionMatrix().multiply(0.5F);
                    } else if (renderMode.isFirstPerson()) {
                        entry.getPositionMatrix().multiply(0.75F);
                    }

                    if (bl2) {
                        vertexConsumer = getDirectCompassGlintConsumer(vertexConsumers, renderLayer, entry);
                    } else {
                        vertexConsumer = getCompassGlintConsumer(vertexConsumers, renderLayer, entry);
                    }

                    matrices.pop();
                } else if (bl2) {
                    vertexConsumer = getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
                } else {
                    vertexConsumer = getItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
                }
                if (stack.isOf(ModItems.SPEAR_ITEM)) {
                    vertexConsumer = vertexConsumer.light(12);

                }

                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            } else {
                this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
            }

            matrices.pop();
        }
        ci.cancel();
    }
}
