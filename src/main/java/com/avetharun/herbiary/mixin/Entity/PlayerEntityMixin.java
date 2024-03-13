package com.avetharun.herbiary.mixin.Entity;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.QuiverItem;
import com.avetharun.herbiary.ModItems;
import com.avetharun.herbiary.client.HerbiaryClient;
import com.avetharun.herbiary.client.PlayerStatus;
import com.avetharun.herbiary.client.StatusOverlay;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.hUtil.iface.PlayerEntityAccessor;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isSifting() {
        return isSifting;
    }

    @Override
    public void setSifting(boolean state) {
        isSifting = state;
    }

    @Unique
    public boolean isSifting;
    public QuiverItem.BowArrowType selectedArrowType = QuiverItem.BowArrowType.NORMAL;
    @Mixin(ServerPlayerEntity.class)
    public static abstract class ServerPlayerEntityMixin {
        public ArrayList<String> knownIDs = new ArrayList<>();
        @Inject(method="writeCustomDataToNbt", at=@At("TAIL"))
        void writeCustomData(NbtCompound nbt, CallbackInfo ci) {
            NbtList l = new NbtList();
            for (String id : knownIDs) {
                l.add(NbtString.of(id));
            }
            nbt.put("knownItemNames", l);
        }
        @Inject(method="readCustomDataFromNbt", at=@At("TAIL"))
        void readCustomData(NbtCompound nbt, CallbackInfo ci) {
            if (nbt.contains("knownItemNames")) {
                NbtList l = (NbtList) nbt.get("knownItemNames");
                assert l != null;
                for (int i = 0; i < l.size(); i++) {
                    knownIDs.add(l.getString(i));
                }
            }
        }
    }
    public boolean isLocalPlayer(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) { // Check if running on client-side
            return player == client.player;
        } else { // Running on server-side
            return false;
        }
    }
    @Unique
    public int ticksUntilZipwingAvailable = 0;
    @Inject(method="handleFallDamage", at=@At(shift= At.Shift.BEFORE, value="INVOKE", target = "Lnet/minecraft/entity/LivingEntity;handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z"), cancellable = true)
    void handleFallDamageMixin(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir){
        PlayerEntity e = (PlayerEntity) (Object)this;
        ItemStack s = e.getEquippedStack(EquipmentSlot.CHEST);
        if (s.isEmpty()) {return;}
        int enc = EnchantmentHelper.getLevel(ModItems.GRACEFUL_ENCHANTMENT, s);
        boolean bl1 = s.hasEnchantments() && enc > 0;
        if (damageSource.isOf(DamageTypes.FLY_INTO_WALL) && bl1) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
    @Inject(method="readCustomDataFromNbt", at=@At("TAIL"))
    void readNBTMixin(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("statuses")) {
            NbtCompound c = nbt.getCompound("statuses");
            this.statuses.get("cold").percentage = c.getFloat("cold");
            this.statuses.get("hot").percentage = c.getFloat("hot");
            this.statuses.get("wet").percentage = c.getFloat("wet");
        }
    }
    @Inject(method="writeCustomDataToNbt", at=@At("HEAD"))
    void writeNBTMixin(NbtCompound nbt, CallbackInfo ci){
        NbtCompound c = new NbtCompound();
        c.putFloat("cold", this.statuses.get("cold").percentage);
        c.putFloat("hot", this.statuses.get("hot").percentage);
        c.putFloat("wet", this.statuses.get("wet").percentage);
        nbt.put("statuses",c);
    }
    public HashMap<String,PlayerStatus> statuses = new HashMap<>();
    @Inject(method="<init>", at=@At("TAIL"))
    void init(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        PlayerEntity e = (PlayerEntity) (Object)this;
        if (isLocalPlayer(e)) {
            StatusOverlay.localStatuses = statuses;
        }
        StatusOverlay.localStatuses = statuses;
        statuses.put("cold", new PlayerStatus("Cold", 10));
        statuses.put("hot", new PlayerStatus("Hot",30));
        statuses.put("wet", new PlayerStatus("Wet", 5));
    }
    boolean isHot = false;
    boolean isCold = false;
    boolean isWet = false;
    boolean coolOff = false;
    int ticksSinceBegunSleeping = 0;

    @Inject(method="tick", at=@At("TAIL"))
    void tickStatuses(CallbackInfo ci) {
        PlayerEntity e = (PlayerEntity) (Object)this;
        if (e instanceof ServerPlayerEntity p) {
            if (e.isSleeping()) {
                ticksSinceBegunSleeping++;
                if (ticksSinceBegunSleeping > 100) {
                    e.wakeUp(true, true);

                    long timeOfDay = e.getWorld().getTimeOfDay();

                    long ticksUntilNextDay = 24000 - (timeOfDay % 24000);

                    long nextDayTime = timeOfDay + ticksUntilNextDay;
                    ServerWorld world = ((ServerPlayerEntity) e).getServerWorld();
                    world.setTimeOfDay(nextDayTime);
                }
            } else {ticksSinceBegunSleeping=0;}
//        if (e.isCreative()) {return;}
            BlockPos pos = e.getBlockPos();
            Biome biome = e.getWorld().getBiome(pos).value();
            PlayerStatus coldStatus = statuses.get("cold");
            PlayerStatus wetStatus = statuses.get("wet");
            PlayerStatus hotStatus = statuses.get("hot");
            float increment = 0;
            float temperature = biome.getTemperature();
            // hot status
            // find any heat sources near the player in a 1 block radius
            if ((biome.getTemperature() > 1.25f && e.getWorld().getLightLevel(e.getBlockPos()) > 2) || (e.getWorld().isNight() && e.getWorld().getLightLevel(e.getBlockPos()) >= 12)) {
                isHot = true;
                if (!coolOff) {
                    increment = 0.05f;
                } else {
                    hotStatus.percentage = MathHelper.clamp(hotStatus.percentage + increment, 0, 100);
                }
            } else if (coolOff) {
                isHot = false;
                increment = 0.8f;
                hotStatus.percentage = MathHelper.clamp(hotStatus.percentage - increment * 2, 0, 100);
            } else {
                // slowly decrement if no longer hot
                increment = 0.1f;
                hotStatus.percentage = MathHelper.clamp(hotStatus.percentage - increment * 2, 0, 100);
            }
            if ((biome.getTemperature() < 0.2f || e.inPowderSnow ) || e.getWorld().getLightLevel(pos) < 8 && !isHot || e.getBlockStateAtPos().isIn(BlockTags.SNOW) || e.getWorld().getBlockState(e.getBlockPos().down()).isIn(BlockTags.SNOW)) {
                isCold = true;
                if (e.isTouchingWaterOrRain()) {temperature = 0.25f;}
                if (e.inPowderSnow) {temperature = -25f;}
                float coldness = 2.0f - temperature;
                increment = Math.max(0, Math.abs(coldness)) * 0.1f;
                coldStatus.percentage = MathHelper.clamp(coldStatus.percentage + increment, 0, 100);
                if (coldStatus.percentage == 100 && p.getWorld().getServer().getTicks() % 20 == 0) {
                    e.damage(e.getDamageSources().freeze(), 1f);
                }
            } else {
                isCold = false;
                float coldness = 2.0f - temperature;
                increment = Math.max(0, Math.min(1, coldness)) * 0.1f;
                if (isHot) {
                    increment = 1f;
                }
                coldStatus.percentage = MathHelper.clamp(coldStatus.percentage - increment * 2, 0, 100);
            }
            if (coldStatus.percentage > 0) {
                e.setFrozenTicks((int)MathHelper.lerp(coldStatus.percentage * 0.01, 0, 30));
            }

            if (e.isTouchingWaterOrRain() || biome.getTemperature() < 1f) {
                coolOff = true;
            }
            // wet status

            if (e.isTouchingWaterOrRain()) {
                increment = 1.2f;
                if (e.isSwimming()) {
                    increment = 10f;
                }
                wetStatus.percentage = MathHelper.clamp(wetStatus.percentage + increment * 2, 0, 100);
                if (e.getWorld().hasRain(e.getBlockPos())) {
                    if (e.getWorld().getTime() % 4 == 0 && wetStatus.percentage > 5) {
                        e.getWorld().addParticle(ParticleTypes.RAIN, e.getX() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), e.getY() + 0.1f, e.getZ() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), 0, 0, 0);
                    }
                }
            } else {
                if (e.getWorld().getTime() % 7 == 0 && wetStatus.percentage > 5) {
                    e.getWorld().addParticle(ParticleTypes.RAIN, e.getX() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), e.getY() + 0.1f, e.getZ() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), 0, 0, 0);
                }
                increment = 0.05f;
                wetStatus.percentage = MathHelper.clamp(wetStatus.percentage - increment * 2, 0, 100);
                hotStatus.percentage = MathHelper.clamp(hotStatus.percentage - increment * 0.8f, 0, 100);
            }
        }
    }
    @Shadow public abstract boolean canModifyBlocks();
    // if I can find a better method to allow breaking specific blocks via a gamerule, I will.
    // I don't think server & client get the same datapack, so unless I can make a packet of sorts, this will be the only method.
    @Inject(method="isBlockBreakingRestricted", at=@At("HEAD"), cancellable = true)
    void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {

        // Until further notice: This will NOT be modified! Replace vanilla blocks instead!!!
//        if (gameMode.isCreative() || !world.isClient) {
//            return;
//        }
//        if (!HerbiaryClient.CanBreakVanillaBlocks && HerbiaryClient.CanMineStoneLikeBlocks && world.getBlockState(pos).isIn(BlockTags.PICKAXE_MINEABLE)) {
//            return;
//        }
//
//        else if (!HerbiaryClient.CanBreakVanillaBlocks) {
//            if (Herbiary.AllowedHerbiaryBreakables.contains(world.getBlockState(pos).getBlock())) {
//                return;
//            } else {
//                cir.setReturnValue(true);
//                cir.cancel();
//            }
//        }
    }
}
