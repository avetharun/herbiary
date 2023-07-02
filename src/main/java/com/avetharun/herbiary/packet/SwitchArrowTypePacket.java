package com.avetharun.herbiary.packet;

import com.avetharun.herbiary.Items.QuiverItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPacketListener;
import net.minecraft.network.packet.Packet;

public class SwitchArrowTypePacket implements Packet<PacketListener> {
    public QuiverItem.BowArrowType wantedArrowType = QuiverItem.BowArrowType.NORMAL;
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(wantedArrowType.ordinal());
    }

    @Override
    public void apply(PacketListener listener) {}
    public  SwitchArrowTypePacket(QuiverItem.BowArrowType wantedArrowType) {
        this.wantedArrowType = wantedArrowType;
    }
    public PacketByteBuf getBuf() {
        PacketByteBuf buf = PacketByteBufs.create();
        write(buf);
        return buf;
    }
    public SwitchArrowTypePacket(){}

    public SwitchArrowTypePacket(PacketByteBuf buf) {
        wantedArrowType = QuiverItem.BowArrowType.values()[buf.readVarInt()];
    }
}
