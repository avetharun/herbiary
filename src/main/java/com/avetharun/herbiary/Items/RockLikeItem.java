package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.HashMap;

public class RockLikeItem extends Item {
    public RockLikeItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 75;
    }


    private HitResult getHitResult(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, (entity) -> {
            return !entity.isSpectator() && entity.canHit();
        }, (double)PlayerEntity.getReachDistance(user.isCreative()));
    }
    public static final HashMap<Item, Item> ROCK_CONVERSIONS = new HashMap<>();
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var  s = context.getWorld().getBlockState(context.getBlockPos()).isIn(Herbiary.SHARPENING_BLOCKS);
        if (!s) {return ActionResult.PASS;}
        PlayerEntity playerEntity = context.getPlayer();
        if (playerEntity != null && this.getHitResult(playerEntity).getType() == HitResult.Type.BLOCK) {
            playerEntity.setCurrentHand(context.getHand());
        }
        return super.useOnBlock(context);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks + 1;
        boolean bl = i % 10 == 5;
        if (bl) {
            if (!world.isClient) {
                ((ServerPlayerEntity)user).getHungerManager().addExhaustion(1);
            }
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BASALT_FALL, SoundCategory.PLAYERS);
        }
        if (i == getMaxUseTime(stack) && !world.isClient) {
            if (ROCK_CONVERSIONS.containsKey(stack.getItem())) {
                Item i1 = stack.getItem();
                stack.decrement(1);
                ((ServerPlayerEntity)user).giveItemStack(ROCK_CONVERSIONS.get(i1).getDefaultStack());
            }
            user.stopUsingItem();
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }
    static {
    }
}
