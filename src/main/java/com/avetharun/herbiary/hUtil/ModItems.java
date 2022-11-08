package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import com.avetharun.herbiary.Items.SpearItem;
import com.avetharun.herbiary.block.SFPTreeMountedMushroom;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;

import java.util.ArrayList;

public class ModItems {
        public static final GameRules.Key<GameRules.BooleanRule> CUSTOM_TREE_GROWTH =
                GameRuleRegistry.register("customTreesCanGenerate", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));
        public static final ModBlockItem OYSTER_MUSHROOM = registerExistingBlockWithEdibleItem(
                new SFPTreeMountedMushroom(
                        FabricBlockSettings.
                                of(Material.PLANT).
                                breakInstantly().
                                noCollision().
                                sounds(BlockSoundGroup.FUNGUS)
                ),
                "oyster_mushroom",
                ItemGroup.FOOD,
                new FabricItemSettings().food(new FoodComponent.Builder().hunger(3).build())
        );
        public static ArrayList<Block> RotLogGrowables = new ArrayList<>();
    public static final ModBlockItem ROT_LOG = registerExistingPillarBlockWithItem(new RottingLogBlockInstancer(FabricBlockSettings
            .of(Material.WOOD)
            .ticksRandomly()
            .strength(0.3f)
            .sounds(BlockSoundGroup.HANGING_ROOTS),
            RotLogGrowables
        ), "rot_log", ItemGroup.BUILDING_BLOCKS);

    public static final ModBlockItem RICE_PLANT = registerExistingBlockWithItem(
                new CropBlockInstancer(
                        FabricBlockSettings.
                                of(Material.PLANT).
                                breakInstantly().
                                noCollision().
                                sounds(BlockSoundGroup.CROP)
                                .ticksRandomly(),
                        true,
                        3,
                        8,
                        10,
                        3
                ),
                "rice_plant",
                ItemGroup.MISC
        );
        public static final Item RICE_BUNDLE = registerItem("rice_bundle", new Item(new FabricItemSettings()
                .group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(5).build())));

        public static final Item SPEAR_ITEM = registerItem("spear", new SpearItem(new FabricItemSettings()
                .group(ItemGroup.COMBAT).maxDamage(120 /* 12 throws */)));

    public static EntityType<HerbiarySpearItemEntity> SPEAR_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier("al_herbiary", "spear"), HerbiarySpearItemEntity.getEntityType());


    private static ModBlockItem registerBlockWithItem(String name, Material material, float strength, ItemGroup group) {
        Block b = Registry.register(Registry.BLOCK, new Identifier(Herbiary.MOD_ID, name), new Block(FabricBlockSettings.of(material).strength(strength)));
        Item i = Registry.register(Registry.ITEM, new Identifier(Herbiary.MOD_ID, name), new BlockItem(b, new FabricItemSettings().group(group)));
        return new ModBlockItem(b,i);
    }
    private static PillarBlock createLogBlock(MapColor topMapColor, MapColor sideMapColor) {

        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }

    private static ModBlockItem registerExistingBlockWithItem(Block block, String name, ItemGroup group) {
        Block b = Registry.register(Registry.BLOCK, new Identifier(Herbiary.MOD_ID, name), block);
        Item i = Registry.register(Registry.ITEM, new Identifier(Herbiary.MOD_ID, name), new BlockItem(b, new FabricItemSettings().group(group)));
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingPillarBlockWithItem(PillarBlock block, String name, ItemGroup group) {
        Block b = Registry.register(Registry.BLOCK, new Identifier(Herbiary.MOD_ID, name), block);
        Item i = Registry.register(Registry.ITEM, new Identifier(Herbiary.MOD_ID, name), new BlockItem(b, new FabricItemSettings().group(group)));
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItem(Block block, String name, ItemGroup group, FabricItemSettings settings) {
        Block b = Registry.register(Registry.BLOCK, new Identifier(Herbiary.MOD_ID, name), block);
        Item i = Registry.register(Registry.ITEM, new Identifier(Herbiary.MOD_ID, name), new BlockItem(b, settings));
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithEdibleItem(Block block, String name, ItemGroup group, FabricItemSettings settings) {
        Block b = Registry.register(Registry.BLOCK, new Identifier(Herbiary.MOD_ID, name), block);
        Item i = Registry.register(Registry.ITEM, new Identifier(Herbiary.MOD_ID, name), new EdibleBlock(b, settings.group(group)).asItem());
        return new ModBlockItem(b,i);
    }
    private static Item registerItem(String name, Item i) {
        return Registry.register(Registry.ITEM, new Identifier(Herbiary.MOD_ID, name), i);
    }
    public static void registerModItemData() {
        RotLogGrowables.add(OYSTER_MUSHROOM.getLeft());
    }
}
