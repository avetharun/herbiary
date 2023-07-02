package com.avetharun.herbiary.client.entity.model;

import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class OwlEntityModel  extends GeoModel<OwlEntity> {
    private static final Identifier MODEL_LOCATION = new Identifier("al_herbiary", "geo/entity/owl.geo.json");
    private static final Identifier ANIMATION_LOCATION = new Identifier("al_herbiary", "animations/entity/owl.animation.json");

    private static final Identifier TEXTURE_LOCATION = new Identifier("al_herbiary", "textures/entity/owl.png");
    @Override
    public Identifier getAnimationResource(OwlEntity owl) {
        return ANIMATION_LOCATION;
    }

    @Override
    public void setCustomAnimations(OwlEntity entity, long instanceId, AnimationState<OwlEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone leftWing = this.getAnimationProcessor().getBone("left");
        CoreGeoBone rightWing = this.getAnimationProcessor().getBone("right");
        CoreGeoBone body = this.getAnimationProcessor().getBone("root");
        if (entity.isFlying()) {

        } else {
            if (head != null) {
                head.setRotY(-(entity.getHeadYaw() - entity.getBodyYaw()) * ((float) Math.PI / 180F));
                head.setRotX(-(entity.getPitch()) * ((float) Math.PI / 180F));
            }
        }
        if (entity.isBaby()) {
            body.updateScale(0.5f, 0.5f, 0.5f);
        } else {
            body.updateScale(1f, 1f, 1f);
        }
    }


    @Override
    public Identifier getModelResource(OwlEntity object) {
        return MODEL_LOCATION;
    }

    @Override
    public Identifier getTextureResource(OwlEntity object) {
        return TEXTURE_LOCATION;
    }
}
