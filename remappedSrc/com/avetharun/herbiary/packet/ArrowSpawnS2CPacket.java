package com.avetharun.herbiary.packet;

import com.avetharun.herbiary.Items.QuiverItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;

public class ArrowSpawnS2CPacket extends EntitySpawnS2CPacket {
    public QuiverItem.BowArrowType arrowType;
    public boolean willCauseBleeding = false;
    public ArrowSpawnS2CPacket(Entity entity, QuiverItem.BowArrowType typeOfArrow, boolean willCauseBleeding, int entityData) {
        super(entity, entityData);
        this.arrowType = typeOfArrow;
        this.willCauseBleeding = willCauseBleeding;

    }

    public ArrowSpawnS2CPacket(PacketByteBuf buf) {
        super(buf);
        arrowType = QuiverItem.BowArrowType.values()[buf.readVarInt()];
        willCauseBleeding = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        super.write(buf);
        buf.writeVarInt(arrowType.ordinal());
    }
}

