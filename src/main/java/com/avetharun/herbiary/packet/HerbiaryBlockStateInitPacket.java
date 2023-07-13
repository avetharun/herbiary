package com.avetharun.herbiary.packet;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.client.HerbiaryClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HerbiaryBlockStateInitPacket implements Packet<ClientPlayPacketListener> {
    public NbtCompound blocks_out = new NbtCompound();
    public boolean allow_vanilla_breaking,allow_herb_placement, allow_mining_ores_and_stone = false;
    public final List<NbtCompound> blocks_nbt = new ArrayList<>();
    public NbtCompound createCompoundForBlock(Identifier b) {
        NbtCompound c =  new NbtCompound();
        c.putString("n",b.getNamespace());
        c.putString("i", b.getPath());
        return c;
    }
    public Identifier getIdentifierForCompoundSingle(@NotNull NbtCompound c) {
        return new Identifier(c.getString("n"), c.getString("i"));
    }
    public void appendBlock(Identifier i) {
        blocks_nbt.add(createCompoundForBlock(i));
    }
    public void clearBlocks() {blocks_nbt.clear();}

    public HerbiaryBlockStateInitPacket() {
        blocks_out = new NbtCompound();
    }

    public HerbiaryBlockStateInitPacket(PacketByteBuf buf) {
        if (buf.isReadable()) {
            blocks_out = buf.readNbt();
            this.allow_herb_placement = buf.readBoolean();
            this.allow_vanilla_breaking = buf.readBoolean();
            this.allow_mining_ores_and_stone = buf.readBoolean();
        } else {
            System.out.println("Unable to read packet.");
        }
    }
    public void write(PacketByteBuf buf) {
        blocks_out = new NbtCompound();
        blocks_out.putInt("l", blocks_nbt.size());
        for (int i = 0; i < blocks_nbt.size(); i++) {
            blocks_out.put("B"+((Integer)i).toString(), blocks_nbt.get(i));
        }
        buf.writeNbt(blocks_out);
        buf.writeBoolean(this.allow_herb_placement);
        buf.writeBoolean(this.allow_vanilla_breaking);
        buf.writeBoolean(this.allow_mining_ores_and_stone);
    }

    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        System.out.println("help.");

    }
    public NbtCompound getBlocksNBT() {return blocks_out;}
    public List<Identifier> getBlocksArray() {
        List<Identifier> ids = new ArrayList<>();
        NbtCompound c = blocks_out;
        if (c.contains("l")) {
            int l = c.getInt("l");
            for (int i = 0; i < l; i++) {
                NbtCompound t = c.getCompound("B"+((Integer) i).toString());
                ids.add(getIdentifierForCompoundSingle(t));
            }
        }
        return ids;
    }
}
