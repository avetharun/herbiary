package com.avetharun.herbiary;

import com.avetharun.herbiary.Items.*;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import com.avetharun.herbiary.block.*;
import com.avetharun.herbiary.client.entity.TentSpawner;
import com.avetharun.herbiary.entity.block.BackpackBlockEntity;
import com.avetharun.herbiary.entity.block.CampfirePotBlockEntity;
import com.avetharun.herbiary.entity.block.FieldMouseBurrowBlockEntity;
import com.avetharun.herbiary.entity.block.ToolrackBlockEntity;
import com.avetharun.herbiary.hUtil.*;
import com.avetharun.herbiary.recipes.RecipesUtil;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static ArrayList<Block> RotLogGrowables = new ArrayList<>();

    // begin armor
    public static final Item QUIVER = registerItem("quiver", new QuiverItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);
    public static final WolfHideArmorMaterial WOLF_HIDE_ARMOR_MATERIAL = new WolfHideArmorMaterial();
//    public static final Item WOLF_HIDE_TUNIC = registerItem("wolf_hide_tunic", new ArmorItem(WOLF_HIDE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()), ItemGroups.COMBAT);
//    public static final Item WOLF_HIDE_LEGGINGS = registerItem("wolf_hide_leggings", new ArmorItem(WOLF_HIDE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()), ItemGroups.COMBAT);
//    public static final Item WOLF_HIDE_BOOTS = registerItem("wolf_hide_boots", new ArmorItem(WOLF_HIDE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()), ItemGroups.COMBAT);
    // begin weapons

    public static final Item BONE_KNIFE = registerItem("bone_knife", new SwordItem(ToolMaterials.WOOD, 3, 1.8f, new FabricItemSettings()
            .maxDamage(32)
    ), ItemGroups.COMBAT);

    public static final Item METAL_KNIFE = registerItem("metal_knife", new SwordItem(ToolMaterials.STONE, 4, 1.6f, new FabricItemSettings()
            .maxDamage(48)
    ), ItemGroups.COMBAT);
    public static final Item LONGBOW = registerItem("longbow", new BowItem(new Item.Settings().maxDamage(480)));
    // begin tools

    public static final ModBlockItem IRON_SKILLET = registerExistingCreativeOnlyBlockWithItem(new CampfirePlaceableBlock(FabricBlockSettings.create(), Properties.HORIZONTAL_FACING), "iron_skillet", ItemGroups.TOOLS, new FabricItemSettings().maxCount(1));
    public static final ModBlockItem CERAMIC_SKILLET = registerExistingCreativeOnlyBlockWithItem(new CampfirePlaceableBlock(FabricBlockSettings.create(), Properties.HORIZONTAL_FACING), "ceramic_skillet", ItemGroups.TOOLS, new FabricItemSettings().maxCount(1));
    public static final ModBlockItem CAMPFIRE_POT = registerExistingCreativeOnlyBlockWithItem(new CampfirePlaceableBlock(FabricBlockSettings.create(), Properties.HORIZONTAL_FACING), "campfire_pot", ItemGroups.TOOLS, new FabricItemSettings().maxCount(1));
    public static final Item LEATHER_FLASK = registerItem("leather_flask", new FlaskItem(new Item.Settings().maxCount(1)), ItemGroups.TOOLS);
    public static final Item STONE_HATCHET = registerItem("stone_hatchet", new HatchetItem(ToolMaterials.STONE, 4, 1.8f, new Item.Settings().maxDamage(42)), ItemGroups.TOOLS);
    public static final Item IRON_HATCHET = registerItem("iron_hatchet", new HatchetItem(ToolMaterials.IRON, 5, 1.6f, new Item.Settings().maxDamage(64)), ItemGroups.TOOLS);
    public static final ModBlockItem SMALL_BACKPACK = registerExistingBlockWithExistingItem(new BackpackBlock(FabricBlockSettings.create().strength(-1.0F, 3600000.0F).dropsNothing()), new SmallBackpackItem(new Item.Settings().maxCount(1)),"small_backpack",ItemGroups.TOOLS);

    // begin items
    public static final Item WOLF_HIDE = registerItem("wolf_hide", new Item(new Item.Settings()), ItemGroups.INGREDIENTS);
    public static final Item CLOTH = registerItem("cloth", new Item(new Item.Settings()), ItemGroups.INGREDIENTS);
    public static final Item UNKNOWN_HERB = registerItem("unknown_herb", new Item(new Item.Settings()));
    public static final Item BIRCH_BARK = registerItem("birch_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item OAK_BARK = registerItem("oak_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SPRUCE_BARK = registerItem("spruce_bark", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item BONE_SEWING_NEEDLE = registerItem("bone_sewing_needle", new Item(new FabricItemSettings()), ItemGroups.TOOLS);


    public static final Item FLEECE_ITEM = registerItem("fleece", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item BONE_SHARD = registerItem("bone_shard", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item ANIMAL_FAT = registerItem("animal_fat", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item ROCK_ITEM = registerItem("rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SHARPENED_ROCK_ITEM = registerItem("sharpened_rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item OBSIDIAN_ROCK_ITEM = registerItem("obsidian_rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SHARPENED_OBSIDIAN_ROCK_ITEM = registerItem("sharpened_obsidian_rock", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SHARPENED_OBSIDIAN_ARROW_ITEM = registerItem("sharpened_obsidian_arrow", new SharpenedObsidianArrowItem(new FabricItemSettings()), ItemGroups.COMBAT);
    public static final Item FLOUR_ITEM = registerItem("flour", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item SOOT_ITEM = registerItem("soot", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item ASH_ITEM = registerItem("ash", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item ELDERBERRY_JAM = registerItem("elderberry_jam", new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).saturationModifier(1f).build())), ItemGroups.FOOD_AND_DRINK);

    // Begin crafting interfaces

    public static ModBlockItem MORTAR = registerExistingBlockWithItem(new MortarBlock(FabricBlockSettings.create().strength(.2f, 0)), "mortar", ItemGroups.FUNCTIONAL);
    public static ModBlockItem CRAFTING_MAT = registerExistingBlockWithItem(new CraftingMat(FabricBlockSettings.create().sounds(BlockSoundGroup.CANDLE)), "crafting_mat", ItemGroups.FUNCTIONAL);

    // Begin blocks\
    public static final ModBlockItem RIVERBED_GRAVEL = registerExistingBlockWithItem(new SiftableBlock(FabricBlockSettings.create().strength(0.6F).sounds(BlockSoundGroup.GRAVEL)),"riverbed_gravel", ItemGroups.NATURAL);

    public static final ModBlockItem SPRUCE_PLANTER = registerExistingBlockWithItem(new PlanterBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.WOOD)), "spruce_planter", ItemGroups.FUNCTIONAL);
    public static final ModBlockItem BOOK_MAT = registerExistingBlockWithItem(
            new BookMat(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision()),
            "book_mat", ItemGroups.FUNCTIONAL
    );
    public static final ModBlockItem OIL_LAMP = registerExistingBlockWithItem(
            new LampBlock(FabricBlockSettings.create()
                    .nonOpaque().breakInstantly()
                    .luminance(LampBlock.STATE_TO_LUMINANCE)
            ), "oil_lamp", ItemGroups.FUNCTIONAL);
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
    public static final ModBlockItem PINE_SAP = registerExistingBlockWithItemHerb(
            new TreeMountedPlane(
                    FabricBlockSettings.create()
                            .breakInstantly()
                            .sounds(BlockSoundGroup.CANDLE)
                            .nonOpaque()
                            .noCollision()
            ),
            "pine_sap",
            ItemGroups.INGREDIENTS
    );
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
    public static BlockEntityType<CampfirePotBlockEntity> CAMPFIRE_POT_BLOCK_ENTITY;
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
                .maxDamage(120 /* 12 throws */)), ItemGroups.COMBAT);

    public static EntityType<HerbiarySpearItemEntity> SPEAR_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier("al_herbiary", "spear"), HerbiarySpearItemEntity.getEntityType());

    public static final Item TENT_SPAWNER = registerItem("tent_bag", new TentSpawner(new Item.Settings().maxCount(1)), ItemGroups.TOOLS);
    public static final Item SLEEPING_BAG = registerItem("sleeping_bag", new Item(new Item.Settings().maxCount(1)), ItemGroups.TOOLS);
    public static final ModBlockItem TOOLRACK_BLOCK = registerExistingBlockWithItem(new ToolrackBlock(FabricBlockSettings.create().noCollision().nonOpaque()), "tool_rack", ItemGroups.FUNCTIONAL);


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

    private static ModBlockItem registerExistingCreativeOnlyBlockWithItem(Block block, String name, RegistryKey<ItemGroup> group, FabricItemSettings itemSettings) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new OnlyCreativePlaceItem(b, itemSettings));
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithItem(Block block, String name, RegistryKey<ItemGroup> group) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingPillarBlockWithItem(PillarBlock block, String name, RegistryKey<ItemGroup> group) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), new BlockItem(b, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
        });
        return new ModBlockItem(b,i);
    }
    private static ModBlockItem registerExistingBlockWithExistingItem(Block block, Item item, String name, RegistryKey<ItemGroup> group) {
        Block b = Registry.register(Registries.BLOCK, new Identifier("al_herbiary", name), block);
        Item i = Registry.register(Registries.ITEM, new Identifier("al_herbiary", name), item);
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> {
            content.add(i);
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
                new PickableBlock(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly(),
                    (pos, world)-> {
                        for (int i = 0; i < world.getRandom().nextInt(4) + 1; i++) {
                            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), COTTON_PLANT.getRight().getDefaultStack()));
                        }
                    }
                ), "cotton", ItemGroups.NATURAL);
        LAVENDER_PLANT = registerExistingBlockWithItemHerb(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().noCollision().ticksRandomly(),
                        (pos, world)-> {
                            for (int i = 0; i < world.getRandom().nextInt(2) + 1; i++) {
                                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), LAVENDER_PLANT.getRight().getDefaultStack()));
                            }
                        }
                ), "lavender", ItemGroups.NATURAL);
        MINT_PLANT = registerExistingBlockWithItemHerb(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().noCollision().ticksRandomly(),
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
                new PickableBlock(FabricBlockSettings.create().breakInstantly().noCollision().ticksRandomly(),
                        (pos, world)-> {
                            for (int i = 0; i < world.getRandom().nextInt(3) + 1; i++) {
                                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), DANDELION_HERB.getRight().getDefaultStack()));
                            }
                        },
                        true
                ), "dandelion_herb", ItemGroups.FOOD_AND_DRINK,

                new Item.Settings().food(new FoodComponent.Builder().hunger(1).alwaysEdible().build()));

        BLUEBERRY_PLANT = registerExistingBlockWithItemHerbUnlockable(
                new HerbBush(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly(),
                        (pos, world)-> world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), BLUEBERRY_PLANT.getRight().getDefaultStack()))
                ), "blueberries", ItemGroups.FOOD_AND_DRINK,
                new Item.Settings().food(new FoodComponent.Builder().hunger(1).build()));

        WILD_RASPBERRY_PLANT = registerExistingBlockWithItemHerbUnlockable(
                new PickableBlock(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly(),
                        (pos, world)-> {
                            for(int i = 0; i < 3 + world.random.nextBetween(1,4); i++ ) {world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), WILD_RASPBERRY_PLANT.getRight().getDefaultStack()));}
                        }
                ), "wild_raspberries", ItemGroups.FOOD_AND_DRINK,
                new Item.Settings().food(new FoodComponent.Builder().hunger(1).alwaysEdible().build()));

        ELDERBERRY_PLANT = registerExistingBlockWithItemHerbUnlockable(
                new HerbBush(FabricBlockSettings.create().breakInstantly().nonOpaque().noCollision().ticksRandomly(),
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
        CAMPFIRE_POT_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("al_herbiary", "campfire_pot_block_entity"),
                FabricBlockEntityTypeBuilder.create(CampfirePotBlockEntity::new, CAMPFIRE_POT.getLeft()).build()
        );
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
                CAMPFIRE_POT.getLeft(),
                SPRUCE_PLANTER.getLeft(),
                SMALL_BACKPACK.getLeft(),
                FIELD_MOUSE_BURROW_BLOCK.getLeft(),
                CAMPFIRE_POT.getLeft(),
                IRON_SKILLET.getLeft(),
                CERAMIC_SKILLET.getLeft()
        );

        RecipesUtil.registerRecipeHandler();

    }
}
