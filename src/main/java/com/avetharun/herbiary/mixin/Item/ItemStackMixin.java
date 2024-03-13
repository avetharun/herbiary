package com.avetharun.herbiary.mixin.Item;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.client.HerbiaryClient;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.hUtil.iface.PlayerEntityAccessor;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {


    public ModelTransformationMode transformationMode;
    public boolean isBeingRenderedInHotbar = false, isBeingRenderedInGUICompat = false, isBeingRenderedInHand = false;

    @Inject(method="onStoppedUsing", at=@At("HEAD"))
    void stopUsingMixin(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci){
        if (user instanceof PlayerEntityAccessor pea) {
            pea.setSifting(false);
        }
    }
    @Inject(method="useOnBlock", at=@At("HEAD"))
    void useOnBlockMixin(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
    }
    @Inject(method="use", at=@At("HEAD"))
    void useMixin(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {}
}
