package com.avetharun.herbiary.client.entity;

import com.avetharun.herbiary.Items.QuiverItem;
import com.avetharun.herbiary.client.entity.model.QuiverArmorModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class QuiverArmorRenderer extends GeoArmorRenderer<QuiverItem> {
    public QuiverArmorRenderer() {
        super(new QuiverArmorModel());
        this.head = new GeoBone(null,"armorHead", false, null, null, null );
        this.body = new GeoBone(null,"armorBody", false, null, null, null );
        this.rightArm = new GeoBone(null,"armorRightArm", false, null, null, null );
        this.leftArm = new GeoBone(null,"armorLeftArm", false, null, null, null );
        this.rightLeg = new GeoBone(null,"armorRightLeg", false, null, null, null );
        this.leftLeg = new GeoBone(null,"armorLeftLeg", false, null, null, null );
        this.rightBoot = new GeoBone(null,"armorRightBoot", false, null, null, null );
        this.leftBoot = new GeoBone(null,"armorLeftBoot", false, null, null, null );
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        this.setBoneVisible(head, true);
        this.setBoneVisible(body, true);
        this.setBoneVisible(rightArm, true);
        this.setBoneVisible(leftArm, true);
        this.setBoneVisible(rightLeg, true);
        this.setBoneVisible(leftLeg, true);
        this.setBoneVisible(rightBoot, true);
        this.setBoneVisible(leftBoot, true);
    }
}
