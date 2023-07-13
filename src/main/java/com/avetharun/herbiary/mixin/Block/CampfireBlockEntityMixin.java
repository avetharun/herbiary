package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.CampfireBlockEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Stack;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Inject(method="toInitialChunkDataNbt",at=@At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    void toInitialChunkDataNbtMixin(CallbackInfoReturnable<NbtCompound> cir, NbtCompound nbtCompound) {
        NbtCompound c = new NbtCompound();
        this.occupation.writeNbt(c);
        nbtCompound.put("customData", customData);
        nbtCompound.put("occupation", c);
    }
    public NbtCompound customData = new NbtCompound();
    int ticksSinceBegunBurning = 0;
    Stack<ItemStack> itemsBeingBurned;
    public ItemStack occupation = ItemStack.EMPTY;
    @Inject(method="readNbt", at=@At("TAIL"))
    void readNbtMixin(NbtCompound nbt, CallbackInfo ci) {
        this.customData = nbt.getCompound("customData");
        this.occupation = ItemStack.fromNbt(nbt.getCompound("occupation"));
//        System.out.println(nbt);
        CampfireBlockEntity e = (CampfireBlockEntity) (Object)this;
        e.markDirty();
    }
    @Inject(method="writeNbt", at=@At("TAIL"))
    void writeNbtMixin(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound c = new NbtCompound();
        this.occupation.writeNbt(c);
        nbt.put("occupation", c);
    }
    int[] burnTimes;
    @Mutable @Shadow @Final private DefaultedList<ItemStack> itemsBeingCooked;

    @Shadow public abstract DefaultedList<ItemStack> getItemsBeingCooked();

    @Shadow public abstract void spawnItemsBeingCooked();

    @Shadow public abstract boolean addItem(@Nullable Entity user, ItemStack stack, int cookTime);

    @Shadow @Final private int[] cookingTotalTimes;

    @Inject(method="<init>", at=@At("TAIL"))
    private void initMixin(BlockPos pos, BlockState state, CallbackInfo ci){
        itemsBeingBurned = new Stack<>();
        for (int i = 0; i < state.get(Properties.AGE_4); i++) {
            itemsBeingBurned.push(Items.STICK.getDefaultStack());
        }
        CampfireBlockEntity e = (CampfireBlockEntity) (Object)this;
        e.markDirty();
    }

    private boolean addBurnItem(ServerPlayerEntity user, ItemStack stack, Integer cookTime) {
        CampfireBlockEntity e = (CampfireBlockEntity) (Object)this;
        if (itemsBeingBurned.size() < 4) {
            try {
                ticksSinceBegunBurning = 0;
                Method uL = BlockEntity.class.getDeclaredMethod("markDirty");
                this.itemsBeingBurned.push(stack.split(1));
                BlockPos pos = null;
                World world = null;
                BlockState oldState;
                world = e.getWorld();
                pos = e.getPos();
                oldState = e.getCachedState();
                oldState = oldState.with(Properties.AGE_4, MathHelper.clamp(this.itemsBeingBurned.size(),0,4));
                assert world != null;
                uL.invoke(e);
                world.setBlockState(pos, oldState);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(user, oldState));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }

            return true;
        }
        return false;
    }
    @Inject(method="clientTick", at=@At("HEAD"), cancellable = true)
    private static void onClientTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        if (state.get(Properties.AGE_4) == 0 || !state.get(Properties.LIT)) {
            ci.cancel();
        }
    }

    // TODO: make campfires burn nearby burnable blocks, with its factor being weighed by the amount of logs present.

    @SuppressWarnings("JavaReflectionMemberAccess")
    @Inject(method="litServerTick", at=@At("HEAD"))
    private static void litServerTickMixin(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {

        Field bt,ib;
        try {
            bt = CampfireBlockEntity.class.getDeclaredField("ticksSinceBegunBurning");
            ib = CampfireBlockEntity.class.getDeclaredField("itemsBeingBurned");
            bt.setAccessible(true);
            ib.setAccessible(true);

            Stack<ItemStack> iBurns = (Stack<ItemStack>) ib.get(campfire);
            if (iBurns.size() > 0) {
                if (bt.getInt(campfire) > (20*60) + (world.random.nextBetween(0,20)*20)) {
                    iBurns.pop();
                    bt.setInt(campfire, 0);
                }
                world.setBlockState(pos, state.with(Properties.AGE_4, iBurns.size()));
                bt.setInt(campfire, bt.getInt(campfire)+1);
            } else {
                bt.setInt(campfire, 0);
            }


            if (state.get(Properties.AGE_4) == 0) {
                world.setBlockState(pos, state.with(Properties.LIT, false));
            }


        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
