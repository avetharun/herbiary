package com.avetharun.herbiary.hUtil.iface;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public interface ItemAccessor {
    // NEVER CALL THIS IF YOU DON'T KNOW WHAT YOU'RE DOING! (THAT INCLUDES THE DEVELOPER OF THIS MOD!!!!!)
    void PleaseDoNotEverCallThisYourGameWILL_Crash__setRegistryEntry(RegistryEntry.Reference<Item> entry);
    @Nullable RegistryEntry.Reference<Item> getEntryReferenceRaw();
}
