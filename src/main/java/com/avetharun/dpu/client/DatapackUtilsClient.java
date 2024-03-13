package com.avetharun.dpu.client;

import com.avetharun.dpu.client.ModelOverrides.BooleanModelOverride;
import com.avetharun.dpu.client.ModelOverrides.IntModelOverride;
import com.avetharun.dpu.client.ModelOverrides.StringModelOverride;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatapackUtilsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    public static int worldTick, clientTick;
    public static List<Consumer<ClientWorld>> ScheduledActions = new ArrayList<>();
    public static void ScheduleForNextWorldTick(Consumer<ClientWorld> action) {
        ScheduledActions.add(action);
    }
    public static NbtCompound activeRenderingItemStackCompound = null;
    public static int currentInventorySlot = -1;
    public static int currentGlobalSlot = -1;
    @Override
    public void onInitializeClient() {

//        ModelPredicateProviderRegistry.register(new Identifier("nbt"), new BakedNBTModelOverride());
        ModelPredicateProviderRegistry.register(new Identifier("gear_slot"), new StringModelOverride((stack, clientWorld, livingEntity, integer) -> {
            return switch (currentInventorySlot) {
                case 5 -> "HEAD";
                case 6 -> "CHEST";
                case 7 -> "LEGS";
                case 8 -> "BOOTS";
                case 1 -> "CRAFT_TOPLEFT";
                case 2 -> "CRAFT_TOPRIGHT";
                case 3 -> "CRAFT_BOTTOMLEFT";
                case 4 -> "CRAFT_BOTTOMRIGHT";
                case 45 -> "OFFHAND";
                default -> String.valueOf(currentInventorySlot);
            };
        }));
        ModelPredicateProviderRegistry.register(new Identifier("slot"), new IntModelOverride((stack, clientWorld, livingEntity, integer) -> {
            return currentInventorySlot;
        }));
        ModelPredicateProviderRegistry.register(new Identifier("count"), (stack, world, entity, seed) -> (float)stack.getCount() / (float)stack.getMaxCount());
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_first"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandFirst(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_third"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandThird(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_hand_any"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandAny(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_hand_first"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandFirst(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_hand_third"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandThird(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_hand_any"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandAny(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_gui"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_inventory"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_fixed"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_gui"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_inventory"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_fixed"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_item_frame"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_dropped"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_ground"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_on_ground"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_hotbar"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHotbar(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("is_in_hotbar"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHotbar(itemStack)));
        ModelPredicateProviderRegistry.register(new Identifier("use_time"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        ModelPredicateProviderRegistry.register(new Identifier("is_using"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 0.07991f : 0.0F;
        });
        ModelPredicateProviderRegistry.register(new Identifier("is_swinging"), (stack, world, entity, seed) -> {
            return entity != null && entity.handSwinging && entity.getActiveItem() == stack ? 0.07991f : 0.0F;
        });
        ModelPredicateProviderRegistry.register(new Identifier("is_attacking"), (stack, world, entity, seed) -> {
            return entity != null && entity.handSwinging && entity.getActiveItem() == stack ? 0.07991f : 0.0F;
        });

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            for (var action : ScheduledActions) {
                action.accept(world);
            }
            ScheduledActions.clear();
            worldTick++;
        });
        ClientTickEvents.END_CLIENT_TICK.register(world -> {
            clientTick++;
        });

    }
    public static boolean isModelTransformationInHand(ModelTransformationMode mode) {
        return mode.isFirstPerson() || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }
    public static boolean isModelTransformationFixed(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.FIXED;
    }
    public static boolean isModelTransformationGui(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.GUI;
    }
    public static boolean isModelTransformationInHandFirst(ModelTransformationMode mode) {
        return mode.isFirstPerson();
    }
    public static boolean isModelTransformationInHandThird(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }

}
