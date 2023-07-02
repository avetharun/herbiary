package com.avetharun.herbiary.entity.block;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.recipes.RecipesUtil;
import com.avetharun.herbiary.screens.CampfirePotScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CampfirePotBlockEntity extends AbstractCampfirePotBlockEntity{
    public CampfirePotBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.CAMPFIRE_POT_BLOCK_ENTITY, pos, state, RecipesUtil.POT_RECIPE_TYPE);
    }
    ItemStack output;
    @Override
    protected Text getContainerName() {
        return Text.translatable("campfire.pot");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        var e_inventory = new SimpleInventory(4) {
            @Override
            public void markDirty() {
                CampfirePotBlockEntity.this.setStack(0, this.getStack(0));
                CampfirePotBlockEntity.this.setStack(1, this.getStack(1));
                CampfirePotBlockEntity.this.setStack(2, this.getStack(2));
                CampfirePotBlockEntity.this.setStack(3, this.getStack(3));
                CampfirePotBlockEntity.this.markDirty();
                super.markDirty();
            }

            @Override
            public void onClose(PlayerEntity player) {
                super.onClose(player);
                CampfirePotBlockEntity.this.setStack(0, this.getStack(0));
                CampfirePotBlockEntity.this.setStack(1, this.getStack(1));
                CampfirePotBlockEntity.this.setStack(2, this.getStack(2));
                CampfirePotBlockEntity.this.setStack(3, this.getStack(3));
                CampfirePotBlockEntity.this.markDirty();
            }
        };
        var o_inventory = new SimpleInventory(1) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void markDirty() {
                CampfirePotBlockEntity.this.setStack(4, this.getStack(0));
                CampfirePotBlockEntity.this.markDirty();
            }

            @Override
            public void onClose(PlayerEntity player) {
                super.onClose(player);
                CampfirePotBlockEntity.this.setStack(4, this.getStack(0));
                CampfirePotBlockEntity.this.markDirty();
            }
        };
        for (int i = 0; i < 4; i++) {
            e_inventory.setStack(i, this.getStack(i));
        }
        return this.getCachedState().createScreenHandlerFactory(world, pos).createMenu(syncId,playerInventory,playerInventory.player);

    }

    public static class Renderer implements BlockEntityRenderer<CampfirePotBlockEntity> {
        public Renderer(BlockEntityRendererFactory.Context ctx) {
        }

        @Override
        public void render(CampfirePotBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            assert blockEntity.world != null;
            if (blockEntity.isRemoved()) {
                blockEntity.world.removeBlockEntity(blockEntity.pos);
                return;
            }
        }
    }
}
