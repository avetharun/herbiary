package com.avetharun.herbiary.recipes;

import com.avetharun.herbiary.screens.CampfirePotScreenHandler;
import com.avetharun.herbiary.screens.WorkstationScreenHandler;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.*;
import net.minecraft.util.Identifier;

public class RecipesUtil{

    public static final Identifier MORTAR_ID = new Identifier("al_herbiary", "workstation");
    public static final Identifier POT_ID = new Identifier("al_herbiary", "pot");


    public static final Identifier HERB_UNLOCKER_ID = new Identifier("al_herbiary", "herb_unlocker");

    public static final Identifier HERB_BOOK_ID = new Identifier("al_herbiary", "herb_book");
    public static final RecipeType<WorkstationRecipe> MORTAR_RECIPE_TYPE= Registry.register(Registries.RECIPE_TYPE, MORTAR_ID, new RecipeType<WorkstationRecipe>() {
        public String toString() {
            return "workstation";
        }
    });
    public static final RecipeType<ShapelessPotRecipe> POT_RECIPE_TYPE= Registry.register(Registries.RECIPE_TYPE, POT_ID, new RecipeType<ShapelessPotRecipe>() {
        public String toString() {
            return "pot";
        }
    });
    public static ScreenHandlerType<WorkstationScreenHandler> WORKSTATION_SCREEN_HANDLER;
    public static ScreenHandlerType<CampfirePotScreenHandler> POT_SCREEN_HANDLER;
    public static final WorkstationRecipe.MortarRecipeSerializer<WorkstationRecipe> MORTAR_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, MORTAR_ID, new WorkstationRecipe.MortarRecipeSerializer<>(WorkstationRecipe::new));
    public static final ShapelessPotRecipe.PotCookingRecipeJsonProvider.Serializer<ShapelessPotRecipe> POT_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, POT_ID, new ShapelessPotRecipe.PotCookingRecipeJsonProvider.Serializer<>(ShapelessPotRecipe::create));


    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return (S) Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
    }
    public static void registerRecipeHandler() {

    }
}
