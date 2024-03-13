package com.avetharun.herbiary.mixin.Entity;

import com.avetharun.herbiary.Herbiary;
import com.google.common.collect.Lists;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
public abstract class RecipeMixin {
    @Shadow @Final public MinecraftServer server;

    @Shadow public abstract int unlockRecipes(Collection<Recipe<?>> recipes);


    @Shadow public abstract ServerWorld getServerWorld();

    // dirty hack to make it so only herbiary or non-minecraft recipes are unlocked.
//    @Inject(method = "unlockRecipes([Lnet/minecraft/util/Identifier;)V", at = @At("HEAD"), cancellable = true)
//    void unlock(Identifier[] ids, CallbackInfo ci) {
//        List<Recipe<?>> list = Lists.newArrayList();
//        int var4 = ids.length;
//        for (Identifier identifier : ids) {
//            Optional<? extends Recipe> recipe = this.server.getRecipeManager().get(identifier);
//            Objects.requireNonNull(list);
//            if (!identifier.getNamespace().equalsIgnoreCase("al_herbiary") && !this.getServerWorld().getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_RECIPES_UNLOCK)) {
//                continue;
//            }
//            recipe.ifPresent(list::add);
//        }
//        this.unlockRecipes(list);
//        ci.cancel();
//    }

}
