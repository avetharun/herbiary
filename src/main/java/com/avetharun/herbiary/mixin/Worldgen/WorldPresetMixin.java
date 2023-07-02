package com.avetharun.herbiary.mixin.Worldgen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldPresets.class)
public abstract class WorldPresetMixin {
    @Mixin(WorldPresets.Registrar.class)
    public static abstract class WorldPreset_RegistrarMixin{
        @Shadow protected abstract DimensionOptions createOverworldOptions(ChunkGenerator chunkGenerator);
        @Shadow protected abstract void register(RegistryKey<WorldPreset> key, DimensionOptions dimensionOptions);
        @Shadow protected abstract DimensionOptions createOverworldOptions(BiomeSource biomeSource, RegistryEntry<ChunkGeneratorSettings> chunkGeneratorSettings);
    }

    static {

    }
}
