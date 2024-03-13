package com.avetharun.herbiary.Enchants.fantasy;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GracefulEnchant extends Enchantment {
    public GracefulEnchant(Rarity weight) {
        super(weight, EnchantmentTarget.WEARABLE, new EquipmentSlot[] {EquipmentSlot.CHEST});
    }
    @Override
    public int getMinPower(int level) {
        return 1;
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isIn(Herbiary.ELYTRI);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
