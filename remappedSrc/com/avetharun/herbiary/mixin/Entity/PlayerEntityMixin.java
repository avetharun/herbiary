package com.avetharun.herbiary.mixin.Entity;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.QuiverItem;
import com.avetharun.herbiary.client.HerbiaryClient;
import com.avetharun.herbiary.client.PlayerStatus;
import com.avetharun.herbiary.client.StatusOverlay;
import com.avetharun.herbiary.entity.ModEntityTypes;
import com.avetharun.herbiary.hUtil.alib;
import com.avetharun.herbiary.packet.UnlockItemNamePacket;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.entity.WardenEntityRenderer;
import net.minecraft.client.render.entity.feature.WardenFeatureRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.*;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    public boolean isSifting;
    public QuiverItem.BowArrowType selectedArrowType = QuiverItem.BowArrowType.NORMAL;
    @Mixin(ServerPlayerEntity.class)
    public static abstract class ServerPlayerEntityMixin {

        @Inject(method="tick", at=@At("TAIL"))
        void tickMixin(CallbackInfo ci) {
            ServerPlayerEntity pE = (ServerPlayerEntity) (Object)this;
            if (pE.isSleeping()) {
                pE.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 6, 1, true, false));
            }
        }
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

                    long timeOfDay = e.method_48926().getTimeOfDay();

                    long ticksUntilNextDay = 24000 - (timeOfDay % 24000);

                    long nextDayTime = timeOfDay + ticksUntilNextDay;
                    ServerWorld world = ((ServerPlayerEntity) e).getServerWorld();
                    world.setTimeOfDay(nextDayTime);
                }
            } else {ticksSinceBegunSleeping=0;}
//        if (e.isCreative()) {return;}
            BlockPos pos = e.getBlockPos();
            Biome biome = e.method_48926().getBiome(pos).value();
            PlayerStatus coldStatus = statuses.get("cold");
            PlayerStatus wetStatus = statuses.get("wet");
            PlayerStatus hotStatus = statuses.get("hot");
            float increment = 0;
            float temperature = biome.getTemperature();
            // hot status
            // find any heat sources near the player in a 1 block radius
            if ((biome.getTemperature() > 1.25f && e.method_48926().getLightLevel(e.getBlockPos()) > 2) || (e.method_48926().getLightLevel(e.getBlockPos()) >= 8)) {
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
            if ((biome.getTemperature() < 0.2f || e.inPowderSnow ) || e.method_48926().getLightLevel(pos) < 8 && !isHot || e.getBlockStateAtPos().isIn(BlockTags.SNOW) || e.method_48926().getBlockState(e.getBlockPos().down()).isIn(BlockTags.SNOW)) {
                isCold = true;
                if (e.isTouchingWaterOrRain()) {temperature = 0.25f;}
                if (e.inPowderSnow) {temperature = -25f;}
                float coldness = 2.0f - temperature;
                increment = Math.max(0, Math.abs(coldness)) * 0.1f;
                coldStatus.percentage = MathHelper.clamp(coldStatus.percentage + increment, 0, 100);
                if (coldStatus.percentage == 100 && p.method_48926().getServer().getTicks() % 20 == 0) {
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
                if (e.method_48926().hasRain(e.getBlockPos())) {
                    if (e.method_48926().getTime() % 4 == 0 && wetStatus.percentage > 5) {
                        e.method_48926().addParticle(ParticleTypes.RAIN, e.getX() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), e.getY() + 0.1f, e.getZ() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), 0, 0, 0);
                    }
                }
            } else {
                if (e.method_48926().getTime() % 7 == 0 && wetStatus.percentage > 5) {
                    e.method_48926().addParticle(ParticleTypes.RAIN, e.getX() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), e.getY() + 0.1f, e.getZ() + alib.getRandomFloat(e.getRandom(), -0.25f, 0.25f), 0, 0, 0);
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
        if (gameMode.isCreative()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
        if (world.isClient && !HerbiaryClient.CanBreakVanillaBlocks) {
            if (Herbiary.AllowedHerbiaryBreakables.contains(world.getBlockState(pos).getBlock())) {
                cir.setReturnValue(false);
                cir.cancel();
            } else {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
        if (!world.isClient) {
            boolean canBreakVanillaBlocks = world.getGameRules().getBoolean(Herbiary.ALLOW_VANILLA_BLOCK_BREAKING);
            boolean blockIsAllowed = world.getBlockState(pos).isIn(Herbiary.ALLOWED_VANILLA_BREAKABLES);
            if (!canBreakVanillaBlocks && !blockIsAllowed) {
                cir.setReturnValue(true);
                cir.cancel();
            }
            if (!canBreakVanillaBlocks && blockIsAllowed) {
                cir.setReturnValue(false);
            } else if (canBreakVanillaBlocks) {
                if (!gameMode.isBlockBreakingRestricted()) {
                    cir.setReturnValue(false);
                } else if (gameMode == GameMode.SPECTATOR) {
                    cir.setReturnValue(true);
                } else if (this.canModifyBlocks()) {
                    cir.setReturnValue(false);
                } else {
                    ItemStack itemStack = (((LivingEntity) (Object) this)).getMainHandStack();
                    cir.setReturnValue(itemStack.isEmpty() || !itemStack.canDestroy(world.getRegistryManager().get(RegistryKeys.BLOCK), new CachedBlockPosition(world, pos, false)));
                }
            }
        }
    }
}
