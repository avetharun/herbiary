package com.avetharun.herbiary.mixin.Item;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.QuiverItem;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.packet.ArrowSpawnS2CPacket;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.Set;

@Mixin(BowItem.class)
public abstract class BowItemMixin {
    @Mixin(ArrowEntity.class)
    public static abstract class ArrowEntityMixin{
        @Mixin(ArrowEntityRenderer.class)
        public static abstract class ArrowEntityRendererMixin{
            private static final Identifier OBSIDIAN_TEXTURE = new Identifier("al_herbiary", "textures/entity/projectiles/obsidian_arrow.png");
            @Inject(method= "getTexture*", at=@At("HEAD"), cancellable = true)
            void getTextureMixin(ArrowEntity arrowEntity, CallbackInfoReturnable<Identifier> cir) {
                if ((boolean)alib.getMixinField(arrowEntity, "isObsidian")) {
                    cir.setReturnValue(OBSIDIAN_TEXTURE);
                    cir.cancel();
                }
            }
        }
        @Shadow @Final private Set<StatusEffectInstance> effects;

        @Shadow public abstract void addEffect(StatusEffectInstance effect);

        @Shadow private Potion potion;
        public boolean willCauseBleed = false;
        public boolean isObsidian = false;
        @Inject(method="initFromStack", at=@At("TAIL"))
        void initFromStack(ItemStack stack, CallbackInfo ci) {
            if (stack.isOf(ModItems.SHARPENED_OBSIDIAN_ARROW_ITEM)) {
                willCauseBleed = true;
                isObsidian = true;
            }
        }

        public void onSpawnPacket(EntitySpawnS2CPacket packet) {
            ArrowEntity _this = ((ArrowEntity)(Object)this);
            ArrowSpawnS2CPacket aPacket = (ArrowSpawnS2CPacket)packet;
            // Entity.super.onSpawnPacket()

            int i = packet.getId();
            double d = packet.getX();
            double e = packet.getY();
            double f = packet.getZ();
            _this.updateTrackedPosition(d, e, f);
            _this.refreshPositionAfterTeleport(d, e, f);
            _this.setPitch(packet.getPitch());
            _this.setYaw(packet.getYaw());
            _this.setId(i);
            _this.setUuid(packet.getUuid());

            // ProjectileEntity.super.onSpawnPacket()
            Entity entity = _this.getWorld().getEntityById(packet.getEntityData());
            if (entity != null) {
                _this.setOwner(entity);
            }

            this.isObsidian = aPacket.arrowType == QuiverItem.BowArrowType.SHARPENED;
            this.willCauseBleed = aPacket.willCauseBleeding;
        }
        public Packet<ClientPlayPacketListener> createSpawnPacket() {
            ArrowEntity _this = ((ArrowEntity)(Object)this);
            Entity entity = _this.getOwner();
            return new ArrowSpawnS2CPacket(_this, this.isObsidian ? QuiverItem.BowArrowType.SHARPENED : QuiverItem.BowArrowType.NORMAL, willCauseBleed, entity == null ? 0 : entity.getId());
        }
        @Inject(method="asItemStack", at=@At("HEAD"), cancellable = true)
        void asItemStack(CallbackInfoReturnable<ItemStack> cir) {
            if (this.isObsidian) {cir.setReturnValue(ModItems.SHARPENED_OBSIDIAN_ARROW_ITEM.getDefaultStack());}
        }
        @Inject(method="writeCustomDataToNbt", at=@At("TAIL"))
        void nbtSave(NbtCompound nbt, CallbackInfo ci) {
            nbt.putBoolean("willCauseBleeding", willCauseBleed);
            nbt.putBoolean("isObsidian", isObsidian);
        }
        @Inject(method="readCustomDataFromNbt", at=@At("TAIL"))
        void nbtRead(NbtCompound nbt, CallbackInfo ci) {
            if (nbt.contains("willCauseBleeding")) {
                willCauseBleed = nbt.getBoolean("willCauseBleeding");
            }
            if (nbt.contains("isObsidian")) {
                isObsidian = nbt.getBoolean("isObsidian");
            }
        }
        @Inject(method="onHit", at=@At("TAIL"))
        void onHitMixin(LivingEntity target, CallbackInfo ci) {
            if (willCauseBleed) {
                target.addStatusEffect(new StatusEffectInstance(Herbiary.EFFECT_BLEED, 40, 2));
            }
        }
    }
    @Shadow public abstract int getMaxUseTime(ItemStack stack);
    private static float m_getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
    @Inject(method = "use",at=@At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack stackHead = user.getEquippedStack(EquipmentSlot.HEAD);
        if (stackHead.isOf(ModItems.QUIVER)) {
            if (stackHead.getOrCreateNbt().contains("arrows") || stackHead.getOrCreateNbt().contains("sharp_arrows")) {
                if (stackHead.getOrCreateNbt().getInt("arrows") >= 1 || stackHead.getOrCreateNbt().getInt("sharp_arrows") >= 1) {
                    user.setCurrentHand(hand);
                    user.playSoundIfNotSilent(SoundEvents.ITEM_BUNDLE_REMOVE_ONE);
                    cir.setReturnValue(TypedActionResult.consume(itemStack));
                }
            }
        }
    }

    @Inject(method="onStoppedUsing", at=@At("HEAD"), cancellable = true)
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean bowIsVanilla = false;
            boolean bowIsLongbow = false;
            Identifier stackID = stack.getItem().getRegistryEntry().getKey().get().getValue();
            if (stackID.equals(new Identifier("minecraft","bow"))) {
                bowIsVanilla = true;
            }
            if (stackID.equals(new Identifier("al_herbiary","longbow"))) {
                bowIsLongbow = true;
            }
            boolean bl = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            ItemStack stackHead = user.getEquippedStack(EquipmentSlot.HEAD);
            boolean has_quiver_equipped = false;
            boolean has_SharpArrow = false;
            boolean has_NormalArrow = false;

            if (stackHead.isOf(ModItems.QUIVER)) {
                if (stackHead.getOrCreateNbt().contains("arrows") && alib.getMixinField(user, "selectedArrowType") == QuiverItem.BowArrowType.NORMAL) {
                    if (stackHead.getOrCreateNbt().getInt("arrows") >= 1) {
                        has_quiver_equipped = true;
                        has_NormalArrow = true;
                    }
                    itemStack = Items.ARROW.getDefaultStack();
                }
                if (stackHead.getOrCreateNbt().contains("sharp_arrows") && alib.getMixinField(user, "selectedArrowType") == QuiverItem.BowArrowType.SHARPENED) {
                    if (stackHead.getOrCreateNbt().getInt("sharp_arrows") >= 1) {
                        has_quiver_equipped = true;
                        has_SharpArrow = true;
                    }
                    itemStack = ModItems.SHARPENED_OBSIDIAN_ARROW_ITEM.getDefaultStack();
                }
            }
            if (!itemStack.isEmpty() || bl || has_quiver_equipped) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }
                int i = getMaxUseTime(stack) - remainingUseTicks;
                float f = m_getPullProgress(i);
                if (!((double)f < 0.1)) {
                    boolean bl2 = bl && (itemStack.isIn(ItemTags.ARROWS) || has_quiver_equipped);
                    if (!world.isClient) {
                        ArrowItem arrowItem = (ArrowItem)(itemStack.getItem());
                        ArrowEntity persistentProjectileEntity = (ArrowEntity) arrowItem.createArrow(world, itemStack, playerEntity);
                        persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.0F);
                        alib.setMixinField(persistentProjectileEntity, "isObsidian", itemStack.isOf(ModItems.SHARPENED_OBSIDIAN_ARROW_ITEM));
                        if (bowIsVanilla) {
                            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 0.35f);
                            persistentProjectileEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.25F);
                        }
                        if (f == 1.0F && bowIsLongbow) {
                            persistentProjectileEntity.setCritical(true);
                        }

                        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)j * 0.5 + 0.5);
                        }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            persistentProjectileEntity.setPunch(k);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            persistentProjectileEntity.setOnFireFor(100);
                        }

                        stack.damage(24, playerEntity, (p) -> {
                            p.sendToolBreakStatus(playerEntity.getActiveHand());
                        });
                        if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }

                        world.spawnEntity(persistentProjectileEntity);
                        if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                            if (itemStack.isEmpty()) {
                                playerEntity.getInventory().removeOne(itemStack);
                            }
                        }
                        if (has_quiver_equipped) {
                            if (has_NormalArrow && alib.getMixinField(user, "selectedArrowType") == QuiverItem.BowArrowType.NORMAL) {
                                int _i = stackHead.getOrCreateNbt().getInt("arrows");
                                stackHead.getOrCreateNbt().putInt("arrows", _i - 1);
                            }
                            if (has_SharpArrow && alib.getMixinField(user, "selectedArrowType") == QuiverItem.BowArrowType.SHARPENED) {
                                int _i = stackHead.getOrCreateNbt().getInt("sharp_arrows");
                                stackHead.getOrCreateNbt().putInt("sharp_arrows", _i - 1);
                            }
                        }
                    }

                    world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(((BowItem)(Object)this)));
                }
            }
        }
        ci.cancel();
    }
}
