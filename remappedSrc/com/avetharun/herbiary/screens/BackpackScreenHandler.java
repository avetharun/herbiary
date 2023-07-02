package com.avetharun.herbiary.screens;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BackpackScreenHandler extends ScreenHandler {
    SimpleInventory backpackInventory;
    PlayerInventory playerInventory;
    ItemStack backpackStack;
    private static class BackpackScreenSlot extends Slot {
        public BackpackScreenSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getMaxItemCount() {
            return 8;
        }

        @Override
        public int getMaxItemCount(ItemStack stack) {
            return Math.min(stack.getMaxCount(), 8);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return !stack.isOf(ModItems.SMALL_BACKPACK.getRight()) && !stack.isOf(getStack().getItem());
        }
    }
    private static class RestrictedBackpackScreenSlot extends BackpackScreenSlot {
        Item allowedItemType;
        TagKey<Item> allowedItemTypes;
        public RestrictedBackpackScreenSlot(Inventory inventory, int index, int x, int y, @Nullable Item allowedItemType, @Nullable TagKey<Item> allowedItemTypes) {
            super(inventory, index, x, y);
            this.allowedItemType = allowedItemType;
            this.allowedItemTypes = allowedItemTypes;
        }
        public RestrictedBackpackScreenSlot(Inventory inventory, int index, int x, int y, Item allowedItemType) {
            super(inventory, index, x, y);
            this.allowedItemType = allowedItemType;
        }
        public RestrictedBackpackScreenSlot(Inventory inventory, int index, int x, int y, TagKey<Item> allowedItemTypes) {
            super(inventory, index, x, y);
            this.allowedItemTypes = allowedItemTypes;
        }
        @Override
        public boolean canInsert(ItemStack stack) {
            boolean bpk = (!stack.isOf(ModItems.SMALL_BACKPACK.getRight()) && !stack.isOf(getStack().getItem()));
            if (allowedItemTypes != null) {
                bpk = bpk && stack.isIn(allowedItemTypes);
            }
            if (allowedItemType != null && allowedItemTypes == null) {
                bpk = bpk && stack.isOf(allowedItemType);
            }
            return bpk;
        }
    }
    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, SimpleInventory backpackInventory, ItemStack backpackStack) {
        super(BACKPACK_SCREEN_HANDLER_TYPE, syncId);
        checkSize(backpackInventory, 18);
        this.backpackInventory = backpackInventory;
        this.playerInventory = playerInventory;
        this.backpackStack = backpackStack;
        backpackInventory.onOpen(playerInventory.player);
        playerInventory.onOpen(playerInventory.player);
        int idx_base = 0;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 4; column++) {
                int index = ++idx_base;
                int x = 8 + column * 18;
                int y = 18 + row * 18;
                this.addSlot(new BackpackScreenSlot(backpackInventory, index, x, y));
            }
        }

        // Right-side slots (6 slots, arranged as 2 columns, 3 rows)
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 2; column++) {
                int index = ++idx_base;
                int x = 116 + column * 18;
                int y = 18 + row * 18;
                if (row == 0 && column == 0) {
                    bowSlot = this.addSlot(new RestrictedBackpackScreenSlot(backpackInventory, index, x, y, Herbiary.BOWS));
                    bowSlotx = x;
                    bowSloty = y;
                }
                if (row == 1 && column == 0) {
                    axeSlot = this.addSlot(new RestrictedBackpackScreenSlot(backpackInventory, index, x, y, ItemTags.AXES));
                    axeSlotx = x;
                    axeSloty = y;
                }
                if (row == 1 && column == 1) {
                    strikerSlot = this.addSlot(new RestrictedBackpackScreenSlot(backpackInventory, index, x, y, Items.FLINT_AND_STEEL));
                    strikerSlotx = x;
                    strikerSloty = y;
                }
                if (row == 2 && column == 0) {
                    canteenSlot = this.addSlot(new RestrictedBackpackScreenSlot(backpackInventory, index, x, y, ModItems.LEATHER_FLASK));
                    canteenSlotx = x;
                    canteenSloty = y;
                }
                if (row == 2 && column == 1) {
                    knifeSlotx = x;
                    knifeSloty = y;
                    knifeSlot = this.addSlot(new RestrictedBackpackScreenSlot(backpackInventory, index, x, y, Herbiary.KNIVES));
                }
            }
        }
        // Player inventory
        int k,l;

        for(k = 0; k < 3; ++k) {
            for(l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + k * 9 + 9, 8 + l * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(20), null);
    }
    private int axeSlotx, axeSloty,
                strikerSlotx, strikerSloty,
                canteenSlotx, canteenSloty,
                knifeSlotx, knifeSloty,
                bowSlotx, bowSloty;
    Slot axeSlot, strikerSlot, canteenSlot, knifeSlot, bowSlot;
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot) this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot <= 18) {
                if (!this.insertItem(itemStack2, 18, 45, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return ItemStack.EMPTY;
    }
    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.backpackInventory.onClose(player);
        this.playerInventory.onClose(player);
    }


    public static final Identifier SID = new Identifier("al_herbiary", "backpack");
    public static ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER_TYPE;
    public static class BackpackScreen extends HandledScreen<BackpackScreenHandler> {
        private static final Identifier BACKGROUND_TEXTURE = new Identifier("al_herbiary", "textures/gui/container/backpack.png");

        public BackpackScreen(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
            super(handler, inventory, title);
            this.backgroundWidth = 176; // Set the width of your custom texture
            this.backgroundHeight = 166; // Set the height of your custom texture
        }

        @Override
        protected void drawBackground(DrawContext matrices, float delta, int mouseX, int mouseY) {
            int x = (this.width - this.backgroundWidth) / 2;
            int y = (this.height - this.backgroundHeight) / 2;
            matrices.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
            x-=1;
            y-=1;
            if (!handler.axeSlot.hasStack()) {
                matrices.drawTexture(BACKGROUND_TEXTURE, x+handler.axeSlotx, y+handler.axeSloty, 176, 0, 18,18);
            }if (!handler.strikerSlot.hasStack()) {
                matrices.drawTexture(BACKGROUND_TEXTURE, x+handler.strikerSlotx, y+handler.strikerSloty, 194, 0, 18,18);
            }if (!handler.canteenSlot.hasStack()) {
                matrices.drawTexture(BACKGROUND_TEXTURE, x+handler.canteenSlotx, y+handler.canteenSloty, 176, 18, 18,18);
            }if (!handler.knifeSlot.hasStack()) {
                matrices.drawTexture(BACKGROUND_TEXTURE, x+handler.knifeSlotx, y+handler.knifeSloty, 194,  18, 18,18);
            }if (!handler.bowSlot.hasStack()) {
                matrices.drawTexture(BACKGROUND_TEXTURE, x+handler.bowSlotx, y+handler.bowSloty, 176,  36, 18,18);
            }
        }

        @Override
        public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
            super.render(matrices, mouseX, mouseY, delta);
            this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        }
    }
}
