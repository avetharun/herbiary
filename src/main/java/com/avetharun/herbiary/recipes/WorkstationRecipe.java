package com.avetharun.herbiary.recipes;

import com.avetharun.herbiary.ModItems;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class WorkstationRecipe extends CuttingRecipe {
    public WorkstationRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, Identifier id, String group, Ingredient input, ItemStack output) {
        super(type, serializer, id, group, input, output);
    }
    public boolean consumeToolOnCraft = false;
    public Ingredient requiredTool;
    public SoundEvent onCraftSound = SoundEvents.UI_STONECUTTER_TAKE_RESULT;
    public WorkstationRecipe(Identifier id, String group, Ingredient input, Ingredient requiredTool, ItemStack output, SoundEvent onCraftSound, boolean consumeToolOnCraft) {
        super(RecipesUtil.MORTAR_RECIPE_TYPE, RecipesUtil.MORTAR_SERIALIZER, id, group, input, output);
        this.requiredTool = requiredTool;
        this.consumeToolOnCraft = consumeToolOnCraft;
        this.onCraftSound = onCraftSound;
    }
    public boolean matchesTool(ItemStack s) {
        boolean bl1 = false;
        for (ItemStack stack : requiredTool.getMatchingStacks()) {
            bl1 |= s.isOf(stack.getItem());
        }
        return bl1;
    }
    public boolean matches(Inventory inventory, World world) {
        return this.input.test(inventory.getStack(0));
    }

    public ItemStack createIcon() {
        return new ItemStack(ModItems.ROCK_ITEM);
    }

    public static class MortarRecipeSerializer<T extends WorkstationRecipe> implements RecipeSerializer<T> {
        final MortarRecipeSerializer.RecipeFactory<T> recipeFactory;

        public MortarRecipeSerializer(RecipeFactory<T> recipeFactory) {
            this.recipeFactory = recipeFactory;
        }
        public T read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Ingredient ingredient, requiredTool;
            Identifier sID = new Identifier(JsonHelper.getString(jsonObject, "craft_sound", "minecraft:ui.stonecutter.take_result"));
            SoundEvent e = Registries.SOUND_EVENT.get(sID);
            boolean consume = JsonHelper.getBoolean(jsonObject, "consume", false);
            System.out.println("consume? " + consume);
            if (JsonHelper.hasArray(jsonObject, "tool")) {
                requiredTool = Ingredient.fromJson(JsonHelper.getArray(jsonObject, "tool"));
            } else {
                requiredTool = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "tool"));
            }
            if (JsonHelper.hasArray(jsonObject, "tool")) {
                ingredient = Ingredient.fromJson(JsonHelper.getArray(jsonObject, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
            }

            String string2 = JsonHelper.getString(jsonObject, "result");
            int i = JsonHelper.getInt(jsonObject, "count");
            ItemStack itemStack = new ItemStack((ItemConvertible) Registries.ITEM.get(new Identifier(string2)), i);
            return this.recipeFactory.create(identifier, string, ingredient, requiredTool, itemStack, e, consume);
        }

        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient rtool = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();

            SoundEvent e = Registries.SOUND_EVENT.get(packetByteBuf.readIdentifier());
            boolean consume = packetByteBuf.readBoolean();
            return this.recipeFactory.create(identifier, string, ingredient, rtool, itemStack, e, consume);
        }

        public void write(PacketByteBuf packetByteBuf, T cuttingRecipe) {
            packetByteBuf.writeString(cuttingRecipe.group);
            cuttingRecipe.input.write(packetByteBuf);
            cuttingRecipe.requiredTool.write(packetByteBuf);
            packetByteBuf.writeItemStack(cuttingRecipe.output);
            packetByteBuf.writeIdentifier(cuttingRecipe.onCraftSound.getId());
            packetByteBuf.writeBoolean(cuttingRecipe.consumeToolOnCraft);
        }

        public interface RecipeFactory<T extends WorkstationRecipe> {
            T create(Identifier id, String group, Ingredient input, Ingredient tool, ItemStack output, SoundEvent onCraftSound, boolean consume);
        }
    }
}
