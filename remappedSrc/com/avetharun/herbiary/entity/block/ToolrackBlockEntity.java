package com.avetharun.herbiary.entity.block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.block.ToolrackBlock;
import com.avetharun.herbiary.ModItems;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.lang.reflect.Method;

public class ToolrackBlockEntity extends BlockEntity {
    public ItemStack HeldItem = ItemStack.EMPTY;
    public ToolrackBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.TOOLRACK_BLOCK_ENTITY, pos, state);

    }
    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound nbt) {
        NbtCompound i = new NbtCompound();
        HeldItem.writeNbt(i);
        nbt.put("item", i);
        super.writeNbt(nbt);
    }
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        HeldItem = ItemStack.fromNbt(nbt.getCompound("item"));
    }

    public static class Renderer implements BlockEntityRenderer<ToolrackBlockEntity> {
        public Renderer(BlockEntityRendererFactory.Context ctx) {
        }

        @Override
        public void render(ToolrackBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            assert blockEntity.world != null;
            if (blockEntity.isRemoved()) {
                blockEntity.world.removeBlockEntity(blockEntity.pos);
                return;
            }
            matrices.push();
            if (blockEntity.HeldItem.isOf(ModItems.SMALL_BACKPACK.getRight())) {
                matrices.translate(0.5, 0.6, 0.5);
                matrices.multiply(blockEntity.world.getBlockState(blockEntity.pos).get(ToolrackBlock.FACING).getRotationQuaternion());
                matrices.scale(0.25f, 0.25f, 0.25f);
                MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.HeldItem, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, blockEntity.world, 0);
            }
            else {
                // Move the item
                float padding = 0.1f;
                matrices.translate(0.5, 0.6, 0.5);
                if (blockEntity.HeldItem.isIn(Herbiary.BOWS)) {
                    matrices.translate(0, -0.1, 0);
                }
                matrices.multiply(blockEntity.world.getBlockState(blockEntity.pos).get(ToolrackBlock.FACING).getRotationQuaternion());
                if (blockEntity.HeldItem.isIn(ItemTags.SWORDS)) {
                    matrices.multiply(new Quaternionf().rotationXYZ((float) Math.toRadians(90), (float) Math.toRadians(180), (float) Math.toRadians(45)));
                    matrices.translate(0.1, -0.1, 0);
                } else {
                    matrices.translate(0, 0, -0.1);
                    matrices.multiply(new Quaternionf().rotationXYZ((float) Math.toRadians(90), (float) Math.toRadians(180), (float) Math.toRadians(45 - 90)));
                }
                matrices.translate(0, 0, -0.4);
                if (blockEntity.HeldItem.isIn(Herbiary.BOWS)) {
                    matrices.translate(-0.1, -0.1, 0);
                }
                matrices.scale(1.5f, 1.5f, 1.5f);
                MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.HeldItem, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, blockEntity.world, 0);

            }
            matrices.pop();
        }

    }
}
