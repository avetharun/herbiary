package com.avetharun.herbiary;


import com.avetharun.herbiary.block.LogBlock;
import com.feintha.regedit.RegistryEditEvent;
import com.feintha.regedit.RegistryManipulation;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

public class RegistryOverrides {

    private static Unsafe unsafe;
    RegistryOverrides(){
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    static void setFinalStatic(Field field, ImmutableMap value) {
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);
        unsafe.putObject(fieldBase, fieldOffset, value);
    }
    static void setFinal(BlockEntityType<?> instance, Field field, HashSet<Block> value) {
        long fieldOffset = unsafe.objectFieldOffset(field);
        unsafe.putObject(instance, fieldOffset, value);
    }

    static <T> Field getFieldForElement(Class parent, T element) throws IllegalAccessException {
        try {
            Field[] var2 = parent.getFields();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Field field = var2[var4];
                if (field.get((Object)null) == element) {
                    return field;
                }
            }

            return null;
        } catch (Exception var6) {
            return null;
        }
    }

    public static final CampfireBlock CAMPFIRE_BLOCK = new CampfireBlock(true, 3, AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).luminance(state -> state.get(Properties.LIT) && (state.get(Properties.AGE_4) > 0) ? 10 : 0).nonOpaque().burnable());

    public static final LogBlock OAK_LOG = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(Blocks.OAK_LOG).ticksRandomly());
    public static final LogBlock SPRUCE_LOG = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(Blocks.SPRUCE_LOG).ticksRandomly());
    public static final LogBlock ACACIA_LOG = new LogBlock(FabricBlockSettings.copyOf(Blocks.ACACIA_LOG));
    public static final LogBlock BIRCH_LOG = new LogBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LOG));
    public static final LogBlock JUNGLE_LOG = new LogBlock(FabricBlockSettings.copyOf(Blocks.JUNGLE_LOG));
    public static final LogBlock DARK_OAK_LOG = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(Blocks.DARK_OAK_LOG).ticksRandomly());
    public static final LogBlock CHERRY_LOG = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(Blocks.CHERRY_LOG).ticksRandomly());
    public static final LogBlock MANGROVE_LOG = new LogBlock(FabricBlockSettings.copyOf(Blocks.MANGROVE_LOG));
    public static final LogBlock OAK_WOOD = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(OAK_LOG).ticksRandomly());
    public static final LogBlock SPRUCE_WOOD = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(SPRUCE_LOG).ticksRandomly());
    public static final LogBlock ACACIA_WOOD = new LogBlock(FabricBlockSettings.copyOf(ACACIA_LOG));
    public static final LogBlock BIRCH_WOOD = new LogBlock(FabricBlockSettings.copyOf(BIRCH_LOG));
    public static final LogBlock JUNGLE_WOOD = new LogBlock(FabricBlockSettings.copyOf(JUNGLE_LOG));
    public static final LogBlock DARK_OAK_WOOD = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(DARK_OAK_LOG).ticksRandomly());
    public static final LogBlock CHERRY_WOOD = new LogBlock.SapProducingLog(FabricBlockSettings.copyOf(CHERRY_LOG).ticksRandomly());
    public static final LogBlock MANGROVE_WOOD = new LogBlock(FabricBlockSettings.copyOf(MANGROVE_LOG));

    public static final PillarBlock STRIPPED_OAK_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG).strength(4F, 7.0F));
    public static final PillarBlock STRIPPED_SPRUCE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_SPRUCE_LOG).strength(4.5F, 7.0F));
    public static final PillarBlock STRIPPED_ACACIA_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_ACACIA_LOG).strength(4.5F, 7.0F));
    public static final PillarBlock STRIPPED_BIRCH_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_BIRCH_LOG).strength(4.5F, 7.0F));
    public static final PillarBlock STRIPPED_JUNGLE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_JUNGLE_LOG).strength(4.5F, 7.0F));
    public static final PillarBlock STRIPPED_DARK_OAK_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_DARK_OAK_LOG).strength(4.5F, 7.0F));
    public static final PillarBlock STRIPPED_CHERRY_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_CHERRY_LOG).strength(4.5F, 7.0F));
    public static final PillarBlock STRIPPED_MANGROVE_LOG = new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_MANGROVE_LOG).strength(4.5F, 7.0F));


    public static final PillarBlock STRIPPED_OAK_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_OAK_LOG));
    public static final PillarBlock STRIPPED_SPRUCE_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_SPRUCE_LOG));
    public static final PillarBlock STRIPPED_ACACIA_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_ACACIA_LOG));
    public static final PillarBlock STRIPPED_BIRCH_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_BIRCH_LOG));
    public static final PillarBlock STRIPPED_JUNGLE_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_JUNGLE_LOG));
    public static final PillarBlock STRIPPED_DARK_OAK_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_DARK_OAK_LOG));
    public static final PillarBlock STRIPPED_CHERRY_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_CHERRY_LOG));
    public static final PillarBlock STRIPPED_MANGROVE_WOOD = new PillarBlock(FabricBlockSettings.copyOf(STRIPPED_MANGROVE_LOG));

    public static class CoolableBlock extends Block{
        public static class Heatable extends Block {
            public CoolableBlock hotVariant;
            public Heatable(Settings settings) {
                super(settings);
            }
            void incrementHeat(BlockState state, BlockPos pos, World world) {
                world.setBlockState(pos, state.with(Properties.AGE_4, Math.max(0, Math.min(Properties.AGE_4_MAX, state.get(Properties.AGE_4) + 1))));
            }
            void decrementHeat(BlockState state, BlockPos pos, World world) {
                world.setBlockState(pos, state.with(Properties.AGE_4, Math.max(0, Math.min(Properties.AGE_4_MAX, state.get(Properties.AGE_4) - 1))));
            }
            @Override
            protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
                super.appendProperties(builder.add(Properties.AGE_4));
            }

            public Heatable(Settings settings, CoolableBlock hotVariant) {
                super(settings);
                this.hotVariant = hotVariant;
            }
            @Override
            public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
                var s =  world.getBlockState(pos.down());
                if (s.isIn(BlockTags.CAMPFIRES) && s.get(Properties.LIT)) {
                    if (state.get(Properties.AGE_4) == Properties.AGE_4_MAX) {
                        world.setBlockState(pos, hotVariant.getDefaultState());
                        return;
                    }
                    incrementHeat(state, pos, world);
                } else {
                    decrementHeat(state, pos, world);
                }
                super.randomTick(state, world, pos, random);
            }
        }
        public Heatable coldVariant;
        public CoolableBlock(Settings settings) {
            super(settings);
        }
        public CoolableBlock(Settings settings, Heatable coldVariant) {
            super(settings);
            this.coldVariant = coldVariant;
        }

        void incrementCooling(BlockState state, BlockPos pos, World world) {
            world.setBlockState(pos, state.with(Properties.AGE_7, Math.max(0, Math.min(Properties.AGE_7_MAX, state.get(Properties.AGE_7) + 1))));
        }
        void decrementCooling(BlockState state, BlockPos pos, World world) {
            world.setBlockState(pos, state.with(Properties.AGE_7, Math.max(0, Math.min(Properties.AGE_7_MAX, state.get(Properties.AGE_7) - 1))));
        }
        @Override
        public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
            if (!entity.bypassesSteppingEffects() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
                entity.damage(world.getDamageSources().hotFloor(), 1.0F);
            }
            super.onSteppedOn(world, pos, state, entity);
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            super.appendProperties(builder.add(Properties.AGE_7));
        }

        @Override
        public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            var s =  world.getBlockState(pos.down());
            if (!s.isIn(BlockTags.CAMPFIRES) || !s.get(Properties.LIT)) {
                if (state.get(Properties.AGE_7) == Properties.AGE_7_MAX) {
                    world.setBlockState(pos, coldVariant.getDefaultState());
                    return;
                }
                incrementCooling(state, pos, world);
            } else {
                // heat back up!!
                decrementCooling(state, pos, world);
            }
            super.randomTick(state, world, pos, random);
        }
    }
    public static final CoolableBlock MAGMA_BLOCK = new CoolableBlock(FabricBlockSettings.copyOf(Blocks.MAGMA_BLOCK).ticksRandomly());

    static void OverrideBlockAndItem(RegistryManipulation.Manipulator manipulator, Block VANILLA_BLK, Item VANILLA_ITM, BlockItem CUSTOM) {
        manipulator.Redirect(Registries.BLOCK, VANILLA_BLK, CUSTOM.getBlock());
        manipulator.Redirect(Registries.ITEM, VANILLA_ITM, CUSTOM.asItem());
    }
    static void OverrideBlockWithEntityAndItem(RegistryManipulation.Manipulator manipulator, BlockEntityType<?> type, Block VANILLA_BLK, Item VANILLA_ITM, BlockItem CUSTOM){
        // This is NOT an error! Just the IDE being mean!
        var s = new HashSet<>(type.blocks);
        s.remove(VANILLA_BLK);
        s.add(CUSTOM.getBlock());
        manipulator.Redirect(Registries.BLOCK, VANILLA_BLK, CUSTOM.getBlock());
        manipulator.Redirect(Registries.ITEM, VANILLA_ITM, CUSTOM.asItem());
        try {
            setFinal(type, type.getClass().getDeclaredField("blocks"), s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static long sizeOf(Class<?> clazz) {
        long maximumOffset = 0;
        do {
            for (Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    maximumOffset = Math.max(maximumOffset, unsafe.objectFieldOffset(f));
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        return maximumOffset == 0 ? maximumOffset : maximumOffset + 8;
    }
    public static void Override() {
        new RegistryOverrides();
        RegistryEditEvent.EVENT.register(manipulator -> {
            OverrideBlockWithEntityAndItem(manipulator, BlockEntityType.CAMPFIRE, Blocks.CAMPFIRE, Items.CAMPFIRE, new BlockItem(CAMPFIRE_BLOCK, new Item.Settings()));
            /* Logs & Wood */ {
                OverrideBlockAndItem(manipulator, Blocks.OAK_LOG, Items.OAK_LOG, new BlockItem(OAK_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.SPRUCE_LOG, Items.SPRUCE_LOG, new BlockItem(SPRUCE_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.ACACIA_LOG, Items.ACACIA_LOG, new BlockItem(ACACIA_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.BIRCH_LOG, Items.BIRCH_LOG, new BlockItem(BIRCH_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.JUNGLE_LOG, Items.JUNGLE_LOG, new BlockItem(JUNGLE_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.DARK_OAK_LOG, Items.DARK_OAK_LOG, new BlockItem(DARK_OAK_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.CHERRY_LOG, Items.CHERRY_LOG, new BlockItem(CHERRY_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.MANGROVE_LOG, Items.MANGROVE_LOG, new BlockItem(MANGROVE_LOG, new Item.Settings()));

                OverrideBlockAndItem(manipulator, Blocks.OAK_WOOD, Items.OAK_WOOD, new BlockItem(OAK_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.SPRUCE_WOOD, Items.SPRUCE_WOOD, new BlockItem(SPRUCE_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.ACACIA_WOOD, Items.ACACIA_WOOD, new BlockItem(ACACIA_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.BIRCH_WOOD, Items.BIRCH_WOOD, new BlockItem(BIRCH_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.JUNGLE_WOOD, Items.JUNGLE_WOOD, new BlockItem(JUNGLE_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.DARK_OAK_WOOD, Items.DARK_OAK_WOOD, new BlockItem(DARK_OAK_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.CHERRY_WOOD, Items.CHERRY_WOOD, new BlockItem(CHERRY_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.MANGROVE_WOOD, Items.MANGROVE_WOOD, new BlockItem(MANGROVE_WOOD, new Item.Settings()));

                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_OAK_LOG, Items.STRIPPED_OAK_LOG, new BlockItem(STRIPPED_OAK_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_SPRUCE_LOG, Items.STRIPPED_SPRUCE_LOG, new BlockItem(STRIPPED_SPRUCE_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_ACACIA_LOG, Items.STRIPPED_ACACIA_LOG, new BlockItem(STRIPPED_ACACIA_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_BIRCH_LOG, Items.STRIPPED_BIRCH_LOG, new BlockItem(STRIPPED_BIRCH_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_JUNGLE_LOG, Items.STRIPPED_JUNGLE_LOG, new BlockItem(STRIPPED_JUNGLE_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_LOG, new BlockItem(STRIPPED_DARK_OAK_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_CHERRY_LOG, Items.STRIPPED_CHERRY_LOG, new BlockItem(STRIPPED_CHERRY_LOG, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_MANGROVE_LOG, Items.STRIPPED_MANGROVE_LOG, new BlockItem(STRIPPED_MANGROVE_LOG, new Item.Settings()));

                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_OAK_WOOD, Items.STRIPPED_OAK_WOOD, new BlockItem(STRIPPED_OAK_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_SPRUCE_WOOD, Items.STRIPPED_SPRUCE_WOOD, new BlockItem(STRIPPED_SPRUCE_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_ACACIA_WOOD, Items.STRIPPED_ACACIA_WOOD, new BlockItem(STRIPPED_ACACIA_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_BIRCH_WOOD, Items.STRIPPED_BIRCH_WOOD, new BlockItem(STRIPPED_BIRCH_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_JUNGLE_WOOD, Items.STRIPPED_JUNGLE_WOOD, new BlockItem(STRIPPED_JUNGLE_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_DARK_OAK_WOOD, Items.STRIPPED_DARK_OAK_WOOD, new BlockItem(STRIPPED_DARK_OAK_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_CHERRY_WOOD, Items.STRIPPED_CHERRY_WOOD, new BlockItem(STRIPPED_CHERRY_WOOD, new Item.Settings()));
                OverrideBlockAndItem(manipulator, Blocks.STRIPPED_MANGROVE_WOOD, Items.STRIPPED_MANGROVE_WOOD, new BlockItem(STRIPPED_MANGROVE_WOOD, new Item.Settings()));
                try {
                    setFinalStatic(AxeItem.class.getDeclaredField("STRIPPED_BLOCKS"),new ImmutableMap.Builder().put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD).put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD).put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG).put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK).build());
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
            OverrideBlockAndItem(manipulator, Blocks.MAGMA_BLOCK, Items.MAGMA_BLOCK, new BlockItem(MAGMA_BLOCK, new Item.Settings()));
        });
    }
}
