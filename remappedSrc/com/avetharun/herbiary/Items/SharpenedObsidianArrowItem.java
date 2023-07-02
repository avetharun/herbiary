package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.world.World;

public class SharpenedObsidianArrowItem extends ArrowItem {
    public SharpenedObsidianArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        ArrowEntity e =(ArrowEntity) super.createArrow(world, stack, shooter);
        alib.setMixinField(e, "isObsidian", true);
        return e;
    }
}
