package com.avetharun.herbiary.mixin.Item;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.HerbiarySoundEvents;
import com.avetharun.herbiary.block.LampBlock;
import com.avetharun.herbiary.packet.FlintIgniterIgnitePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;
import java.util.Random;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin extends Item {
    Random random = new Random(0);

    public FlintAndSteelMixin(Settings settings) {
        super(settings);
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
                world.playSound(playerEntity, blockPos, Herbiary.FLINT_FAIL, SoundCategory.BLOCKS, 0.7F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            } else {
                world.playSound(playerEntity, blockPos, Herbiary.FLINT_FAIL, SoundCategory.BLOCKS, 0.7F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            }

            p.write(buf);
            ClientPlayNetworking.send(Herbiary.IGNITER_IGNITE_PACKET_ID, buf);
        } else {
            stack.damage(1, playerEntity, (_p) -> {
                _p.sendToolBreakStatus(context.getHand());
            });
        }
        return ActionResult.SUCCESS;
    }
}
