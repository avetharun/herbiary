package com.avetharun.herbiary.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnlockItemNamePacket implements Packet<ClientPlayPacketListener> {
    // If true: will remove the unlockedTranslationKeys from the known keys. If it doesn't exist, will do nothing.
    // If false: will add the unlockedTranslationKeys to the known keys.
    public boolean forceLocked = false;
    public ArrayList<String> unlockedTranslationKeys = new ArrayList<>();
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(unlockedTranslationKeys.size());
        buf.writeBoolean(forceLocked);
        for (String key : unlockedTranslationKeys) {
            buf.writeString(key);
        }
    }
    public UnlockItemNamePacket(){}
    public UnlockItemNamePacket(PacketByteBuf buf) {
        int len = buf.readInt();
        forceLocked = buf.readBoolean();
        for (int i = 0; i < len; i++) {
            unlockedTranslationKeys.add(buf.readString());
        }
    }

    public void addAll(String ... trKeys) {
        unlockedTranslationKeys.addAll(Arrays.asList(trKeys));
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }
}
