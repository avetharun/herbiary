package com.avetharun.herbiary.mixin.Rendering;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {

    @Shadow @Final private ScreenHandlerContext context;

    @Shadow public abstract boolean canUse(PlayerEntity player);

    @Shadow @Final private PlayerEntity player;


    @Shadow @Final private RecipeInputInventory input;

    @Inject(at=@At("HEAD"), cancellable = true, method = "canUse")
    void canUseInject(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        CraftingScreenHandler _this = ((CraftingScreenHandler) (Object)this);

        boolean o = context.get((world, pos) ->

                world.getBlockState(pos).isOf(Blocks.CRAFTING_TABLE) || world.getBlockState(pos).isOf(ModItems.CRAFTING_MAT.getLeft())
                        && player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0, true);

        cir.setReturnValue(o);
        cir.cancel();
    }
    @Inject(at=@At("HEAD"), cancellable = true, method="matches")
    void matchesInject(Recipe<RecipeInputInventory> recipe, CallbackInfoReturnable<Boolean> cir) {
        if (this.player.getWorld().getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_RECIPES_CRAFT)) {
            cir.setReturnValue(recipe.matches(input, this.player.getWorld()));
            cir.cancel();
            return;
        } else if (recipe.getId().getNamespace().equalsIgnoreCase("al_herbiary")) {
            cir.setReturnValue(recipe.matches(this.input, this.player.getWorld()));
            cir.cancel();
            return;
        }
        cir.setReturnValue(false);
        cir.cancel();
    }
    @Inject(method="updateResult", at=@At("HEAD"), cancellable = true)
    private static void updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingInventory, world);
            if (optional.isPresent()) {
                CraftingRecipe craftingRecipe = (CraftingRecipe)optional.get();
                if (craftingRecipe.getId().getNamespace().equalsIgnoreCase("al_herbiary") && !world.getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_RECIPES_CRAFT)) {
                    itemStack = craftingRecipe.craft(craftingInventory, world.getRegistryManager());
                } else if (world.getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_RECIPES_CRAFT) && resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    itemStack = craftingRecipe.craft(craftingInventory, world.getRegistryManager());
                }
            }

            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
        ci.cancel();
    }

}
