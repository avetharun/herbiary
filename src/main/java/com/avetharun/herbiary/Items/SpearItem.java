package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.HerbiarySoundEvents;
import com.avetharun.herbiary.Items.ItemEntities.HerbiarySpearItemEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.Vanishable;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.mojang.blaze3d.platform.GlStateManager.Viewport.getX;
import static com.mojang.blaze3d.platform.GlStateManager.Viewport.getY;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.getZ;

public class SpearItem extends TridentItem implements Vanishable {

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public SpearItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID,
                "Tool modifier", 4, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
                "Tool modifier", -3.5, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {

        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }
    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 12) {
                if (!world.isClient) {
                    int damagedState = stack.getMaxDamage() - stack.getDamage();
                    stack.damage(10, playerEntity, (p) -> {
                        p.sendToolBreakStatus(user.getActiveHand());
                    });
                    boolean returnBroken = damagedState == 10 || damagedState == 0;
                    if (returnBroken) {return;}
                    HerbiarySpearItemEntity tridentLikeEntity = new HerbiarySpearItemEntity(world, playerEntity, stack.copy());
                    tridentLikeEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.1F, 1.0F);
                    tridentLikeEntity.setPosition(playerEntity.getEyePos());
                    tridentLikeEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                    world.spawnEntity(tridentLikeEntity);
                    playerEntity.getInventory().removeOne(stack);

                }
            }
        }
    }
}
