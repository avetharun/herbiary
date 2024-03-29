package com.avetharun.herbiary.hUtil;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class alib {
    public static float mod(float a, float b){
        return ((a % b) + b) % b;
    }
    // Class to automatically create an Entity Part (Similar to how an Ender Dragon does it)
    public static abstract class EntityPart<T extends Entity> extends Entity {
        public EntityPart(EntityType<?> type, T parent, World world) {
            super(type, world);
            this.parent = parent;
        }
        public T parent;
        @Override
        public boolean isPartOf(Entity entity) {
            return entity == this || entity == parent;
        }

        @MustBeInvokedByOverriders
        @Override
        public void tick() {
            super.tick();
            if (this.parent == null) {this.discard();}
        }
    }
    public static PointOfInterestType registerPOIType(RegistryKey<PointOfInterestType> key, Set<BlockState> states, int ticketCount, int searchDistance) {
        PointOfInterestType pointOfInterestType = new PointOfInterestType(states, ticketCount, searchDistance);
        Registry.register(Registries.POINT_OF_INTEREST_TYPE, key, pointOfInterestType);
        registerStates(Registries.POINT_OF_INTEREST_TYPE.entryOf(key), states);
        return pointOfInterestType;
    }
    private static void registerStates(RegistryEntry<PointOfInterestType> poiTypeEntry, Set<BlockState> states) {
        states.forEach((state) -> {
            RegistryEntry<PointOfInterestType> registryEntry2 = PointOfInterestTypes.POI_STATES_TO_TYPE.put(state, poiTypeEntry);
            if (registryEntry2 != null) {
                throw (IllegalStateException) Util.throwOrPause(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", state)));
            }
        });
    }
    public static Set<BlockState> getStatesOfBlock(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }
    public static boolean playerHasItem(ServerPlayerEntity player, ItemStack targetItem) {
        for (ItemStack itemStack : player.getInventory().main) {
            // Check if the item matches the target item type (ignoring NBT)
            if (itemStack.getItem() == targetItem.getItem() && itemStack.getItem() != Items.AIR) {
                return true;
            }
        }
        return false;
    }
    public static <F,T> F getMixinField(T mixinType, String fieldName) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            //noinspection unchecked
            return (F) f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F setMixinField(T mixinType, String fieldName, F value) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            f.set(mixinType, value);
            //noinspection unchecked
            return (F)f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F setPrivateMixinField(T mixinType, String fieldName, F value) {
        try {
            Field f = mixinType.getClass().getField(fieldName);
            f.setAccessible(true);
            f.set(mixinType, value);
            //noinspection unchecked
            return (F)f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <F,T> F getPrivateMixinField(T mixinType, String fieldName) {
        try {
            Field f = mixinType.getClass().getDeclaredField(fieldName);
            //noinspection unchecked
            return (F) f.get(mixinType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Checks if left is present and values equal in right.
    public static boolean checkNBTEquals(NbtCompound left, NbtCompound right) {
        if (left.getSize() == right.getSize() && left.getSize() == 0) {return true;}
        boolean bl = true;
        for (String key : left.getKeys()) {
            if (!bl || !right.contains(key)) {return false;}
            NbtElement e_L = left.get(key);
            NbtElement e_R = right.get(key);
            int elem_t = left.getType(key);
            if (right.getType(key) != elem_t) {
                return false;
            }
            if (left.get(key) instanceof NbtList lL && right.get(key) instanceof NbtList rL) {
                if (lL.size() != rL.size()) {return false;}
                int i = 0;
                for (NbtElement e : lL.subList(0, lL.size())) {
                    if (!e.asString().contentEquals(rL.get(i).asString())){
                        return false;
                    }
                    i++;
                }
            }

            if (e_L instanceof AbstractNbtNumber numL && e_R instanceof AbstractNbtNumber numR) {
                switch (elem_t) {
                    case NbtType.INT -> bl = numL.intValue() == numR.intValue();
                    case NbtType.SHORT -> bl = numL.shortValue() == numR.shortValue();
                    case NbtType.BYTE -> bl = numL.byteValue() == numR.byteValue();
                    case NbtType.FLOAT -> bl = numL.floatValue() == numR.floatValue();
                    case NbtType.DOUBLE -> bl = numL.doubleValue() == numR.doubleValue();
                    case NbtType.LONG -> bl = numL.longValue() == numR.longValue();
                }
            }
            switch (elem_t) {
                case NbtType.COMPOUND -> bl = checkNBTEquals(left.getCompound(key), right.getCompound(key));
                case NbtType.STRING -> bl = e_L.asString().contentEquals(e_R.asString());
            }
        }
        return bl;
    }

    public static NbtCompound json2NBT(JsonObject jsonObject) {
        NbtCompound nbtCompound = new NbtCompound();

        for (var entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();

            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    if (primitive.getAsNumber() instanceof Integer i) {
                        nbtCompound.putInt(key, i);
                    } else if (primitive.getAsNumber() instanceof Float i) {
                        nbtCompound.putFloat(key, i);
                    } else if (primitive.getAsNumber() instanceof Double i) {
                        nbtCompound.putDouble(key, i);
                    } else if (primitive.getAsNumber() instanceof Short i) {
                        nbtCompound.putShort(key, i);
                    }
                } else if (primitive.isBoolean()) {
                    nbtCompound.putBoolean(key, primitive.getAsBoolean());
                } else if (primitive.isString()) {
                    nbtCompound.putString(key, primitive.getAsString());
                }
                // Add more conversions as needed for other primitive types
            } else if (jsonElement.isJsonObject() || jsonElement.isJsonArray()) {
                NbtCompound nestedCompound = json2NBT(jsonElement.getAsJsonObject());
                nbtCompound.put(key, nestedCompound);
            }
            // Add more conversions as needed for other types like arrays or nested objects
        }

        return nbtCompound;
    }

    public static <T> void runMixinMethod(T mixinType, String methodName, Object... args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            Method f;
            if (args.length == 0) {
                f = mixinType.getClass().getDeclaredMethod(methodName);
                f.invoke(mixinType);
            } else {
                f = mixinType.getClass().getDeclaredMethod(methodName, argTypes);
                f.invoke(mixinType, args);
            }
            if (args.length == 0) {
                f.invoke(mixinType);
                return;
            }
            f.invoke(mixinType, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> void runPrivateMixinMethod(T mixinType, String methodName, Object... args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            Method f;
            if (args.length == 0) {
                f = mixinType.getClass().getDeclaredMethod(methodName);
                f.setAccessible(true);
                f.invoke(mixinType);
            } else {
                f = mixinType.getClass().getDeclaredMethod(methodName, argTypes);
                f.setAccessible(true);
                f.invoke(mixinType, args);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public static float getRandomFloat(Random random, float minValue, float maxValue) {
        return minValue + random.nextFloat() * (maxValue - minValue);
    }
    public static <T extends Entity> List<T> getEntitiesOfTypeInRange(World world, BlockPos pos, double range, EntityType<T> type) {
        Predicate<Entity> filter = entity -> entity.getType() == type && entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= range * range;
        return world.getEntitiesByType(type, Box.of(Vec3d.of(pos),range, range, range), filter);
    }
    public static boolean isEntityNearBlock(Entity e, int radius, Block... blocks) {
        // Get the entity's position
        BlockPos entityPos = e.getBlockPos();

        // Check if any nearby block is a target block type
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = entityPos.add(x, y, z);
                    Block block = e.getWorld().getBlockState(pos).getBlock();
                    for (Block b : blocks) {
                        System.out.println("check");
                        if (block == b) {
                            return true;
                        }
                    }
                }
            }
        }

        // No nearby block is a target block type
        return false;
    }
    /**
     * Calculate a 64 bits hash by combining CRC32 with Adler32.
     *
     * @param bytes a byte array
     * @return a hash number
     */
    public static long getHash64(byte[] bytes) {

        CRC32 crc32 = new CRC32();
        Adler32 adl32 = new Adler32();

        crc32.update(bytes);
        adl32.update(bytes);

        long crc = crc32.getValue();
        long adl = adl32.getValue();
        return ((crc << 32) | adl) + crc << 8;
    }
    public static boolean stackCustomModelDataEquals(@NotNull ItemStack stack, int data) {
        if (!stack.getOrCreateNbt().contains("CustomModelData")) {return false;}
        return stack.getOrCreateNbt().getInt("CustomModelData") == data;
    }
    public static long getHash64(String s) {
        return getHash64(s.getBytes(StandardCharsets.UTF_8));
    }
    public static long bitenable(long var, long nbit) {
        return (var) |= (1L <<(nbit));
    }
    public static long bitdisable(long var, long nbit) {
        return (var) &= (1L <<(nbit));
    }
    public static long bitflip(long var, long nbit) {
        return (var) ^= (1L <<(nbit));
    }
    public static boolean getbit(long var, long nbit) {
        return ((var) & (1L <<(nbit))) == 1;
    }
    public static long setbit(long var, long nbit, boolean value) {
        if (value) {
            return bitenable(var, nbit);
        } else {
            return bitdisable(var,nbit);
        }
    }
    public static boolean isItemInHotbar(ItemStack itemStack) {
        Entity holder = itemStack.getHolder();
        if (holder instanceof PlayerEntity player) {
            System.out.println("pain");
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 9; i++) { // iterate through the hotbar slots (0-8)
                if (inventory.getStack(i).itemMatches(itemStack.getRegistryEntry())) {
                    // the item is in the selected slot
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isBlockIn(BlockState source, TagKey<Block> tag) {
        return source.isIn(tag);
    }
    public static List<Pair<Identifier, Block>> GetAllBlocksInTag(TagKey<Block> tag) {
        List<Pair<Identifier, Block>> data = new ArrayList<>();
        Optional<RegistryEntryList.Named<Block>> init_BLOCKS = Registries.BLOCK.getEntryList(tag);
        init_BLOCKS.ifPresent(registryEntries -> registryEntries.forEach(entry -> {
            Identifier id = entry.getKey().get().getValue();
            Block block = entry.value();
            data.add(Pair.of(id,block));
        }));
        return data;
    }
    public static List<Pair<Identifier, Block>> GetAllBlocksInTagAnd(TagKey<Block> tag, Consumer<Pair<Identifier, Block>> onFound) {
        List<Pair<Identifier, Block>> data = new ArrayList<>();
        Optional<RegistryEntryList.Named<Block>> init_BLOCKS = Registries.BLOCK.getEntryList(tag);
        init_BLOCKS.ifPresent(registryEntries -> registryEntries.forEach(entry -> {
            Identifier id = entry.getKey().get().getValue();
            Block block = entry.value();
            data.add(Pair.of(id,block));
            onFound.accept(Pair.of(id,block));
        }));
        return data;
    }
    public static BlockPos getBlockPosFromArray(long[] a) {
        if (a.length < 3) {return BlockPos.ORIGIN;}
        return new BlockPos((int)a[0], (int)a[1], (int)a[2]);
    }
    public static long[] getBlockPosAsArray(BlockPos d) {
        if (d == null) {return new long[]{0, 0, 0};}
        return new long[]{d.getX(), d.getY(), d.getZ()};
    }
    public static Pair<Integer, Integer> XYPosFromOffset(int w, int offset) {
        assert w != 0;
        int x;
        int y;
        x = offset % w;    // % is the "modulo operator", the remainder of i / width;
        assert offset != 0;
        y = offset / w;    // where "/" is an integer division
        return Pair.of(x,y);
    }
    public static double lerp(double a, double b, float f) {
        return a + f * (b - a);
    }
    public static int mixRGB (int a, int b, float ratio) {
        if (ratio > 1f) {
            ratio = 1f;
        } else if (ratio < 0f) {
            ratio = 0f;
        }
        float iRatio = 1.0f - ratio;

        int aA = (a >> 24 & 0xff);
        int aR = ((a & 0xff0000) >> 16);
        int aG = ((a & 0xff00) >> 8);
        int aB = (a & 0xff);

        int bA = (b >> 24 & 0xff);
        int bR = ((b & 0xff0000) >> 16);
        int bG = ((b & 0xff00) >> 8);
        int bB = (b & 0xff);

        int A = (int)((aA * iRatio) + (bA * ratio));
        int R = (int)((aR * iRatio) + (bR * ratio));
        int G = (int)((aG * iRatio) + (bG * ratio));
        int B = (int)((aB * iRatio) + (bB * ratio));

        return A << 24 | R << 16 | G << 8 | B;
    }
}
