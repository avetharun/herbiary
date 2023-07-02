package com.avetharun.herbiary.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlintIgniterIgnitePacket implements Packet<ServerPlayPacketListener> {
    public ItemStack stack;
    public BlockPos pos;
    public Hand hand;
    public boolean succeeded = false;
    public BlockHitResult result;
    public FlintIgniterIgnitePacket() {}
    public FlintIgniterIgnitePacket(PacketByteBuf buf) {
        if (buf.isReadable()) {
            pos = buf.readBlockPos();
            stack = buf.readItemStack();
            hand = Hand.valueOf(buf.readString());
            result = buf.readBlockHitResult();
            succeeded = buf.readBoolean();
        } else {
            System.out.println("Unable to read packet.");
        }
    }
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack);
        buf.writeString(hand.name());
        buf.writeBlockHitResult(result);
        buf.writeBoolean(succeeded);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {
    }

}
