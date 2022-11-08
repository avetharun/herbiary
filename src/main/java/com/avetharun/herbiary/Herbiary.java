package com.avetharun.herbiary;

import com.avetharun.herbiary.hUtil.ModItems;
import com.avetharun.herbiary.hUtil.ModStatusEffects.Bleed;
import com.avetharun.herbiary.entity.ModEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;

public class Herbiary implements ModInitializer {
    public static String MOD_ID = "al_herbiary";
    public static Item OWL_SPAWN_EGG;
    public static final StatusEffect EFFECT_BLEED = new Bleed();
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        ModItems.registerModItemData();
        ModEntityTypes.InitializeModEntityTypes();

        OWL_SPAWN_EGG = Registry.register(Registry.ITEM, new Identifier("al_herbiary", "owl_spawn_egg"), new SpawnEggItem(ModEntityTypes.OWL_ENTITY_TYPE,
                0x1A1A1A,
                0xF2ADA1,
                (new Item.Settings()).group(ItemGroup.MISC)));
        Registry.register(Registry.STATUS_EFFECT, new Identifier("al_herbiary", "bleed"), EFFECT_BLEED);
        Registry.register(Registry.SOUND_EVENT, HerbiarySoundEvents.SPEAR_THROWN_ID, HerbiarySoundEvents.SPEAR_THROWN);
        Registry.register(Registry.SOUND_EVENT, HerbiarySoundEvents.SPEAR_LAND_ID, HerbiarySoundEvents.SPEAR_LAND);
    }
}
