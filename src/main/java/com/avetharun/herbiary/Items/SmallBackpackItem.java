package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.block.BackpackBlockEntity;
import com.avetharun.herbiary.screens.BackpackScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class SmallBackpackItem extends Item {
    private static final String TAG_BACKPACK_ITEMS = "backpackItems";

    public SmallBackpackItem(Settings settings) {
        super(settings);
    }
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(World world, ItemStack stack, SimpleInventory backpackInventory) {
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new BackpackScreenHandler(syncId, playerInventory, backpackInventory, stack), this.getName());
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
            ItemStack stack = player.getStackInHand(hand);
            SimpleInventory backpackInventory = new SimpleInventory(19) {
                @Override
                public boolean isValid(int slot, ItemStack stack) {
                    return !stack.isOf(ModItems.SMALL_BACKPACK.getRight()) && super.isValid(slot, stack);
                }

                @Override
                public void onClose(PlayerEntity player) {
                    if (player instanceof ServerPlayerEntity) {
                        saveInventoryToNbt(stack, this.heldStacks);
                    }
                    super.onClose(player);
                }
            };
            if (stack.getNbt() != null) {
                NbtList tag = stack.getNbt().getList(TAG_BACKPACK_ITEMS, NbtList.COMPOUND_TYPE);
                for (int i = 0; i < tag.size(); i++) {
                    NbtCompound c = (NbtCompound) tag.get(i);
                    backpackInventory.setStack(i, ItemStack.fromNbt(c));
                }
            }

            player.openHandledScreen(createScreenHandlerFactory(world, stack, backpackInventory));
        if (!world.isClient()) {
            // Synchronize the inventory data with the client
            saveInventoryToNbt(stack, backpackInventory.heldStacks);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockHitResult r;

        try {
            Method m = ItemUsageContext.class.getDeclaredMethod("getHitResult");
            r = (BlockHitResult)m.invoke(context);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to put packet hit result");
        }
        if (context.getPlayer().isSneaking())  {
            BlockPos posToPlace = r.getBlockPos().offset(r.getSide());
            if (context.getWorld().isAir(posToPlace)) {
                BlockState bs = ModItems.SMALL_BACKPACK.getLeft().getDefaultState();
                context.getWorld().setBlockState(posToPlace, bs);
                if (context.getWorld().getBlockEntity(posToPlace, ModItems.BACKPACK_BLOCK_ENTITY).isEmpty()) {
                    BackpackBlockEntity be = new BackpackBlockEntity(posToPlace, bs);
                    context.getWorld().addBlockEntity(be);
                }
                ;
                var bpe = context.getWorld().getBlockEntity(posToPlace, ModItems.BACKPACK_BLOCK_ENTITY).get();
                bpe.setBackpack(context.getStack());
                context.getPlayer().setStackInHand(context.getHand(), ItemStack.EMPTY);
            }
        }
        return super.useOnBlock(context);
    }

    private static void saveInventoryToNbt(ItemStack stack, DefaultedList<ItemStack> stacks) {
        NbtList itemList = new NbtList();

        for (ItemStack itemStack : stacks) {
            NbtCompound itemTag = itemStack.writeNbt(new NbtCompound());
            itemList.add(itemTag);
        }
        stack.getOrCreateNbt().put(TAG_BACKPACK_ITEMS, itemList);
    }
}