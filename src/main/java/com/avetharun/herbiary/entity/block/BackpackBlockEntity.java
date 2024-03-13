package com.avetharun.herbiary.entity.block;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.block.ToolrackBlock;
import com.avetharun.herbiary.screens.BackpackScreenHandler;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class BackpackBlockEntity extends BlockEntity {
    private static final String TAG_BACKPACK_ITEMS = "backpackItems";
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(World world, ItemStack stack, SimpleInventory backpackInventory) {
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new BackpackScreenHandler(syncId, playerInventory, backpackInventory, stack), this.backpack.getName());
    }
    public ItemStack backpack = ItemStack.EMPTY;
    public BackpackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    private void saveInventoryToNbt(DefaultedList<ItemStack> stacks) {
        NbtList itemList = new NbtList();

        for (ItemStack itemStack : stacks) {
            NbtCompound itemTag = itemStack.writeNbt(new NbtCompound());
            itemList.add(itemTag);
        }
        assert backpack.getOrCreateNbt() != null;
        backpack.getOrCreateNbt().put(TAG_BACKPACK_ITEMS, itemList);
    }

    public static class Renderer implements BlockEntityRenderer<BackpackBlockEntity> {
        public Renderer(BlockEntityRendererFactory.Context ctx) {
        }

        @Override
        public void render(BackpackBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            assert blockEntity.world != null;
            if (blockEntity.isRemoved()) {
                blockEntity.world.removeBlockEntity(blockEntity.pos);
                return;
            }
        }

    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        assert nbt.contains("bp");
        backpack = ItemStack.fromNbt(nbt.getCompound("bp"));
    }

    public TypedActionResult<ItemStack> useExt(World world, PlayerEntity player, Hand hand) {

        SimpleInventory backpackInventory = new SimpleInventory(19) {
            @Override
            public boolean isValid(int slot, ItemStack stack) {
                return !stack.isOf(ModItems.SMALL_BACKPACK.getRight()) && super.isValid(slot, stack);
            }

            @Override
            public void onClose(PlayerEntity player) {
                if (player instanceof ServerPlayerEntity) {
                    saveInventoryToNbt(this.heldStacks);
                    NbtCompound c = new NbtCompound();
                    System.out.println("saved! " + backpack.writeNbt(c));
                }
                super.onClose(player);
            }
        };
        if (this.backpack.getOrCreateNbt() != null) {
            NbtList tag = this.backpack.getOrCreateNbt().getList(TAG_BACKPACK_ITEMS, NbtList.COMPOUND_TYPE);
            for (int i = 0; i < tag.size(); i++) {
                NbtCompound c = (NbtCompound) tag.get(i);
                backpackInventory.setStack(i, ItemStack.fromNbt(c));
            }
        }

        player.openHandledScreen(createScreenHandlerFactory(world, this.backpack, backpackInventory));
        if (!world.isClient()) {
            // Synchronize the inventory data with the client
            saveInventoryToNbt(backpackInventory.heldStacks);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        assert backpack != null;
        NbtCompound bpC = new NbtCompound();
        backpack.writeNbt(bpC);
        nbt.put("bp", bpC);
        super.writeNbt(nbt);
    }
    public void setBackpack(ItemStack backpack) {
        this.backpack = backpack;
    }
    public BackpackBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.BACKPACK_BLOCK_ENTITY, pos, state);
        this.backpack = ItemStack.EMPTY;
    }
    public BackpackBlockEntity(BlockPos pos, BlockState state, ItemStack stack) {
        super(ModItems.BACKPACK_BLOCK_ENTITY, pos, state);
        this.backpack = stack;
    }
}
