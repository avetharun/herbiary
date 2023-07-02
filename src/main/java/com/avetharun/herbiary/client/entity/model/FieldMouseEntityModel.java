package com.avetharun.herbiary.client.entity.model;

import com.avetharun.herbiary.entity.FieldMouseEntity;
import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class FieldMouseEntityModel extends GeoModel<FieldMouseEntity> {
    private static final Identifier MODEL_LOCATION = new Identifier("al_herbiary", "geo/entity/field_mouse.geo.json");
    private static final Identifier ANIMATION_LOCATION = new Identifier("al_herbiary", "animations/entity/field_mouse.animation.json");

    private static final Identifier TEXTURE_LOCATION = new Identifier("al_herbiary", "textures/entity/field_mouse.png");
    @Override
    public Identifier getAnimationResource(FieldMouseEntity owl) {
        return ANIMATION_LOCATION;
    }

    @Override
    public void setCustomAnimations(FieldMouseEntity entity, long instanceId, AnimationState<FieldMouseEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        CoreGeoBone body = this.getAnimationProcessor().getBone("root");
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        head.setRotY(-(entity.getHeadYaw() - 90) * ((float) Math.PI / 180F));
        head.setRotX(-(entity.getPitch()) * ((float) Math.PI / 180F));
    }


    @Override
    public Identifier getModelResource(FieldMouseEntity object) {
        return MODEL_LOCATION;
    }

    @Override
    public Identifier getTextureResource(FieldMouseEntity object) {
        return TEXTURE_LOCATION;
    }
}
