package com.avetharun.herbiary.mixin.Rendering;

import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(EntityRenderer.class)
public abstract class LivingEntityRendererMixin{
    @Mixin(EntityRendererFactory.Context.class)
    public static class LivingEntityRendererFactory_ContextMixin{
        public NbtCompound data = new NbtCompound();
        public boolean hasCustomData = false;
    }
    public LinkedHashMap<Identifier, NbtCompound> requiredCompound;
    private static <T extends EntityRenderer> Identifier  getIDForEntity(T _this, Entity e, Identifier _default) {
        LinkedHashMap<Identifier, NbtCompound> requiredCompound = alib.getMixinField(_this, "requiredCompound");
        if (requiredCompound != null&& (boolean) alib.getMixinField(e, "hasOverride")) {
            AtomicBoolean brk = new AtomicBoolean(false);
            AtomicReference<Identifier> out = new AtomicReference<>(null);
            requiredCompound.forEach((identifier, compound) -> {
                if (!brk.get() && alib.checkNBTEquals(compound, e.writeNbt(new NbtCompound()))) {
                    out.set(identifier);
                    brk.set(true);
                }
            });
            if (out.get() == null) {return _default; } // fallback
            return out.get();
        }
        return _default;
    }
}
