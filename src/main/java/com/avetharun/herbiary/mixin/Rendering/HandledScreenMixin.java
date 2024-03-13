package com.avetharun.herbiary.mixin.Rendering;


import com.avetharun.dpu.client.DatapackUtilsClient;
import com.avetharun.dpu.client.ModelOverrides.BooleanModelOverride;
import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Mixin(InventoryScreen.class)
    public static class InventoryScreenMixin{
//        @Inject(method="render", at=@At("HEAD"))
//        void preRenderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//            InventoryScreen iS = (InventoryScreen) (Object)this;
//            iS.getScreenHandler().getStacks().forEach(stack -> {
//                alib.setMixinField(stack, "isBeingRenderedInHand", false);
//                alib.setMixinField(stack, "isBeingRenderedInGUICompat", true);
//            });
//        }
//        @Inject(method="render", at=@At("TAIL"))
//        void postRenderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//            InventoryScreen iS = (InventoryScreen) (Object)this;
//            iS.getScreenHandler().getStacks().forEach(stack -> {
//                alib.setMixinField(stack, "isBeingRenderedInHand", false);
//                alib.setMixinField(stack, "isBeingRenderedInGUICompat", false);
//            });
//        }

    }
    @Mixin(HeldItemRenderer.class)
    public static class HeldItemRendererMixin{
        @Inject(at=@At("HEAD"),method="renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
        void renderItemMixin(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
            alib.setMixinField(stack, "isBeingRenderedInHand", true);
            alib.setMixinField(stack, "isBeingRenderedInGUICompat", false);
            alib.setMixinField(stack, "isBeingRenderedInHotbar", false);
        }
    }
    @Mixin(PlayerHeldItemFeatureRenderer.class)
    public static class PlayerEntityRenderer_HandMixin{
//        @Inject(method="renderItem", at=@At("HEAD"))
//        void renderItemMixin(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
//            alib.setMixinField(stack, "isBeingRenderedInHand", true);
//            alib.setMixinField(stack, "isBeingRenderedInGUICompat", false);
//            alib.setMixinField(stack, "isBeingRenderedInHotbar", false);
//        }
    }
    @Mixin(DrawContext.class)
    public static class DrawContextMixin{
//        @Inject(method="drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at=@At(value = "INVOKE",target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", shift = At.Shift.BEFORE))
//        private void drawItemMixin(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci){
//            alib.setMixinField(stack, "isBeingRenderedInGUICompat", false);
//            alib.setMixinField(stack, "isBeingRenderedInHand", false);
//        }
//        @Inject(method="drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at=@At("HEAD"))
//        private void drawItemInSlotMixin(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
//            alib.setMixinField(stack, "isBeingRenderedInGUICompat", true);
//            alib.setMixinField(stack, "isBeingRenderedInHand", false);
//        }
    }
    @Mixin(CreativeInventoryScreen.class)
    public static class PlayerScreenHandlerMixin {
        @Shadow @Nullable private List<Slot> slots;
        @Inject(method="render", at=@At("HEAD"))
        private void preRenderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
            BooleanModelOverride.currentModelTransform = ModelTransformationMode.GUI;
        }
        @Inject(method="render", at=@At("TAIL"))
        private void postRenderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
            BooleanModelOverride.currentModelTransform = ModelTransformationMode.NONE;
        }
    }
    @Inject(method="drawSlot", at=@At("HEAD"))
    void drawSlotBegin(DrawContext context, Slot slot, CallbackInfo ci){
        ItemStack itemStack = slot.getStack();
        if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen iS) {
            DatapackUtilsClient.currentInventorySlot = slot.id;
        }
        DatapackUtilsClient.currentGlobalSlot = slot.id;
    }
    @Inject(method="drawSlot", at=@At("HEAD"))
    void drawSlotEnd(DrawContext context, Slot slot, CallbackInfo ci){
        ItemStack itemStack = slot.getStack();
        DatapackUtilsClient.currentInventorySlot = -1;
    }
}
