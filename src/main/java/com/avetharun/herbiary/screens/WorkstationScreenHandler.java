package com.avetharun.herbiary.screens;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.recipes.WorkstationRecipe;
import com.avetharun.herbiary.recipes.RecipesUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

import java.util.List;

public class WorkstationScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Property selectedRecipe;
    private final World world;
    private List<RecipeEntry<WorkstationRecipe>> availableRecipes;
    private ItemStack inputStack;
    private ItemStack inputRockStack;
    long lastTakeTime;
    final Slot inputSlot;
    final Slot inputRockSlot;
    final Slot outputSlot;
    Runnable contentsChangedListener;
    public final Inventory input;
    final CraftingResultInventory output;
    public SoundEvent craftedSoundEvent;
    public WorkstationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.create(playerInventory.player.getWorld(), playerInventory.player.getBlockPos()));
    }

    public WorkstationScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(RecipesUtil.WORKSTATION_SCREEN_HANDLER, syncId);
        this.selectedRecipe = Property.create();
        this.availableRecipes = Lists.newArrayList();
        this.inputStack = ItemStack.EMPTY;
        this.inputRockStack = ItemStack.EMPTY;
        this.contentsChangedListener = () -> {
        };
        this.input = new SimpleInventory(2) {
            public void markDirty() {
                super.markDirty();
                WorkstationScreenHandler.this.onContentChanged(this);
                WorkstationScreenHandler.this.contentsChangedListener.run();
            }
        };
        this.output = new CraftingResultInventory();
        this.context = context;
        this.world = playerInventory.player.getWorld();
        this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 45));
        this.inputRockSlot = this.addSlot(new Slot(this.input, 1, 20, 23) {
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                WorkstationScreenHandler.this.populateResult();
            }
        });
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                ItemStack r = WorkstationScreenHandler.this.inputRockSlot.getStack();
                boolean bl1 = WorkstationScreenHandler.this.isInBounds(WorkstationScreenHandler.this.getSelectedRecipe()) &&
                        WorkstationScreenHandler.this.getAvailableRecipes().get(WorkstationScreenHandler.this.selectedRecipe.get()).value().matchesTool(r);
                return super.canTakeItems(playerEntity) && bl1;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (inputRockSlot.hasStack()) {
                    stack.onCraftByPlayer(player.getWorld(), player, stack.getCount());
                    ItemStack itemStack = WorkstationScreenHandler.this.inputSlot.takeStack(1);
                    if (!itemStack.isEmpty()) {
                        WorkstationScreenHandler.this.populateResult();
                    }

                    context.run((world, pos) -> {
                        long l = world.getTime();
                        if (WorkstationScreenHandler.this.lastTakeTime != l) {
                            var sh = WorkstationScreenHandler.this;
                            world.playSound((PlayerEntity) null, pos, sh.craftedSoundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            WorkstationScreenHandler.this.lastTakeTime = l;
                        }

                    });
                    super.onTakeItem(player, stack);
                }
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

        this.addProperty(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<RecipeEntry<WorkstationRecipe>> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && !this.availableRecipes.isEmpty() && this.inputRockSlot.hasStack();
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModItems.MORTAR.getLeft());
    }

    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
            this.populateResult();
        }

        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    public void onContentChanged(Inventory inventory) {
        if (inventory.equals(this.input)) {
            ItemStack itemStack = this.inputSlot.getStack();
            if (!itemStack.isOf(this.inputStack.getItem())) {
                this.inputStack = itemStack.copy();
                this.updateInput(inventory, itemStack);
            }
        }
        this.populateResult();
    }

    private void updateInput(Inventory input, ItemStack stack) {
        this.availableRecipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.setStack(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            Inventory i = new SimpleInventory(stack);
            this.availableRecipes = this.world.getRecipeManager().getAllMatches(RecipesUtil.MORTAR_RECIPE_TYPE, i, this.world);
        }
        populateResult();
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            RecipeEntry<WorkstationRecipe> stonecuttingRecipe = this.availableRecipes.get(this.selectedRecipe.get());
            if (stonecuttingRecipe.value().matchesTool(this.inputRockSlot.getStack()) && this.inputRockSlot.getStack() != ItemStack.EMPTY) {
                this.output.setLastRecipe(stonecuttingRecipe);
                this.craftedSoundEvent = stonecuttingRecipe.value().onCraftSound;
                this.outputSlot.setStack(stonecuttingRecipe.value().craft(this.input, this.world.getRegistryManager()));
            }
        } else {
            this.outputSlot.setStack(ItemStack.EMPTY);
        }

        this.sendContentUpdates();
    }

    public ScreenHandlerType<?> getType() {
        return RecipesUtil.WORKSTATION_SCREEN_HANDLER;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (index == 1) {
                item.onCraftByPlayer(itemStack2, player.getWorld(), player);
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index == 0) {
                if (!this.insertItem(itemStack2, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.world.getRecipeManager().getFirstMatch(RecipesUtil.MORTAR_RECIPE_TYPE, new SimpleInventory(itemStack2), this.world).isPresent()) {
                if (!this.insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!this.insertItem(itemStack2, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.insertItem(itemStack2, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }

            slot.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }

        return itemStack;
    }
    @Override
    public void onClosed (PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> {
            if (this.inputRockSlot.hasStack()) {
                player.giveItemStack(inputRockSlot.getStack());
            }
            this.dropInventory(player, this.input);
        });
    }

    public static class Factory {
        public static WorkstationScreenHandler create(int syncId, PlayerInventory playerInventory) {
            return new WorkstationScreenHandler(syncId, playerInventory);
        }
    }
}
