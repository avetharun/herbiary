package com.avetharun.herbiary.Enchants.fantasy;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ZipwingEnchant extends Enchantment {
    public ZipwingEnchant(Rarity weight) {
        super(weight, EnchantmentTarget.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isIn(Herbiary.ELYTRI);
    }
}
