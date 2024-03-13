package com.avetharun.herbiary.hUtil.iface;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayDeque;
import java.util.Map;

public interface RegistryAccessor<T> {
    Map<T, RegistryEntry.Reference<T>> valueToEntryGetter();
    Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntryGetter();
    boolean remove(Identifier id);
    boolean remove(RegistryKey<T> id);
    boolean remove(T value);
    interface RegistryEntryAccessor<T>{
        T getValue();
        void setValue(T newValue);
    }
}
