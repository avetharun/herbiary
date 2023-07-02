package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.block.SiftableBlock_GRAVEL;
import com.avetharun.herbiary.block.SiftableBlock_SAND;
import com.avetharun.herbiary.block.SpruceLogMushroomSpawnerLikeBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Shadow @Final public static Block CAMPFIRE;
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(ZILnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/CampfireBlock;"))
    private static CampfireBlock modifyBlock(boolean emitsParticles, int fireDamage, AbstractBlock.Settings settings) {
        settings.luminance(state -> state.get(Properties.LIT) && (state.get(Properties.AGE_4) > 0) ? 10 : 0);
        return new CampfireBlock(emitsParticles, fireDamage, settings);
    }
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/GravelBlock;"))
    private static GravelBlock modifyBlock(AbstractBlock.Settings settings) {
        return new SiftableBlock_GRAVEL(settings);
    }
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(ILnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/SandBlock;"))
    private static SandBlock modifyBlock(int col, AbstractBlock.Settings settings) {
        return new SiftableBlock_SAND(col, settings);
    }
    @Redirect(method = "<clinit>", at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/block/Blocks;createLogBlock(Lnet/minecraft/block/MapColor;Lnet/minecraft/block/MapColor;)Lnet/minecraft/block/PillarBlock;"))
    private static PillarBlock modifyLogBlock_SPRUCE(MapColor topMapColor, MapColor sideMapColor) {
        return new SpruceLogMushroomSpawnerLikeBlock(FabricBlockSettings.create().instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable().mapColor((state) -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor));
    }
    @Redirect(method = "<clinit>", at = @At(ordinal = 5, value = "INVOKE", target = "Lnet/minecraft/block/Blocks;createLogBlock(Lnet/minecraft/block/MapColor;Lnet/minecraft/block/MapColor;)Lnet/minecraft/block/PillarBlock;"))
    private static PillarBlock modifyLogBlock_DARK_OAK(MapColor topMapColor, MapColor sideMapColor) {
        return new SpruceLogMushroomSpawnerLikeBlock(FabricBlockSettings.create().instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable().mapColor((state) -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor));
    }
    @Redirect(method="<clinit>", at=@At(ordinal = 2, value="NEW", target = "(Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/PillarBlock;"))
    private static PillarBlock modifyFullWoodBlock_SPRUCE(AbstractBlock.Settings settings){
        return new SpruceLogMushroomSpawnerLikeBlock(FabricBlockSettings.create().instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable().mapColor(MapColor.SPRUCE_BROWN));
    }
    @Redirect(method="<clinit>", at=@At(ordinal = 7, value="NEW", target = "(Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/PillarBlock;"))
    private static PillarBlock modifyFullWoodBlock_DARK_OAK(AbstractBlock.Settings settings){
        return new SpruceLogMushroomSpawnerLikeBlock(FabricBlockSettings.create().instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable().mapColor(MapColor.BROWN));
    }
//    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/TorchBlock"))
//    private static TorchBlock modifyTorchBlock(AbstractBlock.Settings settings, ParticleEffect particle) {
//        settings.luminance(state -> 0);
//        return new TorchBlock(settings, particle);
//    }
}
