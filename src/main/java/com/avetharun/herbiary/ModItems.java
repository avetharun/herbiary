package com.avetharun.herbiary;

import com.avetharun.herbiary.Enchants.fantasy.GracefulEnchant;
import com.avetharun.herbiary.Enchants.fantasy.ZipwingEnchant;
import com.avetharun.herbiary.Items.*;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import com.avetharun.herbiary.block.*;
import com.avetharun.herbiary.client.entity.TentSpawner;
import com.avetharun.herbiary.entity.block.BackpackBlockEntity;
import com.avetharun.herbiary.entity.block.FieldMouseBurrowBlockEntity;
import com.avetharun.herbiary.entity.block.ToolrackBlockEntity;
import com.avetharun.herbiary.hUtil.*;
import com.avetharun.herbiary.recipes.RecipesUtil;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ModItems {
    public static ArrayList<Block> RotLogGrowables = new ArrayList<>();

    // begin armor
//    public static final Item QUIVER = registerItem("quiver", new QuiverItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);
    public static final WolfHideArmorMaterial WOLF_HIDE_ARMOR_MATERIAL = new WolfHideArmorMaterial();
//    public static final Item WOLF_HIDE_TUNIC = registerItem("wolf_hide_tunic", new ArmorItem(WOLF_HIDE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()), ItemGroups.COMBAT);
//    public static final Item WOLF_HIDE_LEGGINGS = registerItem("wolf_hide_leggings", new ArmorItem(WOLF_HIDE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()), ItemGroups.COMBAT);
//    public static final Item WOLF_HIDE_BOOTS = registerItem("wolf_hide_boots", new ArmorItem(WOLF_HIDE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()), ItemGroups.COMBAT);
    // begin weapons

    public static final Item BONE_KNIFE = registerItem("bone_knife", new SwordItem(ToolMaterials.WOOD, 3, 1.8f, new FabricItemSettings()
            .maxDamage(32)
    ), Pair.of(ItemGroups.COMBAT, Items.WOODEN_SWORD));

    public static final Item METAL_KNIFE = registerItem("metal_knife", new SwordItem(ToolMaterials.STONE, 4, 1.6f, new FabricItemSettings()
            .maxDamage(48)
    ), Pair.of(ItemGroups.COMBAT, Items.IRON_SWORD));
    public static final Item LONGBOW = registerItem("longbow", new BowItem(new Item.Settings().maxDamage(480)), Pair.of(ItemGroups.COMBAT, Items.BOW));
    // begin tools

//    public static final ModBlockItem IRON_SKILLET = registerBlockAsCreativeOnlyWithItem(new HorizontalFacingBlock(FabricBlockSettings.create()) {
//        public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//            builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
//        }
//    }, "iron_skillet", ItemGroups.TOOLS, new FabricItemSettings().maxCount(1));
//
//    public static final ModBlockItem CERAMIC_SKILLET = registerBlockAsCreativeOnlyWithItem(new HorizontalFacingBlock(FabricBlockSettings.create()) {
//        public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//            builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
//        }
//    }, "ceramic_skillet", ItemGroups.TOOLS, new FabricItemSettings().maxCount(1));
//
//
//    public static final ModBlockItem CAMPFIRE_POT = registerBlockAsCreativeOnlyWithItem(new HorizontalFacingBlock(FabricBlockSettings.create()) {
//        public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//            builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
//        }
//    }, "campfire_pot", ItemGroups.TOOLS, new FabricItemSettings().maxCount(1));

    public static final Item LEATHER_FLASK = registerItem("leather_flask", new FlaskItem(new Item.Settings().maxCount(1)), Pair.of(ItemGroups.TOOLS, Items.BUCKET));
    public static final Item STONE_HATCHET = registerItem("stone_hatchet", new HatchetItem(ToolMaterials.STONE, 4, -3.5f, new Item.Settings().maxDamage(42)), Pair.of(ItemGroups.TOOLS, Items.STONE_AXE));
    public static final Item IRON_HATCHET = registerItem("iron_hatchet", new HatchetItem(ToolMaterials.IRON, 5, -2.5f, new Item.Settings().maxDamage(64)), Pair.of(ItemGroups.TOOLS, Items.IRON_AXE));
    public static final Item IRON_HAMMER = registerItem("iron_hammer", new PickaxeItem(ToolMaterials.IRON, 3, -2.5f, new Item.Settings().maxDamage(75)), Pair.of(ItemGroups.TOOLS, Items.IRON_PICKAXE));

    public static final Item AXE_MELTING_TEMPLATE = registerItem("axe_template", new Item(new Item.Settings().maxDamage(8)), Pair.of(ItemGroups.INGREDIENTS, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
    public static final Item PICKAXE_MELTING_TEMPLATE = registerItem("pickaxe_template", new Item(new Item.Settings().maxDamage(8)), Pair.of(ItemGroups.INGREDIENTS, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
    public static final Item SHOVEL_MELTING_TEMPLATE = registerItem("shovel_template", new Item(new Item.Settings().maxDamage(8)), Pair.of(ItemGroups.INGREDIENTS, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
    public static final Item SWORD_MELTING_TEMPLATE = registerItem("sword_template", new Item(new Item.Settings().maxDamage(8)), Pair.of(ItemGroups.INGREDIENTS, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
    public static final ModBlockItem SMALL_BACKPACK = registerExistingBlockWithExistingItem(new BackpackBlock(FabricBlockSettings.create().breakInstantly().dropsNothing()), new SmallBackpackItem(new Item.Settings().maxCount(1)),"small_backpack",ItemGroups.TOOLS);

    // begin items
    public static final Item BIRCH_BARK = registerItem("birch_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item OAK_BARK = registerItem("oak_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item JUNGLE_BARK = registerItem("jungle_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item DARK_OAK_BARK = registerItem("dark_oak_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item ACACIA_BARK = registerItem("acacia_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item CHERRY_BARK = registerItem("cherry_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item MANGROVE_BARK = registerItem("mangrove_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SPRUCE_BARK = registerItem("spruce_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item BONE_SEWING_NEEDLE = registerItem("bone_sewing_needle", new Item(new FabricItemSettings()), ItemGroups.TOOLS, Items.FLINT_AND_STEEL);

    public static final Item PLANT_FIBER_STRING = registerItem("plant_string", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, Items.STRING));
    public static final Item FLEECE_ITEM = registerItem("fleece", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, PLANT_FIBER_STRING));
    public static final Item BONE_SHARD = registerItem("bone_shard", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS, Items.BONE);
    public static final Item LARGE_BONE = registerItem("large_bone", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS, Items.BONE);
    public static final Item ANIMAL_FAT = registerItem("animal_fat", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item WOLF_HIDE = registerItem("wolf_hide", new Item(new Item.Settings()), Pair.of(ItemGroups.INGREDIENTS, Items.RABBIT_HIDE));
    public static final Item CLOTH = registerItem("cloth", new Item(new Item.Settings()), Pair.of(ItemGroups.INGREDIENTS, FLEECE_ITEM));
    public static final Item SINEW = registerItem("sinew", new Item(new Item.Settings()), Pair.of(ItemGroups.INGREDIENTS, Items.STRING));
    public static final Item ROCK_ITEM = registerItem("rock", new RockLikeItem(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, Items.GOLD_NUGGET));
    public static final Item HOT_ROCK_ITEM = registerItem("heated_rock", new HotItem(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, ROCK_ITEM));
    public static final Item HARDWOOD_STICK = registerItem("hardwood_stick", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, Items.STICK));
    public static final Item SHARPENED_ROCK_ITEM = registerItem("sharpened_rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item OBSIDIAN_ROCK_ITEM = registerItem("obsidian_rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SHARPENED_OBSIDIAN_ROCK_ITEM = registerItem("sharpened_obsidian_rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SHARPENED_OBSIDIAN_ARROW_ITEM = registerItem("sharpened_obsidian_arrow", new SharpenedObsidianArrowItem(new FabricItemSettings()), ItemGroups.COMBAT);
    public static final Item FLOUR_ITEM = registerItem("flour", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, Items.WHEAT));
    public static final Item SOOT_ITEM = registerItem("soot", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, Items.FIRE_CHARGE));
    public static final Item ASH_ITEM = registerItem("ash", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, Items.FIRE_CHARGE));
    public static final Item ELDERBERRY_JAM = registerItem("elderberry_jam", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).saturationModifier(1f).build())), ItemGroups.FOOD_AND_DRINK);

    // Begin crafting interfaces

    public static ModBlockItem MORTAR = registerExistingBlockWithItem(new MortarBlock(FabricBlockSettings.create().strength(.2f, 0)), "mortar", Pair.of(ItemGroups.FUNCTIONAL, Items.STONECUTTER));
    public static ModBlockItem CRAFTING_MAT = registerExistingBlockWithItem(new CraftingMat(FabricBlockSettings.create().sounds(BlockSoundGroup.CANDLE)), "crafting_mat", Pair.of(ItemGroups.FUNCTIONAL, Items.CRAFTING_TABLE));

    // Begin blocks


    public static final ModBlockItem RIVERBED_GRAVEL = registerExistingBlockWithItem(new SiftableBlock(FabricBlockSettings.create().strength(0.6F).sounds(BlockSoundGroup.GRAVEL)),"riverbed_gravel", ItemGroups.NATURAL);
    public static final ModBlockItem COOLED_ROCKS = registerExistingBlockWithItem(new RegistryOverrides.CoolableBlock.Heatable(FabricBlockSettings.copyOf(Blocks.COBBLESTONE).ticksRandomly()),"cooled_rocks", ItemGroups.FUNCTIONAL);
    static {
        ((RegistryOverrides.CoolableBlock.Heatable)COOLED_ROCKS.getLeft()).hotVariant = RegistryOverrides.MAGMA_BLOCK;
        RegistryOverrides.MAGMA_BLOCK.coldVariant = (RegistryOverrides.CoolableBlock.Heatable) COOLED_ROCKS.getLeft();
    }
    public static final ModBlockItem BOOK_MAT = registerExistingBlockWithItem(
            new BookMat(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision()),
            "book_mat", Pair.of(ItemGroups.FUNCTIONAL, Items.LECTERN), Pair.of(ItemGroups.REDSTONE, Items.LECTERN)
    );
    public static final ModBlockItem OIL_LAMP = registerExistingBlockWithItem(
            new LampBlock(FabricBlockSettings.create()
                    .nonOpaque().breakInstantly()
                    .luminance(LampBlock.STATE_TO_LUMINANCE)
            ), "oil_lamp", Pair.of(ItemGroups.FUNCTIONAL, Items.SOUL_LANTERN), Pair.of(ItemGroups.TOOLS, Items.CLOCK));
    public static final ModBlockItem ROT_MOSS = registerExistingBlockWithItem(new RotMossBlock(FabricBlockSettings.create().noCollision().nonOpaque()), "rot_moss", ItemGroups.NATURAL);
    public static final ModBlockItem ROT_LOG = registerExistingPillarBlockWithItem(new RottingLogBlockInstancer(FabricBlockSettings
            .create()
            .ticksRandomly()
            .strength(0.3f)
            .sounds(BlockSoundGroup.HANGING_ROOTS),
            RotLogGrowables
    ), "rot_log", ItemGroups.NATURAL);
    public static final ModBlockItem ROT_PLANKS = registerExistingBlockWithItem(new Block(
            FabricBlockSettings.create()
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.HANGING_ROOTS)
                    .dropsLike(ROT_LOG.getLeft())
    ),"rot_planks", ItemGroups.BUILDING_BLOCKS);
    public static final ModBlockItem ROT_STAIRS = registerExistingBlockWithItem(new StairsBlock(ROT_PLANKS.getLeft().getDefaultState(),
            FabricBlockSettings.create()
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.HANGING_ROOTS)
                    .dropsLike(ROT_LOG.getLeft())
    ),"rot_stairs", ItemGroups.BUILDING_BLOCKS);
   public static final ModBlockItem ROT_SLAB = registerExistingBlockWithItem(new SlabBlock(
            FabricBlockSettings.create()
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.HANGING_ROOTS)
                    .dropsLike(ROT_LOG.getLeft())
    ), "rot_slab",
   ItemGroups.BUILDING_BLOCKS);

    public static final ModBlockItem OYSTER_MUSHROOM = registerExistingBlockWithEdibleItemUnlockable(
                new SFPTreeMountedMushroom(
                        FabricBlockSettings.
                                create().
                                breakInstantly().
                                noCollision().
                                sounds(BlockSoundGroup.FUNGUS)
                ),
                "oyster_mushroom",
                ItemGroups.FOOD_AND_DRINK,
                new FabricItemSettings().food(new FoodComponent.Builder().hunger(3).build())
        );
    public static final ModBlockItem INKY_MUSHROOM = registerExistingBlockWithEdibleItemUnlockable(
            new FloorMountedMushroom(
                    FabricBlockSettings.
                            create().
                            breakInstantly().
                            noCollision().
                            sounds(BlockSoundGroup.FUNGUS)
            ),
            "inky_mushroom",
            ItemGroups.FOOD_AND_DRINK,
            new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).build())
    );
    public static final ModBlockItem DARK_HONEY_MUSHROOM = registerExistingBlockWithEdibleItemUnlockable(
            new SFPTreeMountedMushroom(
                    FabricBlockSettings.
                            create().
                            breakInstantly().
                            noCollision().
                            sounds(BlockSoundGroup.FUNGUS)
            ),
            "dark_honey_mushroom",
            ItemGroups.FOOD_AND_DRINK,
            new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).build())
    );
    public static final ModBlockItem AMANITA_MUSHROOM = registerExistingBlockWithEdibleItemUnlockable(
            new FloorMountedMushroom(
                    FabricBlockSettings.create().
                            breakInstantly().
                            noCollision().
                            sounds(BlockSoundGroup.FUNGUS)
            ),
            "amanita_mushroom",
            ItemGroups.FOOD_AND_DRINK,
            new Item.Settings().food(new FoodComponent.Builder().hunger(1).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 2), 1.0F).build()
    ));
    public static final ModBlockItem INKY_MUSHROOM_STEM = registerExistingBlockWithItem(
            new MushroomBlock(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).luminance(3).sounds(BlockSoundGroup.FUNGUS)),
            "inky_mushroom_stem",
            ItemGroups.BUILDING_BLOCKS
    );

    public static final ModBlockItem INKY_MUSHROOM_BLOCK = registerExistingBlockWithItem(
            new MushroomBlock(FabricBlockSettings.copyOf(Blocks.RED_MUSHROOM_BLOCK).luminance(3).sounds(BlockSoundGroup.FUNGUS)),
            "inky_mushroom_block",
            ItemGroups.BUILDING_BLOCKS
    );
    public static final ModBlockItem INKY_MUSHROOM_INK = registerExistingBlockWithItem(
            new HangingRootsBlock(FabricBlockSettings.copyOf(Blocks.ROOTED_DIRT).luminance(0).sounds(BlockSoundGroup.FUNGUS)),
            "inky_mushroom_ink",
            ItemGroups.BUILDING_BLOCKS
    );
    public static final ModBlockItem OYSTER_MUSHROOM_STEM = registerExistingBlockWithItem(
            new MushroomBlock(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).luminance(0).sounds(BlockSoundGroup.FUNGUS)),
            "oyster_mushroom_stem",
            ItemGroups.BUILDING_BLOCKS
    );
    public static final ModBlockItem OYSTER_MUSHROOM_BLOCK = registerExistingBlockWithItem(
            new MushroomBlock(FabricBlockSettings.copyOf(Blocks.RED_MUSHROOM_BLOCK).luminance(0).sounds(BlockSoundGroup.FUNGUS)),
            "oyster_mushroom_block",
            ItemGroups.BUILDING_BLOCKS
    );
    public static final ModBlockItem PINE_SAP = registerExistingBlockWithItemHerb(
            new SapBlock(
                    FabricBlockSettings.create()
                            .breakInstantly()
                            .sounds(BlockSoundGroup.CANDLE)
                            .nonOpaque()
                            .noCollision()
            ),
            "pine_sap",
            Pair.of(ItemGroups.INGREDIENTS, Items.SLIME_BALL)
    );
    public static final Item PITCH_ITEM = registerItem("pitch", new Item(new FabricItemSettings()), Pair.of(ItemGroups.INGREDIENTS, PINE_SAP.getRight()));
    public static final ModBlockItem HONEY_MUSHROOM = registerExistingBlockWithEdibleItemUnlockable(
            new SFPTreeMountedMushroom(
                    FabricBlockSettings.create().
                            breakInstantly().
                            noCollision().
                            sounds(BlockSoundGroup.FUNGUS)
            ),
            "honey_mushroom",
            ItemGroups.FOOD_AND_DRINK,
            new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).build())
    );
    public static ArrayList<Block> SpruceLogGrowables = new ArrayList<>();
    public static ModBlockItem BIRD_NEST_BLOCK;
    public static ModBlockItem OAK_BRANCH_BLOCK = registerExistingBlockWithItem(new PolarBranchBlock(FabricBlockSettings.create()
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque()
    ), "oak_branch",
            ItemGroups.SEARCH);
    public static ModBlockItem SPRUCE_BRANCH_BLOCK = registerExistingBlockWithItem(new PolarBranchBlock(FabricBlockSettings.create()
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque()
    ), "spruce_branch",
            ItemGroups.SEARCH);
    public static BlockEntityType<NestBlockEntity> BIRD_NEST_BLOCK_ENTITY;
    public static BlockEntityType<ToolrackBlockEntity> TOOLRACK_BLOCK_ENTITY;
    public static BlockEntityType<BackpackBlockEntity> BACKPACK_BLOCK_ENTITY;
    public static ModBlockItem FIELD_MOUSE_BURROW_BLOCK = registerExistingBlockWithItem(new FieldMouseBurrowBlock(FabricBlockSettings.create().noCollision().breakInstantly()), "field_mouse_burrow", ItemGroups.NATURAL);
    public static BlockEntityType<FieldMouseBurrowBlockEntity> FIELD_MOUSE_BURROW_BLOCK_ENTITY ;
    public static ModBlockItem DANDELION_HERB;
    public static ModBlockItem MINT_PLANT;
    public static ModBlockItem LAVENDER_PLANT;
    public static ModBlockItem COTTON_PLANT;
    public static ModBlockItem BLUEBERRY_PLANT;
    public static ModBlockItem CATTAIL_PLANT;
    public static ModBlockItem WILD_RASPBERRY_PLANT;
    public static ModBlockItem ELDERBERRY_PLANT;
    public static final ModBlockItem RICE_PLANT = registerExistingBlockWithItem(
                new CropBlockInstancer(
                        FabricBlockSettings.
                                create().
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
            ItemGroups.NATURAL
        );
        public static final Item RICE_BUNDLE = registerItem("rice_bundle", new Item(new FabricItemSettings()
                .food(new FoodComponent.Builder().hunger(8).saturationModifier(5).build())), ItemGroups.FOOD_AND_DRINK);

        public static final Item SPEAR_ITEM = registerItem("spear", new SpearItem(new FabricItemSettings()
                .maxDamage(120 /* 12 throws */)), Pair.of(ItemGroups.COMBAT, Items.TRIDENT));

    public static EntityType<HerbiarySpearItemEntity> SPEAR_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier("al_herbiary", "spear"), HerbiarySpearItemEntity.getEntityType());

    public static final Item TENT_SPAWNER = registerItem("tent_bag", new TentSpawner(new Item.Settings().maxCount(1)), Pair.of(ItemGroups.TOOLS, Items.SHEARS), Pair.of(ItemGroups.FUNCTIONAL, Items.PINK_BED));
    public static final Item SLEEPING_BAG = registerItem("sleeping_bag", new Item(new Item.Settings().maxCount(1)), Pair.of(ItemGroups.TOOLS, Items.SHEARS), Pair.of(ItemGroups.FUNCTIONAL, Items.PINK_BED));
    public static final ModBlockItem TOOLRACK_BLOCK = registerExistingBlockWithItem(new ToolrackBlock(FabricBlockSettings.create().noCollision().nonOpaque()), "tool_rack", ItemGroups.FUNCTIONAL);








    // begin fantasy
    public static final GracefulEnchant GRACEFUL_ENCHANTMENT = registerEnchant("graceful", new GracefulEnchant(Enchantment.Rarity.VERY_RARE));
    public static final ZipwingEnchant ZIPWING_ENCHANTMENT = registerEnchant("zipwing", new ZipwingEnchant(Enchantment.Rarity.VERY_RARE));




























    private static <T extends Enchantment> T registerEnchant(String name, T enchantment) {
        return Registry.register(Registries.ENCHANTMENT, name, enchantment);
    }
    private static ModBlockItem registerBlockWithItem(String name, float strength, RegistryKey<ItemGroup> group) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), new Block(FabricBlockSettings.create().strength(strength)));
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static PillarBlock createLogBlock(MapColor topMapColor, MapColor sideMapColor) {

        return new PillarBlock(AbstractBlock.Settings.create().mapColor((state) -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }

    private static ModBlockItem registerCreativeOnlyBlockWithItem(String name, RegistryKey<ItemGroup> group, FabricItemSettings itemSettings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), new Block(FabricBlockSettings.create()));
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, itemSettings));
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }

    private static ModBlockItem registerBlockAsCreativeOnlyWithItem(Block block, String name, RegistryKey<ItemGroup> group, FabricItemSettings itemSettings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new OnlyCreativePlaceItem(b, itemSettings));
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItem(Block block, String name, RegistryKey<ItemGroup> ... groups) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        Arrays.stream(groups).forEach(group -> {
            ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
                content.add(i);
            });
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItem(Block block, String name, Item afterThisItem, RegistryKey<ItemGroup>... groups) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        Arrays.stream(groups).forEach(group -> {
            ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
                content.addAfter(afterThisItem, i);
            });
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItem(Block block, String name, Pair<RegistryKey<ItemGroup>, Item>... groups) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        Arrays.stream(groups).forEach((pair) -> {
            ItemGroupEvents.modifyEntriesEvent(pair.key()).register(content -> {
                content.addAfter(pair.value(), i);
            });
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingPillarBlockWithItem(PillarBlock block, String name, RegistryKey<ItemGroup> ... groups) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        Arrays.stream(groups).forEach(group -> {
            ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
                content.add(i);
            });
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithExistingItem(Block block, Item item, String name, RegistryKey<ItemGroup>... groups) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), item);
        Arrays.stream(groups).forEach(group -> {
            ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
                content.add(i);
            });
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItemHerb(Block block, String name, RegistryKey<ItemGroup> group, Item.Settings settings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new OnlyCreativePlaceItem(b, settings).asItem());
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItemHerbUnlockable(Block block, String name, RegistryKey<ItemGroup> group, Item.Settings settings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new OnlyCreativePlaceItemUnlockable(b, settings).asItem());
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItemHerb(Block block, String name, RegistryKey<ItemGroup> group) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new OnlyCreativePlaceItem(b, new FabricItemSettings()).asItem());
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItemHerb(Block block, String name, Pair<RegistryKey<ItemGroup>,Item>... groups) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new OnlyCreativePlaceItem(b, new FabricItemSettings()).asItem());
        Arrays.stream(groups).forEach(group -> {
            ItemGroupEvents.modifyEntriesEvent(group.key()).register(content -> {
                content.addAfter(group.value(), i);
            });
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithEdibleItem(Block block, String name, RegistryKey<ItemGroup> group, Item.Settings settings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new EdibleBlock(b, settings).asItem());
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithEdibleItemUnlockable(Block block, String name, RegistryKey<ItemGroup> group, Item.Settings settings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new EdibleBlockUnlockable(b, settings).asItem());
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithEdibleItemHerb(Block block, String name, RegistryKey<ItemGroup> group, Item.Settings settings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new EdibleBlock(b, settings).asItem());
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static Item registerItem(String name, Item i) {
        return Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), i);
    }
    private static Block registerBlock(Block i,String name) {
        return Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), i);
    }
    private static Item registerItem(String name, Item i, RegistryKey<ItemGroup> group) {
        Item re_i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), i);
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(re_i);
        });
        return re_i;
    }
    private static Item registerItem(String name, Item i, Pair<RegistryKey<ItemGroup>,  Item>... groups) {
        Item re_i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), i);
        Arrays.stream(groups).forEach(registryKeyItemPair -> {
            ItemGroupEvents.modifyEntriesEvent(registryKeyItemPair.key()).register(content -> {
                content.addAfter(registryKeyItemPair.right(),re_i);
            });
        });
        return re_i;
    }
    private static Item registerItem(String name, Item i, RegistryKey<ItemGroup> group, Item afterThisItem) {
        Item re_i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), i);
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.addAfter(afterThisItem, re_i);
        });
        return re_i;
    }
    public static void registerModItemData() {
        SpruceLogGrowables.addAll(List.of(new Block[]{
                ModItems.HONEY_MUSHROOM.getLeft(),
                ModItems.OYSTER_MUSHROOM.getLeft()
        }));
        RotLogGrowables.addAll(List.of(new Block[]{
                ModItems.OYSTER_MUSHROOM.getLeft()
        }));
    }

    static {
        CATTAIL_PLANT = registerExistingBlockWithItemHerb(
                new PartiallySubmergedBlock(AbstractBlock.Settings.create()
                        .noCollision().breakInstantly().sounds(BlockSoundGroup.WET_GRASS).offset(AbstractBlock.OffsetType.XZ)),
                "cattail", ItemGroups.NATURAL);
        COTTON_PLANT = registerExistingBlockWithItemHerb(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                    (pos, world)-> {
                        for (int i = 0; i < world.getRandom().nextInt(4) + 1; i++) {
                            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), COTTON_PLANT.getRight().getDefaultStack()));
                        }
                    }
                ), "cotton", ItemGroups.NATURAL);
        LAVENDER_PLANT = registerExistingBlockWithItemHerb(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                        (pos, world)-> {
                            for (int i = 0; i < world.getRandom().nextInt(2) + 1; i++) {
                                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), LAVENDER_PLANT.getRight().getDefaultStack()));
                            }
                        }
                ), "lavender", ItemGroups.NATURAL);
        MINT_PLANT = registerExistingBlockWithItemHerb(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                        (pos, world) -> {
                            for (int i = 0; i < world.getRandom().nextInt(2) + 1; i++) {
                                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), MINT_PLANT.getRight().getDefaultStack()));
                            }
                        }
                        ),
                "mint_plant",
                ItemGroups.FOOD_AND_DRINK
        );
        DANDELION_HERB = registerExistingBlockWithItemHerb(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                        (pos, world)-> {
                            for (int i = 0; i < world.getRandom().nextInt(3) + 1; i++) {
                                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), DANDELION_HERB.getRight().getDefaultStack()));
                            }
                        },
                        true
                ), "dandelion_herb", ItemGroups.FOOD_AND_DRINK,

                new Item.Settings().food(new FoodComponent.Builder().hunger(1).alwaysEdible().build()));

        BLUEBERRY_PLANT = registerExistingBlockWithItemHerbUnlockable(
                new HerbBush(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                        (pos, world)-> world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), BLUEBERRY_PLANT.getRight().getDefaultStack()))
                ), "blueberries", ItemGroups.FOOD_AND_DRINK,
                new Item.Settings().food(new FoodComponent.Builder().hunger(1).build()));

        WILD_RASPBERRY_PLANT = registerExistingBlockWithItemHerbUnlockable(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                        (pos, world)-> {
                            for(int i = 0; i < 3 + world.random.nextBetween(1,4); i++ ) {world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), WILD_RASPBERRY_PLANT.getRight().getDefaultStack()));}
                        }
                ), "wild_raspberries", ItemGroups.FOOD_AND_DRINK,
                new Item.Settings().food(new FoodComponent.Builder().hunger(1).alwaysEdible().build()));

        ELDERBERRY_PLANT = registerExistingBlockWithItemHerbUnlockable(
                new HerbBush(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly().sounds(BlockSoundGroup.SWEET_BERRY_BUSH),
                        (pos, world)-> world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ELDERBERRY_PLANT.getRight().getDefaultStack()))
                ), "elderberry", ItemGroups.FOOD_AND_DRINK,
                new Item.Settings().food(new FoodComponent.Builder().hunger(1).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 2), 1.0F).build()));
        BIRD_NEST_BLOCK  = registerExistingBlockWithItemHerb(new NestBlock(
                FabricBlockSettings.create()
                        .nonOpaque()
                        .strength(0.4f)
                        .sounds(BlockSoundGroup.BAMBOO_SAPLING)
                        .noCollision()
                        .nonOpaque()
                        .ticksRandomly()
        ), "bird_nest", ItemGroups.SEARCH);
        BIRD_NEST_BLOCK_ENTITY  = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("al_herbiary", "bird_nest_block_entity"),
                FabricBlockEntityTypeBuilder.create(NestBlockEntity::new, BIRD_NEST_BLOCK.getLeft()).build()
        );
        TOOLRACK_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("al_herbiary", "toolrack_block_entity"),
                FabricBlockEntityTypeBuilder.create(ToolrackBlockEntity::new, TOOLRACK_BLOCK.getLeft()).build()
        );
//        CAMPFIRE_POT_BLOCK_ENTITY = Registry.register(
//                Registries.BLOCK_ENTITY_TYPE,
//                new Identifier("al_herbiary", "campfire_pot_block_entity"),
//                FabricBlockEntityTypeBuilder.create(CampfirePotBlockEntity::new, CAMPFIRE_POT.getLeft()).build()
//        );
        BACKPACK_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("al_herbiary", "backpack"),
                FabricBlockEntityTypeBuilder.create(BackpackBlockEntity::new, SMALL_BACKPACK.getLeft()).build()
        );
        FIELD_MOUSE_BURROW_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("al_herbiary", "field_mouse_burrow"),
                FabricBlockEntityTypeBuilder.create(FieldMouseBurrowBlockEntity::new, FIELD_MOUSE_BURROW_BLOCK.getLeft()).build()
        );
        // Begin transparent block texture instancing
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                DANDELION_HERB.getLeft(),
                LAVENDER_PLANT.getLeft(),
                PINE_SAP.getLeft(),
                COTTON_PLANT.getLeft(),
                BLUEBERRY_PLANT.getLeft(),
                ELDERBERRY_PLANT.getLeft(),
                WILD_RASPBERRY_PLANT.getLeft(),
                OIL_LAMP.getLeft(),
                ROT_MOSS.getLeft(),
                MINT_PLANT.getLeft(),
//                CAMPFIRE_POT.getLeft(),
                SMALL_BACKPACK.getLeft(),
                FIELD_MOUSE_BURROW_BLOCK.getLeft()
//                ,
//                CAMPFIRE_POT.getLeft(),
//                IRON_SKILLET.getLeft(),
//                CERAMIC_SKILLET.getLeft()
        );

        RecipesUtil.registerRecipeHandler();

        RockLikeItem.ROCK_CONVERSIONS.put(ModItems.ROCK_ITEM, ModItems.SHARPENED_ROCK_ITEM);
        HotItem.ITEM_CONVERSION.put(ModItems.HOT_ROCK_ITEM, ModItems.ROCK_ITEM);
    }
}
