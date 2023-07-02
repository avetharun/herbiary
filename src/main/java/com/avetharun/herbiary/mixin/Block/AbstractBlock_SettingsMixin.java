package com.avetharun.herbiary.mixin.Block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

@Mixin(AbstractBlock.Settings.class)
public class AbstractBlock_SettingsMixin {
    public LinkedHashMap<Identifier, Consumer<Object>> customdata = new LinkedHashMap<>();
}
