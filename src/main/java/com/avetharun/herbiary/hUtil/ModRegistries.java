package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.screens.CampfirePlacementScreenHandler;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

@SuppressWarnings("rawtypes")
public class ModRegistries {
    public static class CampfireScreenEntry<T extends CampfirePlacementScreenHandler>{
        public ScreenHandlerType<T> type;
        public CampfireScreenEntry(ScreenHandlerType<T> ty) {
            this.type = ty;
        }
    }
    public static RegistryKey<Registry<CampfireScreenEntry>> CAMPFIRE_SCREENS_KEY = RegistryKey.ofRegistry(new Identifier("herbiary", "campfirescreens"));
    public static Registry<CampfireScreenEntry> CAMPFIRE_SCREENS = FabricRegistryBuilder.createSimple(CAMPFIRE_SCREENS_KEY).buildAndRegister();
    public static <T extends CampfirePlacementScreenHandler> CampfireScreenEntry registerCampfireScreen(Identifier id, ScreenHandlerType<T> screenHandlerType) {
         return Registry.register(CAMPFIRE_SCREENS, id, new CampfireScreenEntry<T>(screenHandlerType));
    }
}
