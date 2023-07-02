package com.avetharun.herbiary.screens;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.block.CampfirePotBlockEntity;
import com.avetharun.herbiary.recipes.RecipesUtil;
import com.avetharun.herbiary.recipes.ShapelessPotRecipe;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class CampfirePotScreenHandler extends CampfirePlacementScreenHandler {
    public static class CampfirePotResultAmount {
        public int amt = 0;
    }
    private final ScreenHandlerContext context;
    private final World world;
    private final List<ShapelessPotRecipe> availableRecipes;
    long lastTakeTime;
    public Slot inputSlot1, inputSlot2, inputSlot3, inputSlot4;
    public Slot outputSlot;
    public final Inventory input;
    final Inventory output;
    private final PlayerEntity player;
    CampfirePotBlockEntity blockEntity;

    public CampfirePotScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(42), new SimpleInventory(40), ScreenHandlerContext.create(playerInventory.player.method_48926(), playerInventory.player.getBlockPos()),playerInventory.player, null);
    }
    public CampfirePotScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory outputInventory, final ScreenHandlerContext context, PlayerEntity player, CampfirePotBlockEntity be) {
        super(RecipesUtil.POT_SCREEN_HANDLER, syncId);
        this.blockEntity = be;
        this.player = player;
        if (this.player == null) {
            throw new RuntimeException("What the fuck, how is the player null.");
        }
        this.availableRecipes = Lists.newArrayList();
        this.input = inventory;
        this.output = outputInventory;
        this.context = context;
        this.world = playerInventory.player.method_48926();
        this.inputSlot1 = this.addSlot(new Slot(this.input, 0, 44, 18));
        this.inputSlot1.setStack(this.input.getStack(0));
        this.inputSlot2 = this.addSlot(new Slot(this.input, 1, 62, 18));
        this.inputSlot2.setStack(this.input.getStack(1));
        this.inputSlot3 = this.addSlot(new Slot(this.input, 2, 44, 36));
        this.inputSlot3.setStack(this.input.getStack(2));
        this.inputSlot4 = this.addSlot(new Slot(this.input, 3, 62, 36));
        this.inputSlot3.setStack(this.input.getStack(4));
        this.outputSlot = this.addSlot(new Slot(this.output, 4, 130, 28) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    public ScreenHandlerType<?> getType() {
        return RecipesUtil.POT_SCREEN_HANDLER;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }
    @Override
    public void onClosed (PlayerEntity player) {
        this.input.onClose(player);
        this.output.onClose(player);
        super.onClosed(player);
    }

    public static class Factory {
        public static CampfirePotScreenHandler create(int syncId, PlayerInventory playerInventory) {
            return new CampfirePotScreenHandler(syncId, playerInventory);
        }
    }

    public static class CampfirePotScreen extends HandledScreen<CampfirePotScreenHandler> {

        private static final Identifier TEXTURE = new Identifier("al_herbiary",  "textures/gui/container/campfire_pot.png");
        public Inventory gridInventory = new SimpleInventory(4);
        public Inventory resultInventory = new SimpleInventory(1);

        public CampfirePotScreen(CampfirePotScreenHandler handler, PlayerInventory inventory, Text title) {
            super(handler, inventory, title);
            --this.titleY;
        }

        public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
            super.render(matrices, mouseX, mouseY, delta);
            this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        }

        protected void drawBackground(DrawContext matrices, float delta, int mouseX, int mouseY) {
            this.renderBackground(matrices);
            int i = this.x;
            int j = this.y;
            matrices.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        }

        protected void drawMouseoverTooltip(DrawContext matrices, int x, int y) {
            super.drawMouseoverTooltip(matrices, x, y);
        }
    }

}