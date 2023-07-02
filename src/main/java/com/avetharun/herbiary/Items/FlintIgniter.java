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

public class FlintIgniter extends FlintAndSteelItem {
    Random random = new Random(0);
    public FlintIgniter(Settings settings) {
        super(settings);
    }
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
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        Vec3d m_Ppos = context.getHitPos();
        Vec3d p_H = playerEntity.getEyePos();
        Vec3d l_D = p_H.subtract(m_Ppos).multiply(-.02f);
        BlockState blockState = world.getBlockState(blockPos);
        world.addParticle(Herbiary.FLINT_SPARK, m_Ppos.x, m_Ppos.y, m_Ppos.z, 0, -.2f, 0);
        for (int i = 0; i < 10; i ++) {
            world.addParticle(Herbiary.FLINT_SPARK_SMOKE, p_H.x + l_D.x*5, p_H.y + l_D.y*5, p_H.z + l_D.z*5, l_D.x, l_D.y, l_D.z);
        }


        PacketByteBuf buf = PacketByteBufs.create();
        FlintIgniterIgnitePacket p = new FlintIgniterIgnitePacket();
        BlockHitResult result;
        BlockPos pos = context.getBlockPos();
        Hand hand = context.getHand();
        ItemStack stack = context.getStack();

        if (world.isClient) {
            try {
                Method m = ItemUsageContext.class.getDeclaredMethod("getHitResult");
                result = (BlockHitResult)m.invoke(context);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to put packet hit result");
            }

            p.result = result;
            p.pos = pos;
            p.hand = hand;
            p.stack = stack;
            if (random.nextInt(0, 100) > 85) {
                p.succeeded = true;
                world.playSound(playerEntity, blockPos, HerbiarySoundEvents.FLINT_FAIL, SoundCategory.BLOCKS, 0.7F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            } else {
                world.playSound(playerEntity, blockPos, HerbiarySoundEvents.FLINT_FAIL, SoundCategory.BLOCKS, 0.7F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            }

            p.write(buf);
            ClientPlayNetworking.send(Herbiary.IGNITER_IGNITE_PACKET_ID, buf);
        } else {

            assert playerEntity != null;
            stack.damage(1, playerEntity, (_p) -> {
                _p.sendToolBreakStatus(context.getHand());
            });
        }
        return ActionResult.SUCCESS;
    }
}
