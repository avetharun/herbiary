package com.avetharun.herbiary.client.entity.model;

import com.avetharun.herbiary.entity.OwlEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OwlEntityModel  extends AnimatedGeoModel<OwlEntity> {
    private static final Identifier MODEL_LOCATION = new Identifier("al_herbiary", "geo/entity/owl.geo.json");
    private static final Identifier ANIMATION_LOCATION = new Identifier("al_herbiary", "animations/entity/owl.animation.json");

    private static final Identifier TEXTURE_LOCATION = new Identifier("al_herbiary", "textures/entity/owl.png");

    @Override
    public Identifier getAnimationResource(OwlEntity owl) {
        return ANIMATION_LOCATION;
    }

    @Override
    public void setLivingAnimations(OwlEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");
        IBone leftWing = this.getAnimationProcessor().getBone("left");
        IBone rightWing = this.getAnimationProcessor().getBone("right");
        IBone body = this.getAnimationProcessor().getBone("root");

        if (entity.isFlying()) {

        } else {
            if (head != null) {
                head.setRotationY(-(entity.getHeadYaw() - entity.getBodyYaw()) * ((float) Math.PI / 180F));
                head.setRotationX(-(entity.getPitch()) * ((float) Math.PI / 180F));
            }
        }

        if (entity.isBaby()) {
            IBone root = this.getAnimationProcessor().getBone("root");
            if (root != null) {
                root.setScaleX(0.5f);
                root.setScaleY(0.5f);
                root.setScaleZ(0.5f);
            }
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
