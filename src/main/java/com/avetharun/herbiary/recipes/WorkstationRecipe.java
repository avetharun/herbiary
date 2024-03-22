package com.avetharun.herbiary.recipes;

import com.avetharun.herbiary.ModItems;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class WorkstationRecipe extends CuttingRecipe {
    public WorkstationRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, Identifier id, String group, Ingredient input, ItemStack output) {
        super(type, serializer, group, input, output);
    }
    public boolean consumeToolOnCraft = false;
    public boolean requireHeat = false;
    public int toolDmgAmount = 0;
    public Ingredient requiredTool;
    public Identifier onCraftSound = SoundEvents.UI_STONECUTTER_TAKE_RESULT.getId();
    public WorkstationRecipe(String s, Ingredient ingredient, Optional<Ingredient> tool, Optional<Identifier> soundEvent, ItemStack stack, Optional<Boolean> aBoolean, Optional<Boolean> requiresHeat, Optional<Integer> toolDmg) {
        super(RecipesUtil.MORTAR_RECIPE_TYPE, RecipesUtil.MORTAR_SERIALIZER, s, ingredient, stack);
        this.requiredTool = tool.orElseGet(Ingredient::empty);
        this.consumeToolOnCraft = aBoolean.orElse(false);
        this.onCraftSound = soundEvent.orElse(SoundEvents.UI_STONECUTTER_TAKE_RESULT.getId());
        this.toolDmgAmount = toolDmg.orElse(0);
        this.requireHeat = requiresHeat.orElse(false);
    }

    public boolean matchesTool(ItemStack s) {
        boolean bl1 = false;
        return requiredTool.test(s);
    }
    public boolean matches(Inventory inventory, World world) {
        return this.ingredient.test(inventory.getStack(0));
    }

    public ItemStack createIcon() {
        return new ItemStack(ModItems.ROCK_ITEM);
    }

    public static class MortarRecipeSerializer<T extends WorkstationRecipe> implements RecipeSerializer<T> {
        final MortarRecipeSerializer.RecipeFactory<T> recipeFactory;

        public MortarRecipeSerializer(RecipeFactory<T> recipeFactory) {

            this.codec = RecordCodecBuilder.create((instance) -> {
                Products.P8<RecordCodecBuilder.Mu<T>, String, Ingredient, Optional<Ingredient>, Optional<Identifier>, ItemStack, Optional<Boolean>, Optional<Boolean>, Optional<Integer>> var10000 = instance.group(
                        Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter((recipe) -> recipe.group),
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.ingredient),
                        Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("tool").forGetter(o -> Optional.of(o.requiredTool)),
                        Identifier.CODEC.optionalFieldOf("result_sound").forGetter(o -> Optional.of(o.onCraftSound)),
                        ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter(o->{return o.result;}),
                        Codec.BOOL.optionalFieldOf("consume").forGetter(o -> Optional.of(o.consumeToolOnCraft)),
                        Codec.BOOL.optionalFieldOf("require_heat").forGetter(o -> Optional.of(o.requireHeat)),
                        Codec.INT.optionalFieldOf("damages").forGetter(o -> Optional.of(o.toolDmgAmount))
                );
                Objects.requireNonNull(recipeFactory);
                return var10000.apply(instance, recipeFactory::create);
            });
            this.recipeFactory = recipeFactory;
        }
        public T read(PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient rtool = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            Identifier e = packetByteBuf.readIdentifier();
            boolean consume = packetByteBuf.readBoolean();
            boolean heated = packetByteBuf.readBoolean();
            int dmg = packetByteBuf.readInt();
            return this.recipeFactory.create(string, ingredient, Optional.of(rtool), Optional.ofNullable(e), itemStack, Optional.of(consume), Optional.of(heated), Optional.of(dmg));
        }

        private final Codec<T> codec;
        @Override
        public Codec<T> codec() {
            return codec;
        }
        public void write(PacketByteBuf packetByteBuf, T cuttingRecipe) {
            packetByteBuf.writeString(cuttingRecipe.group);
            cuttingRecipe.ingredient.write(packetByteBuf);
            cuttingRecipe.requiredTool.write(packetByteBuf);
            packetByteBuf.writeItemStack(cuttingRecipe.result);
            packetByteBuf.writeIdentifier(cuttingRecipe.onCraftSound);
            packetByteBuf.writeBoolean(cuttingRecipe.consumeToolOnCraft);
            packetByteBuf.writeBoolean(cuttingRecipe.requireHeat);
            packetByteBuf.writeInt(cuttingRecipe.toolDmgAmount);
        }

        public interface RecipeFactory<T extends WorkstationRecipe> {
            T create(String group, Ingredient input, Optional<Ingredient> tool, Optional<Identifier> onCraftSound, ItemStack output, Optional<Boolean> consume, Optional<Boolean> requireHeat, Optional<Integer> toolDmg);
        }
    }
}
