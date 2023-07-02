package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.client.entity.QuiverArmorRenderer;
import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.renderer.armor.WolfArmorRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class QuiverItem extends ArmorItem implements GeoItem {

    public enum BowArrowType {
        SHARPENED, NORMAL;
        public String getName() {
            return this == NORMAL ? "Normal" : "Sharpened";
        }
        public BowArrowType getOpposite() {
            return this == NORMAL ? SHARPENED : NORMAL;
        }
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    public int ARROWS = 0;
    public int SHARP_ARROWS = 0;
    public int ARROWS_MAX = 128;
    public QuiverItem(ArmorMaterial material, ArmorItem.Type slot, Settings settings) {
        super(material, slot, settings);
    }
    protected ItemStack currentItemStack;
    // Predicate runs every frame
    private PlayState predicate(AnimationState<QuiverItem> event) {return PlayState.STOP;}


    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null)
                    this.renderer = new QuiverArmorRenderer();

                // This prepares our GeoArmorRenderer for the current render frame.
                // These parameters may be null however, so we don't do anything further with them
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }
    private int getAvailableArrowSlots() {
        int arrows_consumed = ARROWS + SHARP_ARROWS;
        return ARROWS_MAX - arrows_consumed;
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateNbt().contains("sharp_arrows")) {
            tooltip.add(Text.literal(stack.getOrCreateNbt().getInt("sharp_arrows") + "x Obsidian Arrows"));
        }
        if (stack.getOrCreateNbt().contains("arrows")) {
            tooltip.add(Text.literal(stack.getOrCreateNbt().getInt("arrows") + "x Normal Arrows"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && otherStack.isOf(Items.ARROW)) {
            if (!stack.getOrCreateNbt().contains("arrows")) {
                stack.getOrCreateNbt().putInt("arrows", 0);
            }
            int _m_arrows = stack.getOrCreateNbt().getInt("arrows");
            _m_arrows += otherStack.getCount();
            stack.getOrCreateNbt().putInt("arrows",_m_arrows);
            otherStack.decrement(_m_arrows);
            player.playSoundIfNotSilent(SoundEvents.ITEM_BUNDLE_INSERT);
            return true;
        } else if (clickType == ClickType.RIGHT && otherStack.isOf(ModItems.SHARPENED_OBSIDIAN_ARROW_ITEM)) {
            if (!stack.getOrCreateNbt().contains("sharp_arrows")) {
                stack.getOrCreateNbt().putInt("sharp_arrows", 0);
            }
            int _m_arrows = stack.getOrCreateNbt().getInt("sharp_arrows");
            _m_arrows += otherStack.getCount();
            stack.getOrCreateNbt().putInt("sharp_arrows",_m_arrows);
            otherStack.decrement(_m_arrows);
            player.playSoundIfNotSilent(SoundEvents.ITEM_BUNDLE_INSERT);
            return true;
        } else if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
            if (stack.getOrCreateNbt().contains("sharp_arrows") && stack.getOrCreateNbt().contains("arrows")) {
                int _m_arrows = stack.getOrCreateNbt().getInt("arrows");
                int _m_Sarrows = stack.getOrCreateNbt().getInt("sharp_arrows");
                stack.getOrCreateNbt().putInt("arrows", 0);
                stack.getOrCreateNbt().putInt("sharp_arrows", 0);
                player.giveItemStack(Items.ARROW.getDefaultStack().copyWithCount(_m_arrows));
                player.giveItemStack(ModItems.SHARPENED_OBSIDIAN_ARROW_ITEM.getDefaultStack().copyWithCount(_m_Sarrows));
                player.playSoundIfNotSilent(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS);
                return true;
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);

    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
}
