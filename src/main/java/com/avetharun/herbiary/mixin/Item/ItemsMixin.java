package com.avetharun.herbiary.mixin.Item;

import com.avetharun.herbiary.Items.FlintIgniter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Items.class)
public class ItemsMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/item/FlintAndSteelItem"))
    private static FlintAndSteelItem modifyFlintAndSteelItem(Item.Settings settings) {
        return new FlintIgniter(settings);
    }
}
