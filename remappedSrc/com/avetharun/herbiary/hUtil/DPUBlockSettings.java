package com.avetharun.herbiary.hUtil;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

@SuppressWarnings({"deprecation", "unused"})
public class DPUBlockSettings extends FabricBlockSettings {
    public DPUBlockSettings(Material material, MapColor color) {
        super(material, color);
    }
    public DPUBlockSettings(Material material, Function<BlockState, MapColor> mapColorProvider) {
        super(material, mapColorProvider);
    }
    public DPUBlockSettings(AbstractBlock.Settings settings) {
        super(settings);
    }
    public DPUBlockSettings withFunction(Identifier key, Consumer<Object> consumer) {
        LinkedHashMap<Identifier, Consumer<Object>> map = alib.getMixinField(this, "customdata");
        map.putIfAbsent(key, consumer);
        return this;
    }


    public static DPUBlockSettings of(Material material) {
        return of(material, material.getColor());
    }

    public static DPUBlockSettings of(Material material, MapColor color) {
        return new DPUBlockSettings(material, color);
    }

    public static DPUBlockSettings of(Material material, DyeColor color) {
        return new DPUBlockSettings(material, color.getMapColor());
    }

    public static DPUBlockSettings of(Material material, Function<BlockState, MapColor> mapColor) {
        return new DPUBlockSettings(material, mapColor);
    }

    public static DPUBlockSettings copyOf(AbstractBlock block) {
        return new DPUBlockSettings(((AbstractBlockAccessor) block).getSettings());
    }

    public static DPUBlockSettings copyOf(AbstractBlock.Settings settings) {
        return new DPUBlockSettings(settings);
    }

    public DPUBlockSettings noCollision() {
        super.noCollision();
        return this;
    }

    public DPUBlockSettings nonOpaque() {
        super.nonOpaque();
        return this;
    }

    public DPUBlockSettings slipperiness(float value) {
        super.slipperiness(value);
        return this;
    }

    public DPUBlockSettings velocityMultiplier(float velocityMultiplier) {
        super.velocityMultiplier(velocityMultiplier);
        return this;
    }

    public DPUBlockSettings jumpVelocityMultiplier(float jumpVelocityMultiplier) {
        super.jumpVelocityMultiplier(jumpVelocityMultiplier);
        return this;
    }
    public DPUBlockSettings sounds(BlockSoundGroup group) {
        super.sounds(group);
        return this;
    }

    /**
     * @deprecated Please use {@link DPUBlockSettings#luminance(ToIntFunction)}.
     */
    public DPUBlockSettings lightLevel(ToIntFunction<BlockState> levelFunction) {
        return this.luminance(levelFunction);
    }

    public DPUBlockSettings luminance(ToIntFunction<BlockState> luminanceFunction) {
        super.luminance(luminanceFunction);
        return this;
    }

    public DPUBlockSettings strength(float hardness, float resistance) {
        super.strength(hardness, resistance);
        return this;
    }

    public DPUBlockSettings breakInstantly() {
        super.breakInstantly();
        return this;
    }

    public DPUBlockSettings strength(float strength) {
        super.strength(strength);
        return this;
    }

    public DPUBlockSettings ticksRandomly() {
        super.ticksRandomly();
        return this;
    }

    public DPUBlockSettings dynamicBounds() {
        super.dynamicBounds();
        return this;
    }

    public DPUBlockSettings dropsNothing() {
        super.dropsNothing();
        return this;
    }

    public DPUBlockSettings dropsLike(Block block) {
        super.dropsLike(block);
        return this;
    }

    public DPUBlockSettings air() {
        super.air();
        return this;
    }

    public DPUBlockSettings allowsSpawning(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        super.allowsSpawning(predicate);
        return this;
    }

    public DPUBlockSettings solidBlock(AbstractBlock.ContextPredicate predicate) {
        super.solidBlock(predicate);
        return this;
    }

    public DPUBlockSettings suffocates(AbstractBlock.ContextPredicate predicate) {
        super.suffocates(predicate);
        return this;
    }

    public DPUBlockSettings blockVision(AbstractBlock.ContextPredicate predicate) {
        super.blockVision(predicate);
        return this;
    }

    public DPUBlockSettings postProcess(AbstractBlock.ContextPredicate predicate) {
        super.postProcess(predicate);
        return this;
    }

    public DPUBlockSettings emissiveLighting(AbstractBlock.ContextPredicate predicate) {
        super.emissiveLighting(predicate);
        return this;
    }

    /**
     * Make the block require tool to drop and slows down mining speed if the incorrect tool is used.
     */
    public DPUBlockSettings requiresTool() {
        super.requiresTool();
        return this;
    }

    public DPUBlockSettings mapColor(MapColor color) {
        super.mapColor(color);
        return this;
    }

    public DPUBlockSettings hardness(float hardness) {
        super.hardness(hardness);
        return this;
    }

    public DPUBlockSettings resistance(float resistance) {
        super.resistance(resistance);
        return this;
    }

    public DPUBlockSettings offset(AbstractBlock.OffsetType offsetType) {
        super.offset(offsetType);
        return this;
    }

    public DPUBlockSettings noBlockBreakParticles() {
        super.noBlockBreakParticles();
        return this;
    }


    public DPUBlockSettings requires(FeatureFlag... features) {
        super.requires(features);
        return this;
    }

    /* FABRIC ADDITIONS*/

    /**
     * @deprecated Please use {@link DPUBlockSettings#luminance(int)}.
     */
    @Deprecated
    public DPUBlockSettings lightLevel(int lightLevel) {
        this.luminance(lightLevel);
        return this;
    }

    public DPUBlockSettings luminance(int luminance) {
        this.luminance(ignored -> luminance);
        return this;
    }

    public DPUBlockSettings drops(Identifier dropTableId) {
        ((AbstractBlockSettingsAccessor) this).setLootTableId(dropTableId);
        return this;
    }

    /* FABRIC DELEGATE WRAPPERS */

    /**
     * @deprecated Please migrate to {@link DPUBlockSettings#mapColor(MapColor)}
     */
    @Deprecated
    public DPUBlockSettings materialColor(MapColor color) {
        return this.mapColor(color);
    }

    /**
     * @deprecated Please migrate to {@link DPUBlockSettings#mapColor(DyeColor)}
     */
    @Deprecated
    public DPUBlockSettings materialColor(DyeColor color) {
        return this.mapColor(color);
    }

    public DPUBlockSettings mapColor(DyeColor color) {
        return this.mapColor(color.getMapColor());
    }

    public DPUBlockSettings collidable(boolean collidable) {
        ((AbstractBlockSettingsAccessor) this).setCollidable(collidable);
        return this;
    }
}
