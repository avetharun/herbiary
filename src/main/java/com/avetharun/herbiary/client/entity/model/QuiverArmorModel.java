package com.avetharun.herbiary.client.entity.model;

import com.avetharun.herbiary.Items.QuiverItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class QuiverArmorModel extends GeoModel<QuiverItem> {
    @Override
    public Identifier getModelResource(QuiverItem object) {
        return new Identifier("al_herbiary", "geo/quiver.geo.json");
    }

    @Override
    public Identifier getTextureResource(QuiverItem object) {
        return new Identifier("al_herbiary", "textures/armor/quill.png");
    }

    @Override
    public Identifier getAnimationResource(QuiverItem animatable) {
        return new Identifier("al_herbiary", "animations/armor_animation.json");
    }
}
