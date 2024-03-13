package com.avetharun.dpu;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatapackUtils implements ModInitializer {
    public static void debugRender(MatrixStack matrixStack, ModelTransformationMode mode, VertexConsumerProvider consumerProvider) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrixStack.translate(0,0,-.55);
        matrixStack.scale(0.05f,0.05f,0.05f);
//        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180 * MathHelper.RADIANS_PER_DEGREE));
        String modeS = mode.asString();
        if (mode.isFirstPerson()) {
            modeS = mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND ? "fpl_hand" : "fpr_hand";
        }
        MinecraftClient.getInstance().textRenderer.draw(modeS, 0,-20,0xffffffff,false, matrixStack.peek().getPositionMatrix(), consumerProvider, TextRenderer.TextLayerType.POLYGON_OFFSET,  0x000000000, 255);
        matrixStack.pop();
    }
    public static TagKey<Block> CAMPFIRE_SIGNAL_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft","campfire_signal_blocks"));
    /**
     * Runs the mod initializer.
     */
    public static boolean hasHadEvent = false;
    public static int CURRENT_TICK = 0;
    public static List<Consumer<MinecraftServer>> ScheduledActions = new ArrayList<>();
    public static void ScheduleForNextTick(Consumer<MinecraftServer> action) {
        ScheduledActions.add(action);
    }
//    public static DPUBlockHolderClass DPUBlockHolder = Registry.register(Registries.BLOCK, new Identifier("dpu", "block_holder"), new DPUBlockHolderClass(FabricBlockSettings.create()));
//    public static BlockEntityType<DPUBlockHolderClass.DPUBlockHolderEntity> DPUBlockHolderEntity = FabricBlockEntityTypeBuilder.create(DPUBlockHolderClass.DPUBlockHolderEntity::new, DPUBlockHolder).build();

    @Override
    public void onInitialize() {
    }
}
