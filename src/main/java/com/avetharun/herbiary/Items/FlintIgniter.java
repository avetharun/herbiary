package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.HerbiarySoundEvents;
import com.avetharun.herbiary.block.LampBlock;
import com.avetharun.herbiary.packet.FlintIgniterIgnitePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.lang.reflect.Method;
import java.util.Random;

public class FlintIgniter {
    public static void HandlePlacement(BlockPos pos, World world, PlayerEntity entity, Hand hand, BlockHitResult result, ItemStack stack) {
        BlockState blockState = world.getBlockState(pos);
        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState) && !LampBlock.canBeLit(blockState)) {
            BlockPos blockPos2 = pos.offset(result.getSide());
            if (AbstractFireBlock.canPlaceAt(world, blockPos2, entity.getHorizontalFacing())) {
                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
                world.setBlockState(blockPos2, blockState2, 11);
                world.emitGameEvent(entity, GameEvent.BLOCK_PLACE, pos);
                if (entity instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) entity, blockPos2, stack);
                    stack.damage(1, entity, (p) -> {
                        p.sendToolBreakStatus(hand);
                    });

                }

                return;
            }
        } else {
            world.playSound(entity, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(pos, (BlockState) blockState.with(Properties.LIT, true), 11);
            world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
        }
    }
}
