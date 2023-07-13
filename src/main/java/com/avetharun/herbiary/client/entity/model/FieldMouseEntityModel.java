package com.avetharun.herbiary.client.entity.model;

import com.avetharun.herbiary.entity.FieldMouseEntity;
import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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
        float hY = entity.getHeadYaw();
        float bY = entity.getBodyYaw();
//        body.setRotY(MathHelper.RADIANS_PER_DEGREE * -bY);
        head.setRotY(-((entity.getHeadYaw() - entity.getBodyYaw()) - 90) * ((float) Math.PI / 180F));
//        head.setRotZ(MathHelper.RADIANS_PER_DEGREE * entity.getPitch());
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
