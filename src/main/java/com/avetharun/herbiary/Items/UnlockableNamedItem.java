package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.client.HerbiaryClient;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.packet.UnlockItemNamePacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class UnlockableNamedItem extends Item {
    public UnlockableNamedItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return super.getName();
    }
    public static Text HandleName(ItemStack stack, Text _default) {

        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null && (!MinecraftClient.getInstance().player.isCreative())) {
            return HerbiaryClient.KnownPlants.contains(stack.getTranslationKey()) ? _default : Text.of("???");
        }
        return _default;
    }

    @Override
    public Text getName(ItemStack stack) {
        return UnlockableNamedItem.HandleName(stack, super.getName(stack));
    }
    public static void Unlock(ServerPlayerEntity player, boolean lockState, ItemStack... stacks) {
        UnlockItemNamePacket p = new UnlockItemNamePacket();
        PacketByteBuf buf = PacketByteBufs.create();
        p.forceLocked = lockState;
        for (ItemStack stack : stacks) {
            p.unlockedTranslationKeys.add(stack.getTranslationKey());
        }
        ArrayList<String> m_PlayerKnowns = alib.getMixinField(player, "knownIDs");
        if (!lockState) {
            m_PlayerKnowns.addAll(p.unlockedTranslationKeys);
        } else {
            m_PlayerKnowns.removeAll(p.unlockedTranslationKeys);
        }
        p.write(buf);
        ServerPlayNetworking.send(player, Herbiary.UNLOCK_ITEM_PACKET_ID, buf);
    }
}
