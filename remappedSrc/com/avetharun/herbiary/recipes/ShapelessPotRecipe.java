package com.avetharun.herbiary.recipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShapelessPotRecipe implements Recipe<Inventory> {
    protected RecipeType<?> type = RecipesUtil.POT_RECIPE_TYPE;
    protected Identifier id;
    private final CookingRecipeCategory category;
    protected String group;
    protected List<Ingredient> input;
    protected  ItemStack output;
    protected  float experience;
    protected int cookTime;

    public ShapelessPotRecipe(Identifier id, String group, CookingRecipeCategory category, List<Ingredient> input, ItemStack output, float experience, int cookTime) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.input = input;
        this.output = output;
        this.experience = experience;
        this.cookTime = cookTime;
        for (Ingredient i : input) {
            System.out.println(i.toJson());
        }
        if (type == null) {
            this.type = RecipesUtil.POT_RECIPE_TYPE;
        }

    }

    public boolean matches(Inventory craftingInventory, World world) {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 0; j < craftingInventory.size(); ++j) {
            ItemStack itemStack = craftingInventory.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, (IntList)null);
    }

    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    public boolean fits(int width, int height) {
        return true;
    }

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.addAll(this.input);
        return defaultedList;
    }

    public float getExperience() {
        return this.experience;
    }

    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    public String getGroup() {
        return this.group;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public Identifier getId() {
        return this.id;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesUtil.POT_SERIALIZER;
    }

    public RecipeType<?> getType() {
        return this.type;
    }

    public CookingRecipeCategory getCategory() {
        return this.category;
    }

    public static ShapelessPotRecipe create(Identifier id, String group, CookingRecipeCategory category, List<Ingredient> input, ItemStack output, float experience, int cookTime) {
        return new ShapelessPotRecipe(id, group, category, input, output, experience, cookTime);
    }
    static class PotCookingRecipeJsonProvider implements RecipeJsonProvider{
        private final Identifier recipeId;
        private final String group;
        private final CookingRecipeCategory category;
        private final List<Ingredient> inputs;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;
        private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;


        public static class Serializer<T extends ShapelessPotRecipe> implements RecipeSerializer<T> {
            private int cookingTime;
            private final ShapelessPotRecipe.PotCookingRecipeJsonProvider.Serializer.RecipeFactory<T> recipeFactory;

            public Serializer(ShapelessPotRecipe.PotCookingRecipeJsonProvider.Serializer.RecipeFactory<T> recipeFactory) {
                this.recipeFactory = recipeFactory;
            }


            private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
                DefaultedList<Ingredient> defaultedList = DefaultedList.of();

                for(int i = 0; i < json.size(); ++i) {
                    Ingredient ingredient = Ingredient.fromJson(json.get(i));
                    if (!ingredient.isEmpty()) {
                        defaultedList.add(ingredient);
                    }
                }

                return defaultedList;
            }

            public T read(Identifier identifier, JsonObject jsonObject) {
                System.out.println(jsonObject);
                String string = JsonHelper.getString(jsonObject, "group", "");
                DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
                if (defaultedList.isEmpty()) {
                    throw new JsonParseException("No ingredients for shapeless recipe");
                } else if (defaultedList.size() > 9) {
                    throw new JsonParseException("Too many ingredients for shapeless recipe");
                } else {
                    String string2 = JsonHelper.getString(jsonObject, "result");
                    Identifier identifier2 = new Identifier(string2);
                    ItemStack itemStack = new ItemStack(Registries.ITEM.getOrEmpty(identifier2).orElseThrow(() -> {
                        return new IllegalStateException("Item: " + string2 + " does not exist");
                    }));
                    float f = JsonHelper.getFloat(jsonObject, "experience", 0.0F);
                    int i = JsonHelper.getInt(jsonObject, "cookingtime", 0);
                    //noinspection unchecked
                    return (T) new ShapelessPotRecipe(identifier, string, CookingRecipeCategory.FOOD, defaultedList, itemStack, f, i);
                }
            }

            public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
                System.out.println("pain");
                String string = packetByteBuf.readString();
                CookingRecipeCategory cookingRecipeCategory = packetByteBuf.readEnumConstant(CookingRecipeCategory.class);
                int len = packetByteBuf.readVarInt();
                List<Ingredient> ingredient = new ArrayList<>();
                for (int i = 0; i < len; i++){
                    ingredient.add(Ingredient.fromPacket(packetByteBuf));
                }
                ItemStack itemStack = packetByteBuf.readItemStack();
                float f = packetByteBuf.readFloat();
                int i = packetByteBuf.readVarInt();
                //noinspection unchecked
                return (T) new ShapelessPotRecipe(identifier, string, cookingRecipeCategory, ingredient, itemStack, f, i);
            }

            public void write(PacketByteBuf packetByteBuf, T abstractCookingRecipe) {
                System.out.println("pain w");
                packetByteBuf.writeString(abstractCookingRecipe.group);
                packetByteBuf.writeEnumConstant(abstractCookingRecipe.getCategory());
                packetByteBuf.writeVarInt(abstractCookingRecipe.input.size());
                for(Ingredient ingredient : abstractCookingRecipe.input) {
                    ingredient.write(packetByteBuf);
                }
                packetByteBuf.writeItemStack(abstractCookingRecipe.output);
                packetByteBuf.writeFloat(abstractCookingRecipe.experience);
                packetByteBuf.writeVarInt(abstractCookingRecipe.cookTime);
            }
            public interface RecipeFactory<T extends ShapelessPotRecipe> {
                T create(Identifier id, String group, CookingRecipeCategory category, List<Ingredient> input, ItemStack output, float experience, int cookTime);
            }
        }


        public PotCookingRecipeJsonProvider(Identifier recipeId, String group, CookingRecipeCategory category, List<Ingredient> input, Item result, float experience, int cookingTime, Advancement.Builder advancementBuilder, Identifier advancementId, RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
            this.recipeId = recipeId;
            this.group = group;
            this.category = category;
            this.inputs = input;
            this.result = result;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.serializer = serializer;
        }

        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            Iterator<Ingredient> var7 = this.inputs.iterator();
            JsonArray iO = new JsonArray();
            while(var7.hasNext()) {
                Ingredient entry = var7.next();
                iO.add(entry.toJson());
            }
            json.addProperty("category", this.category.asString());
            json.add("ingredients", iO);
            json.addProperty("result", Registries.ITEM.getId(this.result).toString());
            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        public RecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Nullable
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
