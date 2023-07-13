package com.avetharun.herbiary.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public abstract class CampfirePlacementScreenHandler extends ScreenHandler {
    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
    public NbtCompound saveCustomData(NbtCompound compound) {return compound;}
    protected CampfirePlacementScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }
}
