package com.avetharun.herbiary.client;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemModel;
import com.avetharun.herbiary.Items.QuiverItem;
import com.avetharun.herbiary.client.entity.*;
import com.avetharun.herbiary.client.particle.FlintSparkParticle;
import com.avetharun.herbiary.entity.block.ToolrackBlockEntity;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.packet.HerbiaryBlockStateInitPacket;
import com.avetharun.herbiary.packet.SwitchArrowTypePacket;
import com.avetharun.herbiary.packet.UnlockItemNamePacket;
import com.avetharun.herbiary.screens.BackpackScreenHandler;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.entity.ModEntityTypes;
import com.avetharun.herbiary.recipes.RecipesUtil;
import com.avetharun.herbiary.screens.CampfirePotScreenHandler;
import com.avetharun.herbiary.screens.WorkstationScreen;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.avetharun.herbiary.Herbiary.BACKPACK_UPDATE_PACKET_ID;

@Environment(EnvType.CLIENT)
public class HerbiaryClient implements ClientModInitializer {
    private static KeyBinding switchArrowTypeKeybind;
    BakedModel SPEAR_BAKED_GUI_MODEL;
    public static boolean CanBreakVanillaBlocks = true;
    public static boolean IsAllowedToPlaceHerbBlocks = false;
    public static final EntityModelLayer MODEL_SPEAR_LAYER = new EntityModelLayer(new Identifier("al_herbiary", "spear_entity"), "spear");

    private void onRenderHud(DrawContext matrixStack, float v) {
        // Render the overlay
        MinecraftClient.getInstance().getProfiler().push("status_overlay");
//        StatusOverlay.render(matrixStack);
        // Reset the text color to default
        InGameHud hud = MinecraftClient.getInstance().inGameHud;
        MinecraftClient.getInstance().getProfiler().pop();

    }
    public static Set<String> KnownPlants = Collections.newSetFromMap(new ConcurrentHashMap<>());
    @Override
    public void onInitializeClient() {
        switchArrowTypeKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.herbiary.toggle_arrow_type", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_V, // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
        ));
        HudRenderCallback.EVENT.register(this::onRenderHud);
        ClientPlayNetworking.registerGlobalReceiver(Herbiary.UNLOCK_ITEM_PACKET_ID, (client, handler, buf, responseSender) -> {
            UnlockItemNamePacket packet = new UnlockItemNamePacket(buf);
            boolean lock = packet.forceLocked;
            if (lock) {
                for (String id : packet.unlockedTranslationKeys) {
                    KnownPlants.remove(id);
                }
                return;
            }
            KnownPlants.addAll(packet.unlockedTranslationKeys);
            System.out.println("Recieved known plants");
        });
        ClientPlayNetworking.registerGlobalReceiver(Herbiary.SYNC_BREAKABLES_PACKET_ID, (client, handler, buf, responseSender) -> {
            System.out.println("Recieved gamerules");
            HerbiaryBlockStateInitPacket b = new HerbiaryBlockStateInitPacket(buf);
            Herbiary.AllowedHerbiaryBreakables.clear();
            b.getBlocksArray().forEach(identifier -> {
                Herbiary.AllowedHerbiaryBreakables.add(Registries.BLOCK.get(identifier));
            });
            IsAllowedToPlaceHerbBlocks = b.allow_herb_placement;
            CanBreakVanillaBlocks = b.allow_vanilla_breaking;
        });

        ClientPlayNetworking.registerGlobalReceiver(BACKPACK_UPDATE_PACKET_ID, (client, handler, buf, responseSender) -> {
            int slotCount = buf.readVarInt();
            assert client.player != null;
            DefaultedList<ItemStack> inventory = client.player.getInventory().main;

            // Update the slots in the client-side inventory
            for (int i = 0; i < slotCount; i++) {
                inventory.set(i, buf.readItemStack());
            }
        });

        BlockEntityRendererRegistry.register(ModItems.TOOLRACK_BLOCK_ENTITY, ToolrackBlockEntity.Renderer::new);

        EntityRendererRegistry.register(ModEntityTypes.OWL_ENTITY_TYPE, OwlEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.FIELD_MOUSE_ENTITY_TYPE, FieldMouseEntityRenderer::new);
        EntityRendererRegistry.register(ModItems.SPEAR_ENTITY_TYPE, HerbiarySpearItemModel::new);
        EntityRendererRegistry.register(ModEntityTypes.TENT_ENTITY_TYPE, TentEntityRenderer::new);

        EntityRendererRegistry.register(ModEntityTypes.TENT_STORAGE_ENTITY_ENTITY_TYPE, TentSubEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_SPEAR_LAYER, HerbiarySpearItemModel::getTexturedModelData);
        ModelLoadingRegistry.INSTANCE.registerModelProvider(((manager, out) -> out.accept(new ModelIdentifier(new Identifier("al_herbiary", "spear_inventory"), "inventory"))));


        HandledScreens.register(RecipesUtil.WORKSTATION_SCREEN_HANDLER, WorkstationScreen::new);
        HandledScreens.register(RecipesUtil.POT_SCREEN_HANDLER, CampfirePotScreenHandler.CampfirePotScreen::new);
        HandledScreens.register(BackpackScreenHandler.BACKPACK_SCREEN_HANDLER_TYPE, BackpackScreenHandler.BackpackScreen::new);

        ModelPredicateProviderRegistry.register(new Identifier("bow_pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });

        ModelPredicateProviderRegistry.register(new Identifier("bow_pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
        ParticleFactoryRegistry.getInstance().register(Herbiary.FLINT_SPARK, FlintSparkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Herbiary.FLINT_SPARK_SMOKE, FlintSparkParticle.FlintSmokeParticle.Factory::new);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (switchArrowTypeKeybind.wasPressed()) {
                assert client.player != null;
                QuiverItem.BowArrowType s_T = ((QuiverItem.BowArrowType)alib.getMixinField(client.player, "selectedArrowType")).getOpposite();
                PacketByteBuf buf = PacketByteBufs.create();
                SwitchArrowTypePacket p = new SwitchArrowTypePacket();
                p.wantedArrowType = s_T;
                p.write(buf);
                alib.setMixinField(client.player, "selectedArrowType", p.wantedArrowType);
                client.inGameHud.setSubtitle(Text.literal("Set arrow type to " + p.wantedArrowType.getName() + " Arrows."));
                ClientPlayNetworking.send(Herbiary.SET_ARROW_TYPE_PACKET_ID, buf);
            }
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
    private static <T extends ParticleEffect> ParticleType registerParticle(String name, boolean alwaysShow, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> codecGetter) {
        return (ParticleType)Registry.register(Registries.PARTICLE_TYPE, new Identifier("al_herbiary", name), new ParticleType<T>(alwaysShow, factory) {
            public Codec<T> getCodec() {
                return (Codec)codecGetter.apply(this);
            }
        });
    }

    private static DefaultParticleType registerParticle(String name, boolean alwaysShow) {
        return (DefaultParticleType)Registry.register(Registries.PARTICLE_TYPE, name, FabricParticleTypes.simple(alwaysShow));
    }
}