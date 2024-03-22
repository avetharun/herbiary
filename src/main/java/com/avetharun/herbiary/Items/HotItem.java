package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class HotItem extends Item {
    public HotItem(Settings settings) {
        super(settings);
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        super.postProcessNbt(nbt);
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        stack.getOrCreateNbt().putInt("timeUntilCooled", 20 * 20);
    }

    @Override
    public void onCraft(ItemStack stack, World world) {
        super.onCraft(stack, world);
        stack.getOrCreateNbt().putInt("timeUntilCooled", 20 * 20);
    }

    public static final HashMap<Item, Item> ITEM_CONVERSION = new HashMap<>();
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient) {return;}
        if (entity instanceof ServerPlayerEntity pe) {
            if (stack.getOrCreateNbt().contains("timeUntilCooled")) {
                if (stack.getOrCreateNbt().getInt("timeUntilCooled") <= 0) {
                    ((ServerPlayerEntity) entity).getInventory().setStack(slot, ITEM_CONVERSION.getOrDefault(stack.getItem(), Items.AIR).getDefaultStack());
                }
                // Assume we haven't spawned this from Creative (if we did, this item will never cool)
                stack.getOrCreateNbt().putInt("timeUntilCooled", stack.getOrCreateNbt().getInt("timeUntilCooled")-1);
            }
            if (!selected || (!pe.getStackInHand(Hand.MAIN_HAND).isIn(Herbiary.HEAT_BLOCKING_ITEMS) && !pe.getStackInHand(Hand.OFF_HAND).isIn(Herbiary.HEAT_BLOCKING_ITEMS))) {
                if (world.getServer().getTicks() % 20 == 0) {
                    entity.damage(entity.getDamageSources().onFire(), 0.5f);
                }
            }
        }
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("This item may burn you."));
        tooltip.add(Text.of("Try to hold leather in your other hand while holding this."));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
