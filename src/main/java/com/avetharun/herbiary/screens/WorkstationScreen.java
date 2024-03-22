package com.avetharun.herbiary.screens;

import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.recipes.WorkstationRecipe;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import io.wispforest.owo.ui.base.BaseOwoTooltipComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.client.gui.widget.Positioner;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WorkstationScreen extends HandledScreen<WorkstationScreenHandler> {

    @Environment(EnvType.CLIENT)
    public class ItemInTooltipComponent implements TooltipComponent {
        private static final Identifier BACKGROUND_TEXTURE = new Identifier("container/bundle/background");
        private static final int field_32381 = 4;
        private static final int field_32382 = 1;
        private static final int WIDTH_PER_COLUMN = 18;
        private static final int HEIGHT_PER_ROW = 20;
        private final DefaultedList<ItemStack> inventory;
        private final int occupancy;
        Text inline_text = Text.empty();
        public ItemInTooltipComponent(Text inlinedText, BundleTooltipData data) {
            this.inventory = data.getInventory();
            this.occupancy = data.getBundleOccupancy();
        }

        public int getHeight() {
            return this.getRowsHeight() + 4;
        }

        public int getWidth(TextRenderer textRenderer) {
            return this.getColumnsWidth();
        }

        private int getColumnsWidth() {
            return this.getColumns() * 18 + 2;
        }

        private int getRowsHeight() {
            return this.getRows() * 20 + 2;
        }

        @Override
        public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
            textRenderer.draw(this.inline_text, (float)x, (float)y, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }

        public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
            x+= textRenderer.getWidth(inline_text.asOrderedText());
            int i = this.getColumns();
            int j = this.getRows();
            boolean bl = this.occupancy >= 64;
            int k = 0;

            for(int l = 0; l < j; ++l) {
                for(int m = 0; m < i; ++m) {
                    int n = x + m * 18 + 1;
                    int o = y + l * 20 + 1;
                    this.drawSlot(n, o, k++, bl, context, textRenderer);
                }
            }

        }

        private void drawSlot(int x, int y, int index, boolean shouldBlock, DrawContext context, TextRenderer textRenderer) {
            if (index >= this.inventory.size()) {
            } else {
                ItemStack itemStack = (ItemStack)this.inventory.get(index);
                context.drawItem(itemStack, x + 1, y + 1, index);
                context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
            }
        }
        private int getColumns() {
            return 10;
        }

        private int getRows() {
            return 2;
        }
    }
    private static final Identifier TEXTURE = new Identifier("al_herbiary",  "textures/gui/container/workstation.png");
    private static final Identifier TOOL_TEXTURE = Identifier.of("al_herbiary", "textures/gui/container/tool_reference.png");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int RECIPE_LIST_COLUMNS = 4;
    private static final int RECIPE_LIST_ROWS = 3;
    private static final int RECIPE_ENTRY_WIDTH = 16;
    private static final int RECIPE_ENTRY_HEIGHT = 18;
    private static final int SCROLLBAR_AREA_HEIGHT = 54;
    private static final int RECIPE_LIST_OFFSET_X = 52;
    private static final int RECIPE_LIST_OFFSET_Y = 14;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;

    public WorkstationScreen(WorkstationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
        --this.titleY;
    }
    List<ItemStack> acceptedTools = new ArrayList<>();
    int toolIndex = 0;
    float lastToolSwap = 0f;
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        if (!hasTool) {
            if (!handler.inputSlot.hasStack()) {
                context.drawTexture(TOOL_TEXTURE, 20, 23, 0, 0, 16, 16, 16, 16);
            } else {
                if ((lastToolSwap += MinecraftClient.getInstance().getTickDelta()) > 100f) {
                    toolIndex++;
                    if (toolIndex == acceptedTools.size()) {
                        toolIndex = 0;
                    }
                    lastToolSwap = 0f;
                }
                if (acceptedTools.size() > 0) {
                    context.drawTexture(TEXTURE, 20, 23, 176, 23, 16, 16);
                    context.drawItem(acceptedTools.get(toolIndex), 20, 23);
                    context.drawTexture(TEXTURE, 20, 23, 176, 39, 16, 16); // overlay (slightly darker)
                }
            }

        }
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int k = (int)(41.0F * this.scrollAmount);
        context.drawTexture(TEXTURE, i + 119, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
        int l = this.x + 52;
        int m = this.y + 14;
        int n = this.scrollOffset + 12;
        this.renderRecipeBackground(context, mouseX, mouseY, l, m, n);
        this.renderRecipeIcons(context, l, m, n);
    }

    protected void drawMouseoverTooltip(DrawContext matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
        int i = this.x + 52;
        int j = this.y + 14;
        int k = this.scrollOffset + 12;
        List<RecipeEntry<WorkstationRecipe>> list = this.handler.getAvailableRecipes();
        int _i = 0;
        for(int l = this.scrollOffset; l < k && l < this.handler.getAvailableRecipeCount(); ++l, _i++) {
            assert this.client != null;
            assert this.client.world != null;
            var v = list.get(_i).value();
            ItemStack s = new ItemStack(v.getResult(this.client.world.getRegistryManager()).getItem());
            int m = l - this.scrollOffset;
            int n = i + m % 4 * 16;
            int o = j + m / 4 * 18 + 2;
            if (x >= n && x < n + 16 && y >= o && y < o + 18) {
                NbtCompound cmp = new NbtCompound();
                var lst =  new ArrayList<Text>();
                lst.add(Text.translatable(s.getTranslationKey()));
                if (v.requireHeat) {
                    lst.add(Text.literal("Requires heat ").formatted(Formatting.ITALIC, Formatting.GRAY).append("\uD83D\uDD25").formatted(Formatting.GOLD));
                }
                ArrayList<TooltipComponent> components = new ArrayList<>();
                if (!v.requiredTool.isEmpty()) {
                    lst.add(Text.literal("Can be crafted with the following tools:").formatted(Formatting.ITALIC, Formatting.GRAY));
                }
                for (var txt : lst) {
                    components.add(new OrderedTextTooltipComponent(txt.asOrderedText()));
                }
                if (!v.requiredTool.isEmpty()) {
                    var lst1 = DefaultedList.<ItemStack>of();
                    lst1.addAll(List.of(v.requiredTool.getMatchingStacks()));
                    components.add(new ItemInTooltipComponent(Text.of("Tools:"), new BundleTooltipData(lst1, acceptedTools.size())));
                }
                // This is NOT an error! Just the IDE being mean!
                matrices.drawTooltip(MinecraftClient.getInstance().textRenderer, components, x, y, HoveredTooltipPositioner.INSTANCE);
            }
        }
    }

    private void renderRecipeBackground(DrawContext matrices, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        List<RecipeEntry<WorkstationRecipe>> list = ((WorkstationScreenHandler)this.handler).getAvailableRecipes();
        for(int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
            assert this.client != null;
            assert this.client.world != null;

            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;
            int n = this.backgroundHeight;
            if (i == this.handler.getSelectedRecipe()) {
                n += 18;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + 16 && mouseY < m + 18) {
                n += 36;
            }
            matrices.drawTexture(TEXTURE, k, m - 1, 0, n, 16, 18);
        }

    }
    int recInp = 0;
    int recInp0 = 0;
    float recInpF = 0;
    private void renderRecipeIcons(DrawContext matrices, int x, int y, int scrollOffset) {
        List<RecipeEntry<WorkstationRecipe>> list = ((WorkstationScreenHandler)this.handler).getAvailableRecipes();
        recInpF += MinecraftClient.getInstance().getTickDelta();
        if (recInpF > 100) {
            recInp++;
        }
        for(int i = this.scrollOffset; i < scrollOffset && i < ((WorkstationScreenHandler)this.handler).getAvailableRecipeCount(); i++) {
            var entry = list.get(i).value();
            assert this.client != null;
            assert this.client.world != null;
            ItemStack s = entry.getResult(this.client.world.getRegistryManager()).copy();
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;
            matrices.push();
            matrices.drawItemInSlot(MinecraftClient.getInstance().textRenderer, s, k, m);
            matrices.pop();
//            matrices.drawItem(s,k-2,m-2);
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int i = this.x + 52;
            int j = this.y + 14;
            int k = this.scrollOffset + 12;
            int _i = 0;
            for(int l = this.scrollOffset; l < k; ++l, _i++) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double)(i + m % 4 * 16);
                double e = mouseY - (double)(j + m / 4 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0) {
                    assert this.client != null;
                    if (this.handler.onButtonClick(this.client.player, l)) {
                        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                        assert this.client.interactionManager != null;
                        this.client.interactionManager.clickButton(this.handler.syncId, l);
                        return true;
                    }
                }
            }

            i = this.x + 119;
            j = this.y + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 14;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)amount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 4;
        }

        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && this.handler.getAvailableRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return (this.handler.getAvailableRecipeCount() + 4 - 1) / 4 - 3;
    }
    boolean hasTool = false;
    private void onInventoryChange() {
        hasTool = this.handler.inputRockSlot.hasStack();
        this.canCraft = this.handler.canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0F;
            this.scrollOffset = 0;
        }
        acceptedTools.clear();
        toolIndex = 0;
        List<RecipeEntry<WorkstationRecipe>> list = this.handler.getAvailableRecipes();
        if (this.handler.inputSlot.hasStack()) {
            list.forEach(workstationRecipeRecipeEntry -> {
                for (Ingredient ingredient : workstationRecipeRecipeEntry.value().getIngredients()) {
                    if (ingredient.test(this.handler.inputSlot.getStack())) {
                        acceptedTools.addAll(Arrays.asList(workstationRecipeRecipeEntry.value().requiredTool.getMatchingStacks()));
                    }
                }
            });
        }

    }
}
