package com.avetharun.herbiary.mixin.Other;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class RecipeCraftingMixin {
//    @Mixin(ShapedRecipe.class)
//    public static class ShapedEntryMixin {
//        @Inject(method = "matches(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Z", at=@At("HEAD"), cancellable = true)
//        void matchesMixin(Inventory inventory, World world, CallbackInfoReturnable<Boolean> cir){
//            world.getRecipeManager().values().forEach(recipeEntry -> {
//                if (recipeEntry.value() == (ShapedRecipe)(Object)this) {
//                    if (recipeEntry.id().getNamespace().equalsIgnoreCase("minecraft") && !world.getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_RECIPES_CRAFT)) {
//                        cir.setReturnValue(false);
//                    }
//                }
//            });
//        }
//    }
//    @Mixin(ShapelessRecipe.class)
//    public static class ShapelessEntryMixin{
//        @Inject(method = "matches(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Z", at=@At("HEAD"), cancellable = true)
//        void matchesMixin(Inventory inventory, World world, CallbackInfoReturnable<Boolean> cir){
//            world.getRecipeManager().values().forEach(recipeEntry -> {
//                if (recipeEntry.value() == (ShapelessRecipe)(Object)this) {
//                    if (recipeEntry.id().getNamespace().equalsIgnoreCase("minecraft") && !world.getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_RECIPES_CRAFT)) {
//                        cir.setReturnValue(false);
//                    }
//                }
//            });
//        }
//    }
}
