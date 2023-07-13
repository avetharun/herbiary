package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.hUtil.ModRegistries;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.screens.WorkstationScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static net.minecraft.block.CampfireBlock.SIGNAL_FIRE;

@SuppressWarnings("rawtypes")
@Mixin(CampfireBlock.class)
public abstract class CampfireMixin {
    @Shadow @Final public static BooleanProperty LIT;
    @Shadow @Final public static BooleanProperty WATERLOGGED;
    public void setBurnables(IntProperty prop) {}
    @Inject(method="isSignalFireBaseBlock", at=@At("HEAD"), cancellable = true)
    private void isSignalFireBaseBlockMixin(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(state.isIn(Herbiary.CAMPFIRE_SIGNAL_BLOCKS));
    }
    @Inject(method="canBeLit", at=@At("HEAD"), cancellable = true)
    private static void canBeLitMixin(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.isOf(Blocks.CAMPFIRE)) {
            boolean hasBurnable = state.get(Properties.AGE_4) > 0;
            cir.setReturnValue(hasBurnable && state.isIn(BlockTags.CAMPFIRES, (statex) -> {
                return statex.contains(WATERLOGGED) && statex.contains(LIT);
            }) && !(Boolean) state.get(WATERLOGGED) && !(Boolean) state.get(LIT));
        }
    }
    @Inject(method="randomDisplayTick", at=@At("HEAD"), cancellable = true)
    private void onDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.get(Properties.AGE_4) == 0 || !state.get(Properties.LIT)) {
            ci.cancel();
        }
    }
    @Inject(method="appendProperties", at=@At("RETURN"))
    private void appendPropertiesMixin(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(Properties.AGE_4, Properties.OCCUPIED);
    }
    private static final MethodHandle setDefaultStateMethod;

    static {
        try {
            setDefaultStateMethod = MethodHandles.lookup().findVirtual(Block.class, "setDefaultState", MethodType.methodType(void.class, BlockState.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get setDefaultState method", e);
        }
    }
    @Redirect(method = "<init>(ZILnet/minecraft/block/AbstractBlock$Settings;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;setDefaultState(Lnet/minecraft/block/BlockState;)V"))
    private void redirectSetDefaultState(CampfireBlock campfireBlock, BlockState state) {
        BlockState modifiedState = state.with(LIT, false).with(Properties.AGE_4, 0).with(Properties.OCCUPIED, false);
        try {
            setDefaultStateMethod.invokeExact((Block)campfireBlock, modifiedState);
        } catch (Throwable throwable) {
            throw new RuntimeException("Failed to invoke setDefaultState method", throwable);
        }
    }
    @Inject(method="onUse", at=@At("HEAD"), cancellable = true)
    private void onUseMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        CampfireBlockEntity campfireBlockEntity = (CampfireBlockEntity) world.getBlockEntity(pos);
        ItemStack itemStack = player.getStackInHand(hand);
        assert campfireBlockEntity != null;

        if (itemStack.isIn(Herbiary.ITEMS_THAT_BURN) && state.get(Properties.AGE_4) < 4) {
            if (!world.isClient) {
                alib.runPrivateMixinMethod(campfireBlockEntity, "addBurnItem", player, player.getAbilities().creativeMode ? itemStack.copy() : itemStack, 100);
                player.incrementStat(Stats.INTERACT_WITH_CAMPFIRE);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }


        if (itemStack.isIn(Herbiary.CAMPFIRE_PLACEABLE_ITEMS) && !state.get(Properties.OCCUPIED) && !player.isSneaking()) {
            alib.setMixinField(campfireBlockEntity, "occupation", itemStack.copyWithCount(1));
            if (!world.isClient) {
                itemStack.decrement(1);
                world.setBlockState(pos, state.with(Properties.OCCUPIED, true));
            }
            cir.setReturnValue(ActionResult.CONSUME);
            return;
        } else if (player.isSneaking() && state.get(Properties.OCCUPIED)) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(Properties.OCCUPIED, false));
                ItemStack s = alib.getMixinField(campfireBlockEntity, "occupation");
                if (s != null && s != ItemStack.EMPTY) {
                    alib.setMixinField(campfireBlockEntity, "occupation", ItemStack.EMPTY);
                    Vec3d p = pos.toCenterPos();
                    world.spawnEntity(new ItemEntity(world, p.x, p.y, p.z, s));
                }
            }
            cir.setReturnValue(ActionResult.CONSUME);
        } else if (!player.isSneaking() && state.get(Properties.OCCUPIED)) {
            ItemStack s = alib.getMixinField(campfireBlockEntity, "occupation");
            if (s != null && s != ItemStack.EMPTY && world.isClient) {
                Identifier rID = Registries.ITEM.getId(s.getItem());
                if (ModRegistries.CAMPFIRE_SCREENS.containsId(rID)) {
                    var e = ModRegistries.CAMPFIRE_SCREENS.get(rID);
                    assert e != null;
                    System.out.println(ModRegistries.CAMPFIRE_SCREENS.getId(e));
                    ScreenHandlerType ty = e.type;
                    assert e.type != null;
                    player.openHandledScreen(createScreenHandlerFactory(state, world, pos, s, rID, ty));
                }
                cir.setReturnValue(ActionResult.CONSUME);
            }
        }
        if (state.get(Properties.AGE_4) == 0) {
            cir.setReturnValue(ActionResult.PASS);
            cir.cancel();
        }
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos, ItemStack s, Identifier id, ScreenHandlerType ty) {
        String TITLE = "campfire."+id.toString().replace(':','.');
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> ty.create(syncId,playerInventory), Text.literal(TITLE));
    }

}